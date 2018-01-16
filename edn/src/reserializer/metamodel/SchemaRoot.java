package reserializer.metamodel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class SchemaRoot {

    @XmlElements({
            @XmlElement(type=SchemaArray.class, name="array"),
            @XmlElement(type=SchemaMap.class, name="map"),
            @XmlElement(type=SchemaObject.class, name="object")
    })
    public SchemaRootType rootType;
}
