package offender.backend.com.example.Offender.controller;

//import org.apache.commons.io.IOUtils;
import offender.backend.com.example.Offender.FileType;
import offender.backend.com.example.Offender.config.CustomUserDetails;
import offender.backend.com.example.Offender.entities.*;
import offender.backend.com.example.Offender.pojos.UserRegistration;
import offender.backend.com.example.Offender.repository.UploadDataRepository;
import offender.backend.com.example.Offender.service.DataSourceService;
import offender.backend.com.example.Offender.service.UploadService;
import offender.backend.com.example.Offender.service.UserService;
import offender.backend.com.example.Offender.service.UtilsService;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @Autowired
    private UserService userService;


    @Autowired
    private UtilsService utilsService;

    @Autowired
    private DataSourceService dataSourceService;


    @GetMapping(value = "/upload")
    public byte[] dataSource(HttpServletResponse response) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        List<UploadDataRecord> records =  uploadService.findByUser(userService.getUser(username));
        UploadDataRecord record =  records.get(records.size()- 1);
        String fullFolderPath = utilsService.getBaseFolderPath()  + username + "/"+record.getFileName();
        File file = new File(fullFolderPath);
        ArrayList<AttributesVM> attributesVMList = uploadService.findAttributesByUser((userService.getUser(username)));
        Data data = dataSourceService.getFilteredPeopleSource(file,attributesVMList);
        File newFile = dataSourceService.createXML(data,utilsService.getBaseFolderPath()  + username + "/");
        FileInputStream fileInputStream = new FileInputStream(newFile);
        return IOUtils.toByteArray(fileInputStream);

    }

    @GetMapping(value = "/image/{imageName:.+}")
    public byte[] getImage(HttpServletResponse response, @PathVariable(value = "imageName") String imageName) throws IOException {
        String fullFolderPath = utilsService.getBaseFolderPath()  + "admin/images/";
        File file = new File(fullFolderPath + imageName);
        return Files.readAllBytes(file.toPath());

    }

    @GetMapping(value = "/images/{username}")
    public void imageSource(HttpServletResponse response, @PathVariable String username) throws ServletException, IOException {
//        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String fullFolderPath = utilsService.getBaseFolderPath()  + username + "/images/";
        System.out.println(fullFolderPath);
        File directory = new File(fullFolderPath);
        if(!directory.exists())
            throw new IOException();

        response.setContentType("application/zip");
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"test.zip\"");
        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());




        File[] files = directory.listFiles();
        ServletOutputStream out = response.getOutputStream();
        for (File file : files) {

            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);

            IOUtils.copy(fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();

    }

    @GetMapping(value = "/api/fetchImages/{username}")
    public List<UploadDataRecord> fetchImagesRecoreds(@PathVariable String username) throws IOException {
        String fullFolderPath = utilsService.getBaseFolderPath()  + username + "/images/";
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
            fullFolderPath = utilsService.getBaseFolderPath() + userDetails.getUsername() + "/";
        } else {
            fullFolderPath = utilsService.getBaseFolderPath() + userDetails.getUsername() + "/images/";
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
            if(!isImage){
                File newFile = new File(fullFolderPath + file.getOriginalFilename());
                Data data = dataSourceService.getPeopleSource(newFile);
                ArrayList<AttributesVM> attributesVMList =  dataSourceService.getUniqueKeyAttributesVM(data,userService.getUser(userDetails.getUsername()));
                for (AttributesVM att: attributesVMList
                     ) {
                    uploadService.saveOrUpdateAttributes(att, userService.getUser(userDetails.getUsername()));
                }

            }

        }

    }

    @GetMapping(value = "/api/attributes/{username}")
    public List<AttributesVM> attributes(@PathVariable String username) throws IOException {
        User user = userService.getUser(username);
        return uploadService.findAttributesByUser(user);
    }

    @PostMapping(value = "/api/attributes")
    public String saveDesign(@RequestBody List<AttributesVM> attributesVMList){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        if(user == null)
            return "Error username does not exist";
        for (AttributesVM attributeVM: attributesVMList
             ) {
            uploadService.saveOrUpdateAttributes(attributeVM,user);
        }
        return "Saved";
    }


}