package com.jurassic.grammar.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class PlanDefinition implements ValueDefinition{
	private List<TypeDefinition> types;
	private Signature signature;
	private PlanBody body;
	
	private PlanDefinition(List<TypeDefinition> types, Signature signature,
			PlanBody body) {
		this.types = types;
		this.signature = signature;
		this.body = body;
	}

	@XmlElement(name="of")
	public List<TypeDefinition> getTypes() {
		return types;
	}

	public void setTypes(List<TypeDefinition> types) {
		this.types = types;
	}

	@XmlElement(name="parts")
	public Signature getSignature() {
		return signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	@XmlElements({
		@XmlElement(name="parts", type=PlanBodyImpl.class),
		@XmlElement(name="native", type=NativePlanBody.class),
	})
	public PlanBody getBody() {
		return body;
	}

	public void setBody(PlanBody body) {
		this.body = body;
	}

}
