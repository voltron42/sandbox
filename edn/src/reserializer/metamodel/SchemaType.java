package reserializer.metamodel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class SchemaType {
    @XmlAttribute()
    public String name;

    @XmlElement(name="field")
    public List<SchemaField> fields;
}
