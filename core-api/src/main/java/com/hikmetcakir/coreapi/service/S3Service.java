package com.hikmetcakir.coreapi.service;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    public String uploadFile(MultipartFile file) throws IOException {
        String key = "thumbnails/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        // Görseli sıkıştır ve yeniden boyutlandır
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .width(1280)
                .outputQuality(0.75)
                .outputFormat("jpg")
                .toOutputStream(outputStream);

        byte[] compressedBytes = outputStream.toByteArray();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("image/jpeg")
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(compressedBytes));

        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
    }
}