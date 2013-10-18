package br.com.vexillum.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import br.com.vexillum.model.annotations.SearchField;
import br.com.vexillum.util.CloneUtils;
import br.com.vexillum.util.HibernateUtils;

/**
 * @author Pedro Henrique, Entidade master todas as classes
 * de negócio herdam dela
 *
 */
@SuppressWarnings({ "serial" })
@MappedSuperclass
public abstract class CommonEntity implements ICommonEntity {

	@SearchField
	@Id
	@GenericGenerator(name="gen",strategy="increment")
	@GeneratedValue(generator="gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 15, scale = 0)
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public ICommonEntity cloneEntity(){
		return (ICommonEntity) CloneUtils.cloneSerializable(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj){
		obj = HibernateUtils.materializeProxy(obj);
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(this.getClass() != obj.getClass())
			return false;
		CommonEntity entity = (CommonEntity) obj;
		if(getId() == null || getId() == 0){
			if(entity.getId() == null || entity.getId() != 0){
				return false;
			}
		} else if(getId() != entity.getId()){
			return false;
		}
		return true;
	}
	
	public boolean isNew(){
        return this.getId()==null || this.getId().intValue()==0;
	}
	
}
