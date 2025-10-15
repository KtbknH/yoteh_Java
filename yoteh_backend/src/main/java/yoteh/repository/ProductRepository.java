package yoteh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoteh.entity.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Recherche des produits par catégorie
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    
    /**
     * Recherche des produits par nom (insensible à la casse)
     */
    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name, String description, Pageable pageable
    );
    
    /**
     * Recherche des produits par catégorie et nom
     */
    Page<Product> findByCategoryIdAndNameContainingIgnoreCase(
            Long categoryId, String name, Pageable pageable
    );
    
    /**
     * Recherche un produit par SKU
     */
    Optional<Product> findBySku(String sku);
    
    /**
     * Vérifie si un SKU existe déjà
     */
    boolean existsBySku(String sku);
}