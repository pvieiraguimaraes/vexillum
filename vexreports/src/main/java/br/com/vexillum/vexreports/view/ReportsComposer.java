package br.com.vexillum.vexreports.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Filedownload;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.vexreports.control.GenericGeneratorReporter;
import br.com.vexillum.view.CRUDComposer;

@SuppressWarnings({ "rawtypes", "serial" })
@org.springframework.stereotype.Component
@Scope("prototype")
public abstract class ReportsComposer<E extends ICommonEntity, G extends GenericControl<E>>
		extends CRUDComposer<E, G> {

	private List<?> listReport;

	private String[] listItens;

	private boolean withTemplate;

	private boolean withHeader = true;

	private boolean withFooter = true;

	private boolean withTitle = true;

	private Map<String, String> mapFieldsName;

	private boolean followAnnotation = true;

	private Map params;

	private String titleReport;

	private String pathTemplate;

	private ByteArrayOutputStream outputStream;
	
	private String nameReport;
	
	public ByteArrayOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(ByteArrayOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public String getNameReport() {
		return nameReport;
	}

	public void setNameReport(String nameReport) {
		this.nameReport = nameReport;
	}

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

	public List<?> getListReport() {
		return listReport;
	}

	public void setListReport(List<?> listReport) {
		this.listReport = listReport;
	}

	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	public boolean getWithHeader() {
		return withHeader;
	}

	public void setWithHeader(boolean withHeader) {
		this.withHeader = withHeader;
	}

	public boolean getWithFooter() {
		return withFooter;
	}

	public void setWithFooter(boolean withFooter) {
		this.withFooter = withFooter;
	}

	public boolean getWithTitle() {
		return withTitle;
	}

	public void setWithTitle(boolean withTitle) {
		this.withTitle = withTitle;
	}

	public String getTitleReport() {
		return titleReport;
	}

	public void setTitleReport(String titleReport) {
		this.titleReport = titleReport;
	}

	public String getPathTemplate() {
		return pathTemplate;
	}

	public void setPathTemplate(String pathTemplate) {
		this.pathTemplate = pathTemplate;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if (outputStream == null)
			outputStream = new ByteArrayOutputStream();
	}

	/**
	 * Retorna a classe geradora do relatório.
	 * 
	 * @return
	 */
	public GenericGeneratorReporter getGeneratorReport() {
		return SpringFactory.getController("genericGeneratorReporter",
				GenericGeneratorReporter.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	public Return generateListEntityReport() {
		Return ret = new Return(true);
		ret.concat(generateReport(getListEntity()));
		return ret;
	}

	public Return generateReport() {
		Return ret = new Return(true);

		if (params != null || !params.isEmpty()){
			setOutputStream(getGeneratorReport().generateReport());
			showReport();
		}
		else {
			ret.setValid(false);
			String msg = "O Map params não pode ser nulo";
			throw new NullPointerException(msg);
		}
		return ret;
	}

	@SuppressWarnings({  "null" })
	public Return generateReport(List list) {
		Return ret = new Return(true);

		if (list != null || !list.isEmpty())
			setListReport(list);
		else {
			ret.setValid(false);
			String msg = "A lista não pode ser nula";
			throw new NullPointerException(msg);
		}

		setOutputStream(getGeneratorReport().generateReport());
		showReport();
		return ret;
	}

	public void showReport() {
		Filedownload.save(new ByteArrayInputStream(outputStream.toByteArray()),
				"application/pdf", getNameReport());

	}
}
