package br.com.vexillum.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import br.com.vexillum.model.annotations.SearchField;
import br.com.vexillum.model.annotations.Validate;

/**
 * @author Pedro Henrique, Entidade básica para os casos de uso
 * das tabelas básicas
 *
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class EntityBasic extends CommonEntity {

	@SearchField
	@Validate(notNull=true, min=3, max=200)
	@Column(name="name", nullable=false, updatable=true, unique=true, length=200)
	protected String name;
	
	@Validate(max=2000)
	@Column(name="description", nullable=true, updatable=true, unique=false, length=2000)
	protected String description;

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

}
