package com.photographing.store.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.photographing.store.app.modal.File;

// This interface interacts with database for data persistence
public interface FileRepository extends JpaRepository<File, Long> {
}
