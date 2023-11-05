package pl.krywion.store_api.controller;

import com.amazonaws.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.krywion.store_api.service.FileService;

import java.util.UUID;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/uploadurl")
    public ResponseEntity<String> fileUploadUrl(@RequestParam String extension) {
        return ResponseEntity.ok(fileService.generateUrl(UUID.randomUUID() + "." + extension, HttpMethod.PUT));
    }

    @GetMapping("/downloadurl")
    public ResponseEntity<String> fileDownloadUrl(@RequestParam String filename) {
        return ResponseEntity.ok(fileService.generateUrl(filename, HttpMethod.GET));
    }
}
