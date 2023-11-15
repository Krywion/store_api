package pl.krywion.store_api.service;

import com.amazonaws.HttpMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.krywion.store_api.model.Product;
import pl.krywion.store_api.repository.ProductRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final FileService fileService;

    private final EmailService emailService;

    public ProductService(ProductRepository productRepository, FileService fileService, EmailService emailService) {
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.emailService = emailService;
    }

    public void addProduct(String title, Double price, String category, String description, MultipartFile image) {
        // copyying MultiPartFile to File
        String extension = Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf(".") + 1);
        File tempFile;
        try {
            tempFile = File.createTempFile("temp-image", ".jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UUID uuid = UUID.randomUUID();
        String imageAccessUrl = "https://storeapi-images.s3.eu-central-1.amazonaws.com/" + uuid + "." + extension;
        String presignedUrl = fileService.generateUrl(uuid + "." + "jpeg", HttpMethod.PUT);
        fileService.sendFileToServer(presignedUrl, tempFile);


        Product product = new Product(title, price, category, description, imageAccessUrl);
        productRepository.save(product);


        if (!tempFile.delete()) {
            throw new RuntimeException("Failed to delete temp file");
        }

        String message = "Product added:\nTitle: " + title + "\nPrice: " + price + "\nCategory: " + category + "\nDescription: " + description + "\nImageAccessUrl: " + imageAccessUrl;

        emailService.sendMail("krywionStoreAdNotif@gmail.com", "[WebStore] Added new product", message);
    }


    public List<Product> getProducts(Integer page, Integer amount) {
        Pageable pageable = PageRequest.of(page, amount);
        Page<Product> products = productRepository.findAll(pageable);
        return products.getContent();
    }


    public List<String> getCategories() {
        return Product.getCategories();
    }

    public List<Product> getProductsByCategory(Integer page, Integer amount, String category) {
        Pageable pageable = PageRequest.of(page, amount);
        Page<Product> products = productRepository.findByCategory(category, pageable);
        return products.getContent();
    }


    public Integer getProductsCountByCategory(String category) {
        return productRepository.countByCategory(category);
    }

    public Integer getProductsCount() {
        return productRepository.countAll();
    }


}
