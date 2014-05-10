package com.jurassic.grammar.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Thing {
	private TypeDefinition typeDef;
	private List<EnumElement> members;
	private List<TypeMember> parts;
	
	public Thing(TypeDefinition typeDef, List<EnumElement> members,
			List<TypeMember> parts) {
		this.typeDef = typeDef;
		this.members = members;
		this.parts = parts;
	}

	@XmlElement(name="of")
	public TypeDefinition getTypeDef() {
		return typeDef;
	}

	public void setTypeDef(TypeDefinition typeDef) {
		this.typeDef = typeDef;
	}

	@XmlElementWrapper(name="series")
	@XmlElement(name="member")
	public List<EnumElement> getMembers() {
		return members;
	}

	public void setMembers(List<EnumElement> members) {
		this.members = members;
	}

	@XmlElement(name="has")
	public List<TypeMember> getParts() {
		return parts;
	}

	public void setParts(List<TypeMember> parts) {
		this.parts = parts;
	}
	
	
}