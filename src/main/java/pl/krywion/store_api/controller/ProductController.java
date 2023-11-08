package pl.krywion.store_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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


    @GetMapping("/get-products")
    public ResponseEntity<List<Product>> getProducts(@RequestParam Integer amount) {
        System.out.println("getProducts");
        return new ResponseEntity<>(productService.getProducts(amount), HttpStatus.OK);
    }

    @GetMapping("/get-products-order")
    public ResponseEntity<List<Product>> getProductsOrder(@RequestParam Integer amount, @RequestParam String order) {
        return new ResponseEntity<>(productService.getProductsbYOrder(amount, order), HttpStatus.OK);
    }



}
