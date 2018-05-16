package offender.backend.com.example.Offender.config;

import offender.backend.com.example.Offender.service.UtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    UtilsService utilsService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = utilsService.getBaseFolderPath();
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + path)
                .setCachePeriod(0);
    }
}
