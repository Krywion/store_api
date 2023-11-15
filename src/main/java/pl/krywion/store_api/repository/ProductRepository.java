package pl.krywion.store_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.krywion.store_api.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query("SELECT p FROM Product p WHERE p.category = :category")
    Page<Product> findByCategory(@Param("category") String category, Pageable pageable);

    @Query("SELECT COUNT (*) FROM Product WHERE category = :category")
    Integer countByCategory(@Param("category") String category);

    @Query("SELECT COUNT (*) FROM Product")
    Integer countAll();
}
