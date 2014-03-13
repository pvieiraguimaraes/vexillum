package br.com.vexillum.vexhierarchia.model;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import br.com.vexillum.model.CommonEntityActivated;
import br.com.vexillum.model.annotations.NotInitialize;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class HierarchyEntity<E extends HierarchyEntity<?>>  extends CommonEntityActivated{

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "pattern_id", insertable = true, updatable = true, nullable=true)
	private E pattern; 
	
	@NotInitialize
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pattern")	
	private List<E> childrens;
	
	public abstract String getStyleClass();
	
	public abstract String getInactiveStyleClass();

	public abstract Integer getLevel();
	
	public E getPattern() {
		return pattern;
	}

	public List<E> getChildrens() {
		return childrens;
	}

	public void setPattern(E pattern) {
		this.pattern = pattern;
	}

	public void setChildrens(List<E> childrens) {
		this.childrens = childrens;
	}
}
