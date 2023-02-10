package com.photographing.store.app.controller;

import com.photographing.store.app.modal.File;
import com.photographing.store.app.modal.User;
import com.photographing.store.app.repository.UserRepository;
import com.photographing.store.app.service.FileService;
import com.photographing.store.app.service.UserService;
import com.photographing.store.app.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.photographing.store.app.util.ResponseMessage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/files")
@CrossOrigin
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    // This endpoint uploads photos for a specific user
    @PostMapping("/upload/{userId}")
    public ResponseEntity<ResponseMessage> uploadAndSaveFile(@RequestParam("file") MultipartFile file, @PathVariable("userId") Long userId) {
        System.out.println("Uploading file........");
        String message = "";
        // Uploading file in the local directory
        File uploadedFile = fileService.upload(file);
        // Check if the file is uploaded in the local directory
        if (uploadedFile != null) {
            User currentUser = userService.findById(userId);
            // Saving file in the database for the selected user
            Optional<File> uploadedFileToDB = userRepository.findById(userId).map(user ->{
               user.getFiles().add(uploadedFile);
               return fileService.save(uploadedFile);
            });
            // Check if the file is uploaded for the user in the database
            if(uploadedFileToDB.get() != null){
                message = "Uploaded the file successfully in the database: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            }
            message = "Uploaded the file successfully in the directory: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }
        message = "Could not upload the file: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    }

    // This endpoint fetches all the files from the database
    @GetMapping
    public ResponseEntity<List<File>> findAll(){
        System.out.println("Finding all files...");
        List<File> files = fileService.findAll();
         return ResponseEntity.ok(files);
        //return fileService.fileListToFileDTOsList(files);
    }

    // This endpoint deletes the file from the local directory and database
    @DeleteMapping("/{fileId}")
    public GenericResponse delete(@PathVariable Long fileId){
        System.out.println("Deleting file with id " + fileId);
        File fileById = fileService.findById(fileId);
        // Check if the file exists in the database or not
        if(fileById == null)
            return new GenericResponse("Failure", "The file does not exist!");
        try {
            // Delete file from the local directory
            boolean existed = fileService.delete(fileById.getName());
            if (existed) {
                // Delete the file from the database
                fileService.deleteFromDatabase(fileId);
                return new GenericResponse("Success", "Delete file successfully: " + fileById.getName());
            }
            return new GenericResponse("Success", "The file does not exist!");
        } catch (Exception e) {
            return new GenericResponse("Success", "Could not delete the file: " + fileById.getName() + ". Error: " + e.getMessage());
        }
    }
}
