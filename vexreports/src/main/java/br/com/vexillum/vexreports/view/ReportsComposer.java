package br.com.vexillum.vexreports.view;

import java.util.List;
import java.util.Map;

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
public abstract class ReportsComposer<E extends ICommonEntity, G extends GenericControl<E>>
		extends CRUDComposer<E, G> {

	private List<E> listReport;

	private String[] listItens;

	private boolean withTemplate;

	private Map<String, String> mapFieldsName;

	private boolean followAnnotation = true;

	public Map<String, String> getMapFieldsName() {
		return mapFieldsName;
	}

	public void setMapFieldsName(Map<String, String> mapFieldsName) {
		this.mapFieldsName = mapFieldsName;
	}

	public boolean getFollowAnnotation() {
		return followAnnotation;
	}

	public void setFollowAnnotation(boolean followAnnotation) {
		this.followAnnotation = followAnnotation;
	}

	public boolean getWithTemplate() {
		return withTemplate;
	}

	public void setWithTemplate(boolean withTemplate) {
		this.withTemplate = withTemplate;
	}

	public String[] getListItens() {
		return listItens;
	}

	public void setListItens(String[] listItens) {
		this.listItens = listItens;
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

	/**
	 * Retorna a classe geradora do relatório.
	 * 
	 * @return
	 */
	public GenericGeneratorReporter getGeneratorReport() {
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
			throw new NullPointerException(msg);
		}

		GenericControl controller = getGeneratorReport(); // Tenta buscar um
															// gerador
		ret.concat(controller.doAction("generateReport"));
		return ret;
	}

}
