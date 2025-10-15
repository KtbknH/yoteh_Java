package yoteh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    
    @Builder.Default
    private List<String> images = new ArrayList<>();
    
    private Long categoryId;
    private String categoryName;
    private Integer stockQuantity;
    private boolean active;
    private Double averageRating;
    private BigDecimal weight;
    private LocalDateTime createdAt;
}