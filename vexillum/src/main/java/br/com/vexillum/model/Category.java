package br.com.vexillum.model;

import javax.persistence.Entity;

import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
@Entity
public class Category extends EntityBasic implements GrantedAuthority {

	public Category() {
		super();
	}
	
	public Category(String name) {
		super();
		this.name = name;
	}
	
	public Category(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	//TODO Tratar a categoria como uma permissao do usuario por enquanto
	@Override
	public String getAuthority() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(!(obj instanceof Category)){
			return false;
		}
		Category entity = (Category) obj;
		if(getName() == null || getName().isEmpty()){
			if(!entity.getName().isEmpty()){
				return false;
			}
		} else if(!getName().equals(entity.getName())){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
