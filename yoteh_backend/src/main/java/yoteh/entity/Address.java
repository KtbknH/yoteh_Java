package yoteh.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant une adresse de livraison
 */
@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false)
    private String street;
    
    private String complement; // Complément d'adresse (bâtiment, étage, etc.)
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String postalCode;
    
    private String province; // Province ou région
    
    @Column(nullable = false)
    private String country;
    
    @Builder.Default
    @Column(name = "is_default")
    private boolean isDefault = false; // Adresse par défaut
}