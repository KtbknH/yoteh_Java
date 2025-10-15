package yoteh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoteh.entity.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Recherche une catégorie par nom
     */
    Optional<Category> findByName(String name);
    
    /**
     * Vérifie si une catégorie existe par nom
     */
    boolean existsByName(String name);
}