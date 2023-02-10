package com.photographing.store.app.service.impl;

import com.photographing.store.app.dto.FileDTO;
import com.photographing.store.app.modal.File;
import com.photographing.store.app.repository.FileRepository;
import com.photographing.store.app.service.FileService;
import com.photographing.store.app.transformer.FileTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {
    // Define a root path for the files to be saved in the local directory
    private final Path rootPath = Paths.get("photos");

    @Autowired
    private FileRepository fileRepository;

    // Initialize the local directory for storing files
    @Override
    public void init() {
        try {
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public File save(File file) {
        return fileRepository.save(file);
    }

    @Override
    public File upload(MultipartFile file) {
        File uploadedFile = null;
        try {
            // Upload the file in the local directory
            Files.copy(file.getInputStream(), this.rootPath.resolve(file.getOriginalFilename()));
            uploadedFile = new File();
            uploadedFile.setUrl(rootPath+"/"+file.getOriginalFilename());
            uploadedFile.setName(file.getOriginalFilename());
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
        return uploadedFile;
    }

    // This method verifies whether the file is uploaded  or not in the local directory
    public Boolean isFileUploaded(MultipartFile file) {
        Boolean isFileUploaded = false;
        try {
            // Copy the file contents
            Files.copy(file.getInputStream(), this.rootPath.resolve(file.getOriginalFilename()));
            System.out.println("File uploaded successfully........");
            isFileUploaded = true;
        } catch (Exception e) {
            isFileUploaded = false;
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
        return isFileUploaded;
    }

    // This method loads all the files
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootPath, 1).filter(path -> !path.equals(this.rootPath)).map(this.rootPath::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public List<File> findAll() {
        return fileRepository.findAll();
    }

    // This method transforms FileDTOs objects into File objects
    @Override
    public List<File> fileDTOsListToFileList(List<FileDTO> fileDTOs) {
        FileTransformer fileTransformer = new FileTransformer();
        List<File> fileList = new ArrayList<>();
        for(FileDTO fileDTO : fileDTOs){
            fileList.add(fileTransformer.fileDTOToFile(fileDTO));
        }
        return fileList;
    }


    // This method transforms Files objects into FileDTOs objects
    @Override
    public List<FileDTO> fileListToFileDTOsList(List<File> files) {
        FileTransformer fileTransformer = new FileTransformer();
        List<FileDTO> fileDTOs = new ArrayList<>();
        for(File file : files) {
           fileDTOs.add(fileTransformer.fileToFileDTO(file));
        }
        return fileDTOs;
    }

    // This method delete the file from the local directory
    @Override
    public boolean delete(String filename) {
        try {
            // Get path to the file from filename
            Path file = rootPath.resolve(filename);
            // Delete the file if it exists in the directory
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public File findById(Long id) {
        return fileRepository.findById(id).isPresent() ? fileRepository.findById(id).get() : null;
    }

    // This method delegates file url from the database
    @Override
    public void deleteFromDatabase(Long id) {
        fileRepository.deleteById(id);
    }

    // This method loads the file
    @Override
    public Resource load(String filename) {
        try {
            Path file = rootPath.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
