package pl.krywion.store_api.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

@Service
public class FileService {

    @Autowired
    private AmazonS3 s3client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String generateUrl(String filename, HttpMethod http) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 1);
        URL url = s3client.generatePresignedUrl(bucketName, filename, cal.getTime(), http);
        return url.toString();
    }

    public void sendFileToServer(String presignedUrl, File file) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(presignedUrl);
            httpPut.setHeader("Content-Type", "image/jpeg");
            FileEntity fileEntity = new FileEntity(file);
            httpPut.setEntity(fileEntity);

            HttpResponse response = httpClient.execute(httpPut);
            HttpEntity responseEntity = response.getEntity();

            // Odczytaj odpowiedź serwera, jeśli to konieczne
            System.out.println("Wykonano żądanie PUT do serwera S3.");
            String responseString = EntityUtils.toString(responseEntity);
            System.out.println(responseString);
            // ...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
