package offender.backend.com.example.Offender.entities;

import lombok.Data;
import org.simpleframework.xml.Attribute;

import javax.persistence.*;

@Data
@Entity
public class AttributesVM {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String attributeKey;

    @ManyToOne
    private User creator;

    private String type;
    private String stringValue;
    private Double doubleValue;
    private String listKey;
    private boolean selected;
    public AttributesVM(){}
}
