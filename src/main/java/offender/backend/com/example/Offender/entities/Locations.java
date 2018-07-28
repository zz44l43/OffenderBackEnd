package offender.backend.com.example.Offender.entities;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Data
@Root(strict = false)
public class Locations {
    @Attribute
    protected String id;
    @Attribute
    protected String recordKey;
}
