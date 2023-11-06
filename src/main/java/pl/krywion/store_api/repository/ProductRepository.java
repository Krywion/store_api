package pl.krywion.store_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import pl.krywion.store_api.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findTop12ByOrderByPriceAsc();

    List<Product> findTop12ByOrderByPriceDesc();


    List<Product> findTop24ByOrderByPriceAsc();

    List<Product> findTop24ByOrderByPriceDesc();

    List<Product> findTop36ByOrderByPriceAsc();

    List<Product> findTop36ByOrderByPriceDesc();

    List<Product> find12();

    List<Product> find24();

    List<Product> find36();





}
