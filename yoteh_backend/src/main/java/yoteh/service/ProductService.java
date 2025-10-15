package yoteh.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoteh.dto.ProductDTO;
import yoteh.dto.ProductRequest;
import yoteh.entity.Category;
import yoteh.entity.Product;
import yoteh.repository.CategoryRepository;
import yoteh.repository.ProductRepository;

import java.time.LocalDateTime;

/**
 * Service de gestion des produits
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Récupère tous les produits avec pagination et filtres
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Long categoryId, String search, Pageable pageable) {
        Page<Product> products;

        if (categoryId != null && search != null && !search.isEmpty()) {
            products = productRepository.findByCategoryIdAndNameContainingIgnoreCase(
                categoryId, search, pageable
            );
        } else if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId, pageable);
        } else if (search != null && !search.isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                search, search, pageable
            );
        } else {
            products = productRepository.findAll(pageable);
        }

        return products.map(this::convertToDTO);
    }

    /**
     * Récupère un produit par son ID
     */
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID : " + id));
        return convertToDTO(product);
    }

    /**
     * Crée un nouveau produit
     */
    @Transactional
    public ProductDTO createProduct(ProductRequest request) {
        log.info("Création d'un nouveau produit : {}", request.getName());

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .sku(request.getSku())
                .category(category)
                .stockQuantity(request.getStockQuantity())
                .stockThreshold(request.getStockThreshold())
                .active(true)
                .weight(request.getWeight())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Produit créé avec succès : ID {}", savedProduct.getId());

        return convertToDTO(savedProduct);
    }

    /**
     * Met à jour un produit existant
     */
    @Transactional
    public ProductDTO updateProduct(Long id, ProductRequest request) {
        log.info("Mise à jour du produit ID : {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID : " + id));

        // Mettre à jour les champs
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setSku(request.getSku());
        product.setStockQuantity(request.getStockQuantity());
        product.setStockThreshold(request.getStockThreshold());
        product.setWeight(request.getWeight());
        product.setUpdatedAt(LocalDateTime.now());

        // Mettre à jour la catégorie si nécessaire
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        log.info("Produit mis à jour avec succès : ID {}", updatedProduct.getId());

        return convertToDTO(updatedProduct);
    }

    /**
     * Supprime un produit
     */
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Suppression du produit ID : {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID : " + id));

        productRepository.delete(product);
        log.info("Produit supprimé avec succès : ID {}", id);
    }

    /**
     * Convertit un Product en ProductDTO
     */
    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .sku(product.getSku())
                .images(product.getImages())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .stockQuantity(product.getStockQuantity())
                .active(product.isActive())
                .averageRating(product.getAverageRating())
                .weight(product.getWeight())
                .createdAt(product.getCreatedAt())
                .build();
    }
}