package com.app.bobbookstore.uploads.application;

import com.app.bobbookstore.uploads.application.port.UploadUseCase;
import com.app.bobbookstore.uploads.domain.Upload;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

@Service
public class UploadService implements UploadUseCase {

    Map<String, Upload> storage = new ConcurrentHashMap<>();

    @Override
    public Upload save(SaveUploadCommand command) {
        String newId = RandomStringUtils.randomAlphabetic(8);
        Upload upload = new Upload(newId,
                command.getFile(),
                command.getContentType(),
                command.getFilename(),
                LocalDateTime.now());

        storage.put(upload.getId(), upload);
        return upload;
    }

    @Override
    public Optional<Upload> getById(String id){
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void removeById(String id) {
        storage.remove(id);
    }

}
