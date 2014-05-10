package com.jurassic.grammar.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Signature {
	private SignatureElement returnType;
	private List<SignatureElement> params;
	
	private Signature(SignatureElement returnType, List<SignatureElement> params) {
		this.returnType = returnType;
		this.params = params;
	}
	
	@XmlElement(name="give")
	public SignatureElement getReturnType() {
		return returnType;
	}
	
	public void setReturnType(SignatureElement returnType) {
		this.returnType = returnType;
	}
	
	@XmlElement(name="take")
	public List<SignatureElement> getParams() {
		return params;
	}
	
	public void setParams(List<SignatureElement> params) {
		this.params = params;
	}

}