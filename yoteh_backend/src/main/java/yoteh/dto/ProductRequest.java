package yoteh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    
    @NotBlank(message = "Le nom du produit est obligatoire")
    private String name;
    
    private String description;
    
    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private BigDecimal price;
    
    private String sku;
    
    private Long categoryId;
    
    @Builder.Default
    private Integer stockQuantity = 0;
    
    @Builder.Default
    private Integer stockThreshold = 10;
    
    private BigDecimal weight;
}