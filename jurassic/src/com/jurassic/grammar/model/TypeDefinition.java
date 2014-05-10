package com.jurassic.grammar.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class TypeDefinition {
	private String type;
	private List<TypeDefinition> genericTypes;
	private List<TypeDefinition> extendedTypes;
	
	public TypeDefinition(String type, List<TypeDefinition> genericTypes,
			List<TypeDefinition> extendedTypes) {
		this.setType(type);
		this.genericTypes = genericTypes;
		this.extendedTypes = extendedTypes;
	}

	@XmlAttribute(name="type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name="of")
	public List<TypeDefinition> getGenericTypes() {
		return genericTypes;
	}

	public void setGenericTypes(List<TypeDefinition> genericTypes) {
		this.genericTypes = genericTypes;
	}

	@XmlElementWrapper(name="is")
	@XmlElement(name="of")
	public List<TypeDefinition> getExtendedTypes() {
		return extendedTypes;
	}

	public void setExtendedTypes(List<TypeDefinition> extendedTypes) {
		this.extendedTypes = extendedTypes;
	}
}