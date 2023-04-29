package com.github.gasfgrv.storage.repository;

import com.github.gasfgrv.storage.model.FileDB;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesRepository extends JpaRepository<FileDB, UUID> {
}
