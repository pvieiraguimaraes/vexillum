package br.com.vexillum.model;

import java.io.Serializable;

public interface ICommonEntity extends Serializable {

	public Long getId();
	public void setId(Long id);
	public ICommonEntity cloneEntity();
	
}
