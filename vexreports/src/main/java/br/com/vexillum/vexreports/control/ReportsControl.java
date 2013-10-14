package br.com.vexillum.vexreports.control;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.ICommonEntity;

@SuppressWarnings("rawtypes")
public class ReportsControl<E extends ICommonEntity> extends GenericControl {

	@SuppressWarnings("unchecked")
	public ReportsControl(Class classEntity) {
		super(null);
	}

}
