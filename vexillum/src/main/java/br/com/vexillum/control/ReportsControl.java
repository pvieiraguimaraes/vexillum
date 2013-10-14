package br.com.vexillum.control;

import br.com.vexillum.model.ICommonEntity;

@SuppressWarnings("rawtypes")
public class ReportsControl<E extends ICommonEntity> extends GenericControl {

	@SuppressWarnings("unchecked")
	public ReportsControl(Class classEntity) {
		super(null);
	}

}
