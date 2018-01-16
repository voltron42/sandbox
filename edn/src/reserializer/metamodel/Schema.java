package reserializer.metamodel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Schema {
    public SchemaRoot root;

    @XmlElement(name="type")
    public List<SchemaType> types;
}
