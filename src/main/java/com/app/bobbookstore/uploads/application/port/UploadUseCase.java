package com.app.bobbookstore.uploads.application.port;

import com.app.bobbookstore.uploads.domain.Upload;
import lombok.Value;

import java.util.Optional;

public interface UploadUseCase {

    Upload save(SaveUploadCommand command);

    Optional<Upload> getById(String id);

    void removeById(String id);

    @Value
    class SaveUploadCommand{
        String filename;
        byte[] file;
        String contentType;
    }
}
