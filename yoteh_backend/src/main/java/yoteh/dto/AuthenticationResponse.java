package yoteh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoteh.enums.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    
    private String token;
    
    @Builder.Default  // ⬅️ AJOUTÉ
    private String type = "Bearer";
    
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private Integer loyaltyPoints;
}