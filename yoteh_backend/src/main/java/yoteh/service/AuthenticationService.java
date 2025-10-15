package yoteh.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoteh.dto.AuthenticationRequest;
import yoteh.dto.AuthenticationResponse;
import yoteh.dto.RegisterRequest;
import yoteh.entity.User;
import yoteh.enums.Role;
import yoteh.exception.EmailAlreadyExistsException;
import yoteh.exception.InvalidCredentialsException;
import yoteh.repository.UserRepository;

import java.time.LocalDateTime;

/**
 * Service gérant l'authentification et l'inscription des utilisateurs
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Inscrit un nouvel utilisateur
     * 
     * @param request Données d'inscription
     * @return Réponse avec le token JWT
     * @throws EmailAlreadyExistsException si l'email existe déjà
     */
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Tentative d'inscription pour l'email : {}", request.getEmail());

        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Tentative d'inscription avec un email existant : {}", request.getEmail());
            throw new EmailAlreadyExistsException("Cet email est déjà utilisé");
        }

        // Créer le nouvel utilisateur
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .role(Role.CLIENT)
                .enabled(true)
                .loyaltyPoints(0)
                .createdAt(LocalDateTime.now())
                .build();

        // Sauvegarder l'utilisateur
        User savedUser = userRepository.save(user);
        log.info("Utilisateur créé avec succès : {}", savedUser.getEmail());

        // Générer le token JWT
        String jwtToken = jwtService.generateToken(user);

        // Retourner la réponse
        return buildAuthenticationResponse(savedUser, jwtToken);
    }

    /**
     * Authentifie un utilisateur existant
     * 
     * @param request Identifiants de connexion
     * @return Réponse avec le token JWT
     * @throws InvalidCredentialsException si les identifiants sont invalides
     */
    @Transactional(readOnly = true)
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Tentative de connexion pour l'email : {}", request.getEmail());

        try {
            // Authentifier avec Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.warn("Échec de connexion pour l'email : {}", request.getEmail());
            throw new InvalidCredentialsException("Email ou mot de passe incorrect");
        }

        // Récupérer l'utilisateur
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Utilisateur non trouvé"));

        log.info("Connexion réussie pour : {}", user.getEmail());

        // Générer le token JWT
        String jwtToken = jwtService.generateToken(user);

        // Retourner la réponse
        return buildAuthenticationResponse(user, jwtToken);
    }

    /**
     * Construit la réponse d'authentification
     */
    private AuthenticationResponse buildAuthenticationResponse(User user, String token) {
        return AuthenticationResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .loyaltyPoints(user.getLoyaltyPoints())
                .build();
    }

    /**
     * Valide un token JWT
     * 
     * @param token Token à valider
     * @return true si le token est valide
     */
    public boolean validateToken(String token) {
        try {
            String email = jwtService.extractUsername(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new InvalidCredentialsException("Utilisateur non trouvé"));
            
            return jwtService.isTokenValid(token, user);
        } catch (Exception e) {
            log.error("Erreur lors de la validation du token", e);
            return false;
        }
    }

    /**
     * Récupère un utilisateur à partir d'un token
     * 
     * @param token Token JWT
     * @return Utilisateur
     */
    public User getUserFromToken(String token) {
        String email = jwtService.extractUsername(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Utilisateur non trouvé"));
    }
}