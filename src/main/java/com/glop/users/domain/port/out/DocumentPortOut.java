package com.glop.users.domain.port.out;

import java.util.List;

public interface DocumentPortOut {
    void upload(String key, byte[] content, String contentType);
    List<String> getDownloadUrls(String folder);
}
