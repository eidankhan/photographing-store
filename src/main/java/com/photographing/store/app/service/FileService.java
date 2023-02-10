package com.photographing.store.app.service;

import com.photographing.store.app.dto.FileDTO;
import com.photographing.store.app.modal.File;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

// This interface defines all the methods for file handling and persisting files
@Service
public interface FileService {
     void init();

     File save(File file);

     File upload(MultipartFile file);

     Boolean isFileUploaded(MultipartFile file);

     Resource load(String filename);
     Stream<Path> loadAll();

     List<File> findAll();

     List<File> fileDTOsListToFileList(List<FileDTO> fileDTOs);

     List<FileDTO> fileListToFileDTOsList(List<File> files);

     public boolean delete(String filename);

     public File findById(Long id);

     public void deleteFromDatabase(Long id);
}
