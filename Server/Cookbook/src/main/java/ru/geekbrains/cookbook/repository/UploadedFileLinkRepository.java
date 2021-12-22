package ru.geekbrains.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.cookbook.domain.file.UploadedFileLink;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UploadedFileLinkRepository extends JpaRepository<UploadedFileLink, Long> {
    List<UploadedFileLink> findAllByObjectIdAndObjectType(Long objectId, String objectType);
    List<UploadedFileLink> findAllByObjectIdInAndObjectTypeAndObjectPart(List<Long> ids, String objectType, String objectPart);
    Optional<UploadedFileLink> findByObjectIdAndObjectTypeAndObjectPartAndUploadedFile_Filename(Long objectId, String objectType, String objectPart, String filekey);
}
