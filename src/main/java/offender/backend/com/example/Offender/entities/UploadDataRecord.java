package offender.backend.com.example.Offender.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import offender.backend.com.example.Offender.FileType;

import javax.persistence.*;
import java.util.Date;

@Entity
public class UploadDataRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileName;
    private Date dateCreated;
    private Long fileSize;
    private FileType fileType;
    private String fileDisplayUrl;

    @JsonIgnore
    private String filePath;

    @ManyToOne
    private User creator;

    public UploadDataRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getFilePath() {
        return fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileDisplayUrl() {
        return fileDisplayUrl;
    }

    public void setFileDisplayUrl(String fileDisplayUrl) {
        this.fileDisplayUrl = fileDisplayUrl;
    }

    public FileType getFileType(){
        return fileType;
    }

    public void setFileType(FileType fileType){this.fileType = fileType;}

}
