package pl.krywion.store_api.service;

import com.amazonaws.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.krywion.store_api.model.Product;
import pl.krywion.store_api.repository.ProductRepository;

import java.io.*;
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
        File tempFile;
        try {
            tempFile = File.createTempFile("temp-image", ".png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Kopiujemy dane z MultipartFile do tymczasowego pliku
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
        // Należy pamiętać o usunięciu tymczasowego pliku, gdy już nie jest potrzebny
        tempFile.delete();

        emailService.sendMail("szymon.wojakoss@gmail.com", "subject", "text");


    }


}
