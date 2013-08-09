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
@Table(name="configuration")
public class Configuration extends CommonEntity {

	@SearchField
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_user", unique=false, nullable=false, updatable=true)
	protected UserBasic user;

	@SearchField
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_property", unique=false, nullable=false, updatable=true)
	private UserProperties property;

	@Column(name = "value", unique = false, nullable = false, updatable = true, length = 1000)
	private String value;

	public Configuration() {
	}

	public UserBasic getUser(){
		return user;
	}

	public void setUser(UserBasic user){
		this.user = user;
	}

	public UserProperties getProperty() {
		return property;
	}

	public void setProperty(UserProperties property) {
		this.property = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
