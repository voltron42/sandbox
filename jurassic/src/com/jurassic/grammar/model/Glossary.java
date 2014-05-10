package com.jurassic.grammar.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Glossary {
	private List<Tag> tags;
	private PlanDefinition plan;
	private List<Thing> things;
	
	public Glossary(List<Tag> tags, PlanDefinition plan, List<Thing> things) {
		this.tags = tags;
		this.plan = plan;
		this.things = things;
	}

	@XmlElement(name="tag")
	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public PlanDefinition getPlan() {
		return plan;
	}

	public void setPlan(PlanDefinition plan) {
		this.plan = plan;
	}

	@XmlElement(name="thing")
	public List<Thing> getThings() {
		return things;
	}

	public void setThings(List<Thing> things) {
		this.things = things;
	}
}
