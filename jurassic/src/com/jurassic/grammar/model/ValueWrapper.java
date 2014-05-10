package com.jurassic.grammar.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class ValueWrapper {
	private ValueDefinition value;

	private ValueWrapper(ValueDefinition value) {
		super();
		this.value = value;
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
