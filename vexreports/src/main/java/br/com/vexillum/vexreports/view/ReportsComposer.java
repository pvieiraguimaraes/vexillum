package br.com.vexillum.vexreports.view;

import java.security.InvalidParameterException;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.vexreports.control.ReportsControl;
import br.com.vexillum.view.CRUDComposer;

@SuppressWarnings({ "rawtypes", "serial" })
@org.springframework.stereotype.Component
@Scope("prototype")
public class ReportsComposer<E extends CommonEntity> extends CRUDComposer {

	private List<E> listReport;

	private String[] listItens;

	private String[] orderItens;

	public String[] getListItens() {
		return listItens;
	}

	public void setListItens(String[] listItens) {
		this.listItens = listItens;
	}

	public String[] getOrderItens() {
		return orderItens;
	}

	public void setOrderItens(String[] orderItens) {
		this.orderItens = orderItens;
	}

	public List<E> getListReport() {
		return listReport;
	}

	public void setListReport(List<E> listReport) {
		this.listReport = listReport;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		loadBinder();
	}

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

	public Return generateReport() {
		Return ret = new Return(true);
		ret.concat(generateReport(getListEntity()));
		return ret;
	}

	@SuppressWarnings({ "unchecked", "null", "unused" })
	public Return generateReport(List list) {
		Return ret = new Return(true);
		if (getListEntity() != null || !getListEntity().isEmpty())
			listReport = getListEntity();
		else if (list != null || !list.isEmpty())
			listEntity = list;
		else if (getListEntity() == null && list == null) {
			ret.setValid(false);
			String msg = "A lista do parâmetro ou o listEntity não podem ser nulos";
			throw new InvalidParameterException(msg);
		}
		ret.concat(getControl().doAction("generateReport"));
		return ret;
	}

}
