package offender.backend.com.example.Offender.entities;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(strict = false)
@Namespace(reference="http://www.example.org/Intelligence")
@lombok.Data
public class Data {
    @ElementList(inline=true)
    private ArrayList<Entities> entitiesList;
}
