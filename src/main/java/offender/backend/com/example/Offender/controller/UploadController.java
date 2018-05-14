package offender.backend.com.example.Offender.controller;

//import org.apache.commons.io.IOUtils;
import offender.backend.com.example.Offender.config.CustomUserDetails;
import offender.backend.com.example.Offender.entities.UploadDataRecord;
import offender.backend.com.example.Offender.repository.UploadDataRepository;
import offender.backend.com.example.Offender.service.UploadService;
import offender.backend.com.example.Offender.service.UserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @Autowired
    private UserService userService;

    private static String UPLOADED_FOLDER = "C://temp//";
    @GetMapping(value="/upload")
    public byte[] dataSource(HttpServletResponse response) throws IOException {
        getClass().getResource("entities.xml");
        response.setContentType("application/xml");
        Resource resource = new ClassPathResource("static/entities.xml");
        InputStream in = resource.getInputStream();
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value="api/fetchUpload/{username}")
    public List<UploadDataRecord> uploadDataRecord(@PathVariable String username) throws IOException {
        return uploadService.findByUser(userService.getUser(username));
    }

    @PostMapping("/api/upload")
    // If not @RestController, uncomment this
    //@ResponseBody
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile uploadfile) {

        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {

            saveUploadedFiles(Arrays.asList(uploadfile));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }
    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String fullFolderPath = UPLOADED_FOLDER + userDetails.getUsername() + "/";

        if(!Files.exists(Paths.get(fullFolderPath))){
            Files.createDirectories(Paths.get(fullFolderPath));
        }

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(fullFolderPath + file.getOriginalFilename());
            Files.write(path, bytes);

            UploadDataRecord newRecord = new UploadDataRecord();
            newRecord.setCreator(userService.getUser(userDetails.getUsername()));
            newRecord.setDateCreated(new Date());
            newRecord.setFileSize(file.getSize());
            newRecord.setFileName(file.getOriginalFilename());
            uploadService.insert(newRecord);

        }

    }

}