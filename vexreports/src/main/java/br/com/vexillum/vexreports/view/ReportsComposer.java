package br.com.vexillum.vexreports.view;

import java.util.List;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.vexreports.control.ReportsControl;
import br.com.vexillum.view.CRUDComposer;

@SuppressWarnings({ "rawtypes", "serial" })
public class ReportsComposer<E extends ICommonEntity> extends CRUDComposer {

	private List<E> listReport;

	@Override
	public GenericControl getControl() {
		ReportsControl controller;
		controller = SpringFactory.getController("reportsControl",
				ReportsControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
		return controller;
	}

	@Override
	public ICommonEntity getEntityObject() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public Return generateReport() {
		Return ret = new Return(true);
		if (getListEntity() == null || getListEntity().isEmpty())
			ret.setValid(false);
		else
			listReport = getListEntity();
		ret.concat(getControl().doAction("generateReport"));
		return ret;
	}

	public void generateReport(List list) {

	}

}
