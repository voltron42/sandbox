package reserializer.metamodel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class SchemaMapKey {

    @XmlElements({
            @XmlElement(type=SchemaString.class, name="string"),
            @XmlElement(type=SchemaNumber.class, name="number"),
            @XmlElement(type=SchemaBoolean.class, name="boolean")
    })
    public SchemaMapKeyType keyType;
}
