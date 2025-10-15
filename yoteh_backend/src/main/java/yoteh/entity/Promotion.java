package yoteh.entity;

import jakarta.persistence.*;
import lombok.*;
import yoteh.enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String code;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    
    private BigDecimal discountValue;
    
    private BigDecimal minOrderAmount;
    private Integer maxUsage;
    
    @Builder.Default  // ⬅️ AJOUTÉ
    private Integer currentUsage = 0;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    @Builder.Default  // ⬅️ AJOUTÉ
    private boolean active = true;
}