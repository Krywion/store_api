package pl.krywion.store_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.krywion.store_api.service.FileService;
import pl.krywion.store_api.service.ProductService;

@RestController
public class ProductController {

    private final ProductService productService;
    private final FileService fileService;

    public ProductController(ProductService productService, FileService fileService) {
        this.productService = productService;
        this.fileService = fileService;
    }

    @PostMapping("/addproduct")
    public ResponseEntity<String> addProduct(@RequestParam("title") String title,
                                             @RequestParam("price") Double price,
                                             @RequestParam("category")  String category,
                                             String description,
                                             @RequestParam MultipartFile image) {
        productService.addProduct(title, price, category, description, image);

        return ResponseEntity.ok("Product added");
    }
}
