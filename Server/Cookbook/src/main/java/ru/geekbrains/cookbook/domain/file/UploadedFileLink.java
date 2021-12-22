package ru.geekbrains.cookbook.domain.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "uploaded_file_link")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadedFileLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "object_type")
    private String objectType;

    @Column(name = "object_part")
    private String objectPart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uploaded_file_id", nullable = false)
    private UploadedFile uploadedFile;

    @Column(name = "description")
    private String description;
}
