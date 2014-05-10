package com.jurassic.grammar.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class TypeMember {
	private String name;
	private UsageEnum use;
	private AccessEnum access;
	private Changable change;
	private TypeDefinition type;
	private ValueDefinition value;

	public TypeMember(String name, UsageEnum use, AccessEnum access, Changable change, TypeDefinition type, ValueDefinition value) {
		super();
		this.setName(name);
		this.setUse(use);
		this.setAccess(access);
		this.setChange(change);
		this.type = type;
		this.value = value;
	}

	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name="use")
	public UsageEnum getUse() {
		return use;
	}

	public void setUse(UsageEnum use) {
		this.use = use;
	}

	@XmlAttribute(name="access")
	public AccessEnum getAccess() {
		return access;
	}

	public void setAccess(AccessEnum access) {
		this.access = access;
	}

	@XmlAttribute(name="change")
	public Changable getChange() {
		return change;
	}

	public void setChange(Changable change) {
		this.change = change;
	}
	
	@XmlElement(name="of")
	public TypeDefinition getType() {
		return type;
	}
	
	public void setType(TypeDefinition type) {
		this.type = type;
	}
	
	@XmlElements({
		@XmlElement(name="simple", type=SimpleValue.class),
		@XmlElement(name="act", type=PlanInvocation.class),
		@XmlElement(name="plan", type=PlanDefinition.class)
	})
	public ValueDefinition getValue() {
		return value;
	}
	
	public void setValue(ValueDefinition value) {
		this.value = value;
	}
}