package br.com.vexillum.vexreports.model;

public class State {

	private Long id;

	private String acronym;

	private String name;

	private boolean active;
	
	public State(Long id, String acronym, String name, boolean active) {
		this.id = id;
		this.acronym = acronym;
		this.name = name;
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
