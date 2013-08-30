package br.com.vexillum.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@SuppressWarnings({ "serial" })
@MappedSuperclass
public class CommonEntityActivated extends CommonEntity implements IActivatedEntity {

	@Column(name = "active", nullable = false)
	private Boolean active;
	
	@Override
	public Boolean getActive() {
		return this.active;
	}

	@Override
	public void setActive(Boolean active) {
		this.active = active;
	}

}
