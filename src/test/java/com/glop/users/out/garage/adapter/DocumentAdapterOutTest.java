package com.glop.users.out.garage.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentAdapterOutTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    private DocumentAdapterOut adapter;

    @BeforeEach
    void setUp() {
        adapter = new DocumentAdapterOut(s3Client, s3Presigner);
    }

    @Test
    void testUploadShouldPutObjectWithKeyAndType() {
        ArgumentCaptor<PutObjectRequest> request = ArgumentCaptor.forClass(PutObjectRequest.class);

        adapter.upload("shopkeepers/1/doc.pdf", new byte[]{1, 2, 3}, "application/pdf");

        verify(s3Client).putObject(request.capture(), any(RequestBody.class));
        assertThat(request.getValue().key()).isEqualTo("shopkeepers/1/doc.pdf");
        assertThat(request.getValue().contentType()).isEqualTo("application/pdf");
    }

    @Test
    void testGetDownloadUrlsShouldReturnOneUrlPerFile() throws MalformedURLException {
        S3Object file = S3Object.builder().key("shopkeepers/1/doc.pdf").build();
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class)))
                .thenReturn(ListObjectsV2Response.builder().contents(file).build());
        PresignedGetObjectRequest presigned = mock(PresignedGetObjectRequest.class);
        when(presigned.url()).thenReturn(URI.create("http://localhost:3900/active/shopkeepers/1/doc.pdf").toURL());
        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presigned);

        List<String> urls = adapter.getDownloadUrls("shopkeepers/1/");

        assertThat(urls.size()).isEqualTo(1);
        assertThat(urls.get(0)).isEqualTo("http://localhost:3900/active/shopkeepers/1/doc.pdf");
    }
}
