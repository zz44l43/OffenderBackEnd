package offender.backend.com.example.Offender;

import offender.backend.com.example.Offender.entities.Data;
import offender.backend.com.example.Offender.entities.IntelAttributes;
import offender.backend.com.example.Offender.entities.IntelRecord;
import offender.backend.com.example.Offender.entities.RecordAttribute;
import offender.backend.com.example.Offender.service.DataSourceService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.CartesianDistCalc;
import org.locationtech.spatial4j.distance.DistanceCalculator;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.shape.*;
import org.locationtech.spatial4j.shape.impl.CircleImpl;
import org.locationtech.spatial4j.shape.impl.GeoCircle;
import org.locationtech.spatial4j.shape.impl.PointImpl;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OffenderApplicationTests {

	@Test
	public void contextLoads() {
	}
//	@Test
//	public void test() throws DocumentException {
//		String input = getClass().getClassLoader().getResource("static/recordAttribute.xml").getPath();
//		Serializer serializer = new Persister();
//		File source = new File(input);
//		assertEquals(true, source.exists());
//		try {
////			IntelRecord record = serializer.read(IntelRecord.class, source);
////			assertEquals("MP_20170923_03_Foraging",record.getTitle());
//			RecordAttribute recordAttribute = serializer.read(RecordAttribute.class, source);
//			assertEquals("gpslocation1",recordAttribute.getName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Test
//	public void testAttribute() throws DocumentException {
//		String input = getClass().getClassLoader().getResource("static/attributes.xml").getPath();
//		Serializer serializer = new Persister();
//		File source = new File(input);
//		assertEquals(true, source.exists());
//		try {
////			IntelRecord record = serializer.read(IntelRecord.class, source);
////			assertEquals("MP_20170923_03_Foraging",record.getTitle());
//			IntelAttributes attributes = serializer.read(IntelAttributes.class, source);
//			assertEquals("gpslocation1",attributes.getRecordAttribute().getName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//	@Test
//	public void testIntelRecord() throws DocumentException {
//		String input = getClass().getClassLoader().getResource("static/record.xml").getPath();
//		Serializer serializer = new Persister();
//		File source = new File(input);
//		assertEquals(true, source.exists());
//		try {
//			IntelRecord record = serializer.read(IntelRecord.class, source);
//			assertEquals("MP_20170923_03_Foraging",record.getTitle());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Test
//	public void testEntities() throws DocumentException {
//		String input = getClass().getClassLoader().getResource("static/entities.xml").getPath();
//		Serializer serializer = new Persister();
//		File source = new File(input);
//		assertEquals(true, source.exists());
//		try {
//			Data record = serializer.read(Data.class, source);
//			assertEquals("URN",record.getEntitiesList().get(0).getList().get(0).getLabel());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Test
//	public void testEntitiesAndRecords() throws DocumentException {
//		String input = getClass().getClassLoader().getResource("static/entities.xml").getPath();
//		Serializer serializer = new Persister();
//		File data = new File(input);
//		File record = new File(getClass().getClassLoader().getResource("static/record.xml").getPath());
//		assertEquals(true, data.exists());
//		assertEquals(true, record.exists());
//		try {
//			Data entities = serializer.read(Data.class, data);
//			IntelRecord recordSingle = serializer.read(IntelRecord.class, record);
//			ArrayList<IntelRecord> records = new ArrayList<IntelRecord>();
//			records.add(recordSingle);
//
//			DataSourceService dataSourceService = new DataSourceService();
//			entities = dataSourceService.mergeEntitiesAndRecords(entities,records);
//
//			assertEquals("URN",entities.getEntitiesList().get(0).getList().get(0).getLabel());
//			assertEquals("Intercept #01",entities.getEntitiesList().get(4).getLocations().get(0).getId());
//			assertEquals(103.35471041746172,entities.getEntitiesList().get(4).getRecordLocations().get(0).getLocationAttribute().getNumberValue1(),0.01);
//			assertEquals(3.050261916758624,entities.getEntitiesList().get(4).getRecordLocations().get(0).getLocationAttribute().getNumberValue2(),0.01);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Test
//	public void testGeo() throws DocumentException {
//		SpatialContext ctx = SpatialContext.GEO;
//		Double degree = DistanceUtils.dist2Degrees(6.76,DistanceUtils.EARTH_EQUATORIAL_RADIUS_KM);
//		PointImpl pp = new PointImpl(103.35471041746172,3.050261916758624,ctx);
//		CircleImpl cc = new CircleImpl(pp, degree,ctx);
//		CartesianDistCalc dc = new CartesianDistCalc();
//		boolean isWhitn = dc.within(pp,103.306411,3.013046,degree);
//		boolean iswhitn2 = cc.contains(103.306411,3.013046);
//		assertEquals(isWhitn,iswhitn2);
//		cc.getArea(ctx);
//
//
//
//	}
}
