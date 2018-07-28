package offender.backend.com.example.Offender.entities;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(strict = false)
@Data
public class IntelRecord {
    @Element(required=false)
    protected String title;
    @ElementList(inline=true, required=false, name="attributes")
    protected ArrayList<Attachments> attachments;
}
