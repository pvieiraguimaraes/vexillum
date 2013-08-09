package br.com.vexillum.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import br.com.vexillum.model.annotations.SearchField;

@SuppressWarnings("serial")
@MappedSuperclass
public class Property extends CommonEntity {

	@SearchField
	@Column(name = "property", unique = true, nullable = false, updatable = false, length = 50)
	private String property;
	
	@Column(name = "name", unique = true, nullable = false, updatable = true, length = 50)
	private String name;
	
	@Column(name = "description", unique = false, nullable = true, updatable = true, length = 200)
	private String description;
	
	@SearchField
	@Column(name = "type", unique = false, nullable = false, updatable = true)
	private String type;
	
	@Column(name = "default_value", unique = false, nullable = false, updatable = true, length = 1000)
	private String defaultValue;	
	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
