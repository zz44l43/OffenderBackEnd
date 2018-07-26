package offender.backend.com.example.Offender.entities;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import lombok.Data;

@Data
@Root(strict = false)
public class Attachments{
    @Attribute
    protected String filename;
    @Attribute
    protected Boolean isPrimary;
}
