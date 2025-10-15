package yoteh.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_variants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    private String size;
    private String color;
    
    @Column(unique = true)
    private String sku;
    
    @Builder.Default  // ⬅️ AJOUTÉ
    private Integer stockQuantity = 0;
    
    @Builder.Default  // ⬅️ AJOUTÉ
    private BigDecimal priceAdjustment = BigDecimal.ZERO;
}