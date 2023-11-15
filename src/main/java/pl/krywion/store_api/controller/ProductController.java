package pl.krywion.store_api.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.krywion.store_api.model.Product;
import pl.krywion.store_api.service.ProductService;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add-product")
    public ResponseEntity<String> addProduct(@RequestParam("title") String title,
                                             @RequestParam("price") Double price,
                                             @RequestParam("category")  String category,
                                             String description,
                                             @RequestParam MultipartFile image) {
        productService.addProduct(title, price, category, description, image);

        return new ResponseEntity<>("Product added", HttpStatus.CREATED);
    }


    @GetMapping("/get-products/{page}/{amount}")
    public ResponseEntity<List<Product>> getProducts(
            @PathVariable("page") Integer page,
            @PathVariable("amount") Integer amount
    ) {
        System.out.println("getProducts");
        return new ResponseEntity<>(productService.getProducts(page, amount), HttpStatus.OK);
    }

    @GetMapping("/get-products-by-category/{page}/{amount}/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @PathVariable("page") Integer page,
            @PathVariable("amount") Integer amount,
            @PathVariable("category") String category) {
        return new ResponseEntity<>(productService.getProductsByCategory(page, amount, category), HttpStatus.OK);
    }

    @GetMapping("/get-products-category")
    public ResponseEntity<List<String>> getProductsCategory() {
        System.out.println(productService.getCategories());
        return new ResponseEntity<>(productService.getCategories(), HttpStatus.OK);
    }

    @GetMapping("/get-products-count")
    public ResponseEntity<Integer> getMaxPage() {
        return new ResponseEntity<>(productService.getProductsCount(), HttpStatus.OK);
    }

    @GetMapping("/get-products-count/{category}")
    public ResponseEntity<Integer> getMaxPageByCategory( @PathVariable("category") String category) {
        return new ResponseEntity<>(productService.getProductsCountByCategory(category), HttpStatus.OK);
    }




}
