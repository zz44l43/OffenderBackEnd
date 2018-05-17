package offender.backend.com.example.Offender.service;

import org.springframework.stereotype.Service;

@Service
public class UtilsService {
    public String getBaseFolderPath(){
        if(System.getProperty("os.name").startsWith("Windows")){
            return "C:\\Temp\\";
        }else {
            return "/Users/es/Desktop/images/";
        }
//        return "/";
    }
}
