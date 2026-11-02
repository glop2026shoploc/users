package com.glop.users.out.garage.adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DocumentAdapterOutTest {

    @Mock
    private S3Client s3Client;

    @Test
    void testUploadShouldPutObjectWithKeyAndType() {
        DocumentAdapterOut adapter = new DocumentAdapterOut(s3Client);
        ArgumentCaptor<PutObjectRequest> request = ArgumentCaptor.forClass(PutObjectRequest.class);

        adapter.upload("shopkeepers/1/doc.pdf", new byte[]{1, 2, 3}, "application/pdf");

        verify(s3Client).putObject(request.capture(), any(RequestBody.class));
        assertThat(request.getValue().key()).isEqualTo("shopkeepers/1/doc.pdf");
        assertThat(request.getValue().contentType()).isEqualTo("application/pdf");
    }
}
