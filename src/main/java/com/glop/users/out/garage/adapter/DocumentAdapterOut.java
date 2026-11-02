package com.glop.users.out.garage.adapter;

import com.glop.users.domain.port.out.DocumentPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DocumentAdapterOut implements DocumentPortOut {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${garage.bucket}")
    private String bucket;

    @Override
    public void upload(String key, byte[] content, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();
        s3Client.putObject(request, RequestBody.fromBytes(content));
    }

    @Override
    public List<String> getDownloadUrls(String folder) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(folder)
                .build();

        List<String> urls = new ArrayList<>();
        for (S3Object object : s3Client.listObjectsV2(listRequest).contents()) {
            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(object.key())
                    .build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(getRequest)
                    .build();
            urls.add(s3Presigner.presignGetObject(presignRequest).url().toString());
        }
        return urls;
    }
}
