package offender.backend.com.example.Offender.entities;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Data
@Root(strict = false)
public class IntelEntities {
    @Attribute
    protected String keyid;
    @Element(name="name", required=false)
    protected String name;
    @Element(name="uuid", required=false)
    protected String uuid;
}
