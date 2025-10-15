package yoteh.entity;

import jakarta.persistence.*;
import lombok.*;
import yoteh.enums.OrderStatus;
import yoteh.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String orderNumber;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Builder.Default  // ⬅️ AJOUTÉ
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    @Builder.Default  // ⬅️ AJOUTÉ
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    private BigDecimal subtotal;
    private BigDecimal shippingCost;
    
    @Builder.Default  // ⬅️ AJOUTÉ
    private BigDecimal discount = BigDecimal.ZERO;
    
    private BigDecimal total;
    
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address shippingAddress;
    
    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;
    
    @Builder.Default  // ⬅️ AJOUTÉ
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime paidAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    
    private String trackingNumber;
    private String notes;
}