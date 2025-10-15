package yoteh.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 2000)
    private String description;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(unique = true)
    private String sku;
    
    @Builder.Default  // ⬅️ AJOUTÉ
    @ElementCollection
    private List<String> images = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Builder.Default  // ⬅️ AJOUTÉ
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants = new ArrayList<>();
    
    @Builder.Default  // ⬅️ AJOUTÉ
    private Integer stockQuantity = 0;
    
    @Builder.Default  // ⬅️ AJOUTÉ
    private Integer stockThreshold = 10;
    
    @Builder.Default  // ⬅️ AJOUTÉ
    private boolean active = true;
    
    @Builder.Default  // ⬅️ AJOUTÉ
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Builder.Default  // ⬅️ AJOUTÉ
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Builder.Default  // ⬅️ AJOUTÉ
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
    
    @Builder.Default  // ⬅️ AJOUTÉ
    private Double averageRating = 0.0;
    
    private BigDecimal weight;
}