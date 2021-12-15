package ru.geekbrains.cookbook.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.cookbook.service.FileStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import static org.apache.http.entity.ContentType.*;


@Service("awsFileStorageService")
@AllArgsConstructor
public class AWSFileStorageService implements FileStorageService {
    private final static String BUCKET_NAME = "cookbook-storage-test";
    private final static String IMAGE_DIR = "Images";

    private final AmazonS3 amazonS3;

    @Override
    public String save(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                           IMAGE_BMP.getMimeType(),
                           IMAGE_GIF.getMimeType(),
                           IMAGE_JPEG.getMimeType()).contains(multipartFile.getContentType())) {
            throw new IllegalStateException("File is not an image");
        }
        Map<String, String> metadata = new HashMap<>();
        metadata.put(HttpHeaders.CONTENT_TYPE, multipartFile.getContentType());
        metadata.put(HttpHeaders.CONTENT_LENGTH, String.valueOf(multipartFile.getSize()));

        UUID uuid = UUID.randomUUID();
        String name = multipartFile.getOriginalFilename();
        int pointIndex = name.lastIndexOf(".");
        String extension = name.substring(pointIndex);
        name = uuid + extension;
        String fileKey = String.format("%s/%s", IMAGE_DIR, name);
        try {
            upload(BUCKET_NAME, fileKey, Optional.of(metadata), multipartFile.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        return name;
    }

    @Override
    public byte[] download(String key) {
        String fileKey = String.format("%s/%s", IMAGE_DIR, key);
        try {
            S3Object object = amazonS3.getObject(BUCKET_NAME, fileKey);
            S3ObjectInputStream objectContent = object.getObjectContent();
            return IOUtils.toByteArray(objectContent);
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download the file", e);
        }
    }

    private void upload(String bucketName, String fileKey, Optional<Map<String, String>> optionalMetaData,
                        InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent(stringStringMap -> stringStringMap.forEach(objectMetadata::addUserMetadata));
        try {
            amazonS3.putObject(bucketName, fileKey, inputStream, objectMetadata);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Не удалось загрузить файл", e);
        }
    }

}

