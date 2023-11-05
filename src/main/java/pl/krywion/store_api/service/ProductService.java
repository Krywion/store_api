package pl.krywion.store_api.service;

import com.amazonaws.HttpMethod;
import org.apache.http.entity.FileEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.krywion.store_api.model.Product;
import pl.krywion.store_api.repository.ProductRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final FileService fileService;

    public ProductService(ProductRepository productRepository, FileService fileService) {
        this.productRepository = productRepository;
        this.fileService = fileService;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void addProduct(String title, Double price, String category, String description, MultipartFile image) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp-image", ".png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Kopiujemy dane z MultipartFile do tymczasowego pliku
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(image.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Teraz możesz użyć tymczasowego pliku w swoim kodzie
        String tempFileName = tempFile.getName();
        // Do something with the tempFile...

        String presignedUrl = fileService.generateUrl(UUID.randomUUID() + "." + "jpeg" , HttpMethod.PUT);
        fileService.sendFileToServer(presignedUrl, tempFile);
        Product product = new Product(title, price, category, description, tempFile.getName());
        productRepository.save(product);
        // Należy pamiętać o usunięciu tymczasowego pliku, gdy już nie jest potrzebny
        tempFile.delete();



    }


}
