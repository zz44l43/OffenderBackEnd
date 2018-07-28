package offender.backend.com.example.Offender.entities;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Data
@Root(strict = false)
public class RecordAttribute {
    @Element(name="name", required=false)
    protected String name;
    @Element(name="uuid", required=false)
    protected String uuid;
}
