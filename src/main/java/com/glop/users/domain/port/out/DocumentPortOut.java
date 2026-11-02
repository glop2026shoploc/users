package com.glop.users.domain.port.out;

public interface DocumentPortOut {
    void upload(String key, byte[] content, String contentType);
}
