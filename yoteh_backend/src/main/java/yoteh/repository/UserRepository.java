package yoteh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoteh.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Recherche un utilisateur par email
     * @param email Email de l'utilisateur
     * @return Optional contenant l'utilisateur si trouvé
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Vérifie si un email existe déjà
     * @param email Email à vérifier
     * @return true si l'email existe
     */
    boolean existsByEmail(String email);
}