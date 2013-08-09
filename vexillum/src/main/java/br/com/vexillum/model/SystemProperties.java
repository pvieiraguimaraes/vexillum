package br.com.vexillum.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.vexillum.model.annotations.SearchField;

@SuppressWarnings("serial")
@Entity
@Table(name="systemproperties")
public class SystemProperties extends Property {
	
	@SearchField
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="category", unique=false, nullable=false, updatable=true)
	private SystemPropertiesCategory category;
	
	@Column(name = "value", unique = false, nullable = true, updatable = true, length = 1000)
	private String value;

	public SystemPropertiesCategory getCategory() {
		return category;
	}

	public void setCategory(SystemPropertiesCategory category) {
		this.category = category;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
