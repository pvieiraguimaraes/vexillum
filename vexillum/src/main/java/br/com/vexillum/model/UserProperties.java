package br.com.vexillum.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.vexillum.model.annotations.SearchField;

@SuppressWarnings("serial")
@Entity
@Table(name="userproperties")
public class UserProperties extends Property {
	
	@SearchField
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="category", unique=false, nullable=false, updatable=true)
	private UserPropertiesCategory category;

	public UserPropertiesCategory getCategory() {
		return category;
	}

	public void setCategory(UserPropertiesCategory category) {
		this.category = category;
	}
}
