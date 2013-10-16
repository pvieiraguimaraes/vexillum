package br.com.vexillum.vexreports.view;

import java.security.InvalidParameterException;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.Return;
import br.com.vexillum.vexreports.control.GenericGeneratorReporter;
import br.com.vexillum.view.CRUDComposer;

@SuppressWarnings({ "rawtypes", "serial" })
@org.springframework.stereotype.Component
@Scope("prototype")
public class ReportsComposer<E extends ICommonEntity, G extends GenericControl<E>>
		extends CRUDComposer<E, G> {

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

	public GenericGeneratorReporter getGenerator(){
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
		List thisListEntity = getListEntity();
		if (thisListEntity != null || !thisListEntity.isEmpty())
			listReport = thisListEntity;
		else if (list != null || !list.isEmpty())
			listReport = list;
		else if (thisListEntity == null && list == null) {
			ret.setValid(false);
			String msg = "A lista do parâmetro ou o listEntity não podem ser nulos";
			throw new InvalidParameterException(msg);
		}
		
		GenericControl controller = getGenerator(); // Tenta buscar um gerador
		ret.concat(controller.doAction("generateReport"));
		return ret;
	}

	@Override
	public G getControl() {
		return null;
	}

	@Override
	public E getEntityObject() {
		return null;
	}

}
