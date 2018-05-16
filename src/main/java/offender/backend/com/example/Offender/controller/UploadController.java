package offender.backend.com.example.Offender.controller;

//import org.apache.commons.io.IOUtils;
import offender.backend.com.example.Offender.FileType;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @Autowired
    private UserService userService;

    private static String UPLOADED_FOLDER = "C://Temp//";

    @GetMapping(value = "/upload")
    public byte[] dataSource(HttpServletResponse response) throws IOException {
        getClass().getResource("entities.xml");
        response.setContentType("application/xml");
        Resource resource = new ClassPathResource("static/entities.xml");
        InputStream in = resource.getInputStream();
        return IOUtils.toByteArray(in);

    }

    @GetMapping(value = "/image/{imageName:.+}")
    public byte[] getImage(HttpServletResponse response, @PathVariable(value = "imageName") String imageName) throws IOException {
        String fullFolderPath = UPLOADED_FOLDER  + "admin/images/";
        File file = new File(fullFolderPath + imageName);
        return Files.readAllBytes(file.toPath());

    }

    @GetMapping(value = "/images/{username}")
    public void imageSource(HttpServletResponse response, @PathVariable String username) throws ServletException, IOException {
//        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String fullFolderPath = UPLOADED_FOLDER  + username + "/images/";
        File directory = new File(fullFolderPath);
        if(!directory.exists())
            throw new IOException();

        response.setContentType("multipart/x-mixed-replace;boundary=END");

        File[] files = directory.listFiles();
        ServletOutputStream out = response.getOutputStream();
        for (File file : files) {

            // Get the file
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);

            } catch (FileNotFoundException fnfe) {
                // If the file does not exists, continue with the next file
                System.out.println("Couldfind file " + file.getAbsolutePath());
                continue;
            }

            BufferedInputStream fif = new BufferedInputStream(fis);

            // Print the content type
            out.println("Content-Disposition: attachment; filename=" + file.getName());
            out.println();

            System.out.println("Sending " + file.getName());

            // Write the contents of the file
            int data = 0;
            while ((data = fif.read()) != -1) {
                out.write(data);
            }
            fif.close();

            // Print the boundary string
            out.println();
            out.println("--END");
            out.flush();
            System.out.println("Finisheding file " + file.getName());
        }
        out.flush();
        out.close();

    }

    @GetMapping(value = "/api/fetchImages/{username}")
    public List<UploadDataRecord> fetchImagesRecoreds(@PathVariable String username) throws IOException {
        String fullFolderPath = UPLOADED_FOLDER  + username + "/images/";
        File directory = new File(fullFolderPath);
        List<UploadDataRecord> records = new ArrayList<>();
        if(!directory.exists())
            return records;
        File[] files = directory.listFiles();
        for (File file: files) {
            UploadDataRecord newRecord = new UploadDataRecord();
            newRecord.setCreator(userService.getUser(username));
            newRecord.setFileName(file.getName());
            newRecord.setFileType(FileType.IMAGE);
            newRecord.setFileSize(file.length());
            newRecord.setFileDisplayUrl("images/" + username + "/images/" + file.getName());
            records.add(newRecord);
        }
        return records;
    }

    @GetMapping(value = "api/fetchUpload/{username}")
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

            saveUploadedFiles(Arrays.asList(uploadfile), false, true);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }

    // 3.1.2 Multiple file upload
    @PostMapping("/api/upload/multi")
    public ResponseEntity<?> uploadFileMulti(
            @RequestParam("files") MultipartFile[] uploadfiles) {

        // Get file name
        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {

            saveUploadedFiles(Arrays.asList(uploadfiles), true, false);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - "
                + uploadedFileName, HttpStatus.OK);

    }

    private void saveUploadedFiles(List<MultipartFile> files, Boolean isImage, Boolean keepRecords) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String fullFolderPath;
        if (!isImage) {
            fullFolderPath = UPLOADED_FOLDER + userDetails.getUsername() + "/";
        } else {
            fullFolderPath = UPLOADED_FOLDER + userDetails.getUsername() + "/images/";
        }

        if (!Files.exists(Paths.get(fullFolderPath))) {
            Files.createDirectories(Paths.get(fullFolderPath));
        }

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(fullFolderPath + file.getOriginalFilename());
            Files.write(path, bytes);
            if(!keepRecords)
            {
                continue;
            }
            UploadDataRecord newRecord = new UploadDataRecord();
            newRecord.setCreator(userService.getUser(userDetails.getUsername()));
            newRecord.setDateCreated(new Date());
            newRecord.setFileSize(file.getSize());
            newRecord.setFileName(file.getOriginalFilename());
            if (isImage) {
                newRecord.setFileType(FileType.IMAGE);
            } else {
                newRecord.setFileType(FileType.XML);
            }

            uploadService.insert(newRecord);

        }

    }

}