package offender.backend.com.example.Offender.controller;

//import org.apache.commons.io.IOUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class UploadController {
    @GetMapping(value="/upload")
    public byte[] dataSource(HttpServletResponse response) throws IOException {
        getClass().getResource("entities.xml");
        response.setContentType("application/xml");
        Resource resource = new ClassPathResource("static/entities.xml");
        InputStream in = resource.getInputStream();
        return IOUtils.toByteArray(in);
    }
}