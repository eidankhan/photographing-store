package com.photographing.store.app.transformer;

import com.photographing.store.app.dto.FileDTO;
import com.photographing.store.app.modal.File;

// This class transforms DTOs to Bean objects and vice versa
public class FileTransformer {
    public FileDTO fileToFileDTO(File file) {
        FileDTO fileDTO = new FileDTO();
        if (file != null) {
            fileDTO.setId(file.getId() != null ? file.getId() : null);
            fileDTO.setName(file.getName() != null ? file.getName() : null);
            fileDTO.setUrl(file.getUrl() != null ? file.getUrl() : null);
            //fileDTO.setUser(file.getUser() != null ? file.getUser() : null);
        }
        return fileDTO;
    }

    public File fileDTOToFile(FileDTO fileDTO) {
        File file = new File();
        if (fileDTO != null) {
            file.setId(fileDTO.getId() != null ? fileDTO.getId() : null);
            file.setName(fileDTO.getName() != null ? fileDTO.getName() : null);
            file.setUrl(fileDTO.getUrl() != null ? fileDTO.getUrl() : null);
            //file.setUser(fileDTO.getUser() != null ? fileDTO.getUser() : null);
        }
        return file;
    }
}
