package pl.krywion.store_api.service;

import com.amazonaws.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.krywion.store_api.model.Product;
import pl.krywion.store_api.repository.ProductRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
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
        File tempFile;
        try {
            tempFile = File.createTempFile("temp-image", ".png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UUID uuid = UUID.randomUUID();
        String imageAccessUrl = "https://storeapi-images.s3.eu-central-1.amazonaws.com/" + uuid + "." + "jpeg";
        String presignedUrl = fileService.generateUrl(uuid + "." + "jpeg" , HttpMethod.PUT);
        fileService.sendFileToServer(presignedUrl, tempFile);


        Product product = new Product(title, price, category, description, imageAccessUrl);
        productRepository.save(product);


        if(!tempFile.delete()) {
            throw new RuntimeException("Failed to delete temp file");
        }

        String message = "Product added:\nTitle: " + title + "\nPrice: " + price + "\nCategory: " + category + "\nDescription: " + description + "\nImageAccessUrl: " + imageAccessUrl;

        emailService.sendMail("szymon.wojakoss@gmail.com", "[WebStore] Added new product", message);
    }


    public List<Product> getProducts(Integer amount) {
        return switch (amount) {
            case 24 -> productRepository.find24();
            case 36 -> productRepository.find36();
            default -> productRepository.find12();
        };
    }


    public List<Product> getProductsbYOrder(Integer amount, String order) {
        return switch (amount) {
            case 24 -> {
                if (order.equals("asc")) {
                    yield productRepository.findTop24ByOrderByPriceAsc();
                }
                yield productRepository.findTop24ByOrderByPriceDesc();
            }
            case 36 -> {
                if (order.equals("asc")) {
                    yield productRepository.findTop36ByOrderByPriceAsc();
                }
                yield productRepository.findTop36ByOrderByPriceDesc();
            }
            default -> {
                if (order.equals("asc")) {
                    yield productRepository.findTop12ByOrderByPriceAsc();
                }
                yield productRepository.findTop12ByOrderByPriceDesc();
            }
        };
    }
}
