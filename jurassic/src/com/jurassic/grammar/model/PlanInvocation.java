package com.jurassic.grammar.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class PlanInvocation implements ValueDefinition {
	private ValueWrapper scope;
	private ValueWrapper action;
	private List<ValueWrapper> params;
	
	private PlanInvocation(ValueWrapper scope, ValueWrapper action,
			List<ValueWrapper> params) {
		this.scope = scope;
		this.action = action;
		this.params = params;
	}

	@XmlElement(name="to")
	public ValueWrapper getScope() {
		return scope;
	}

	public void setScope(ValueWrapper scope) {
		this.scope = scope;
	}

	@XmlElement(name="do")
	public ValueWrapper getAction() {
		return action;
	}

	public void setAction(ValueWrapper action) {
		this.action = action;
	}

	@XmlElement(name="with")
	public List<ValueWrapper> getParams() {
		return params;
	}

	public void setParams(List<ValueWrapper> params) {
		this.params = params;
	}

}