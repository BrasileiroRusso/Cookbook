package ru.geekbrains.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.cookbook.domain.file.UploadedFileLink;
import java.util.List;

@Repository
public interface UploadedFileLinkRepository extends JpaRepository<UploadedFileLink, Long> {
    List<UploadedFileLink> findAllByObjectIdAndObjectType(Long objectId, String objectType);
}
