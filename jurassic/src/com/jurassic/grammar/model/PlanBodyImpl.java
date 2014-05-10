package com.jurassic.grammar.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class PlanBodyImpl {
	private List<PlanInvocation> actions;
	private ValueWrapper returnValue;
	
	public PlanBodyImpl(List<PlanInvocation> actions, ValueWrapper returnValue) {
		super();
		this.actions = actions;
		this.returnValue = returnValue;
	}
	
	@XmlElement(name="act")
	public List<PlanInvocation> getActions() {
		return actions;
	}
	
	public void setActions(List<PlanInvocation> actions) {
		this.actions = actions;
	}
	
	@XmlElement(name="return")
	public ValueWrapper getReturnValue() {
		return returnValue;
	}
	
	public void setReturnValue(ValueWrapper returnValue) {
		this.returnValue = returnValue;
	}
}