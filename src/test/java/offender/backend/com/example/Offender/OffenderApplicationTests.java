package offender.backend.com.example.Offender;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OffenderApplicationTests {

	@Test
	public void contextLoads() {
	}
	@Test
	public void test() throws DocumentException {
		String path  = "C:\\Temp\\test";
		SAXReader reader = new SAXReader();
		File file = new File(path);
		Document document = reader.read(file);

	}

}
