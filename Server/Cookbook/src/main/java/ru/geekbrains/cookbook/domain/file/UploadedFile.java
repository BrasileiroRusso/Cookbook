package ru.geekbrains.cookbook.domain.file;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.cookbook.auth.User;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "uploaded_file")
@Data
@NoArgsConstructor
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "filename", nullable = false)
    private String filename;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "upload_time")
    private Date uploadTime;
}
