package br.com.vexillum.vexreports.view;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.control.manager.ExceptionManager;
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

	private boolean withHeader = true;

	private boolean withFooter = true;
	
	private boolean withTitle = true;

	private Map<String, String> mapFieldsName;

	private boolean followAnnotation = true;

	private boolean concatenatedReports = false; //TODO Não está sendo usado por enquanto

	private Map params;

	private ServletOutputStream outputStream;
	
	private String titleReport;

	public ServletOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(ServletOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public boolean getConcatenatedReports() {
		return concatenatedReports;
	}

	public void setConcatenatedReports(boolean concatenatedReports) {
		this.concatenatedReports = concatenatedReports;
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

	public List<E> getListReport() {
		return listReport;
	}

	public void setListReport(List<E> listReport) {
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

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// getOutputStreamZK();
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

		// Component comp = Executions.createComponents("/template/report.zul",
		// null, null);
		// ((Iframe)comp).setContent((Media) getOutputStream());

		return ret;
	}

	// TODO Não será tão fácil assim mandar para a visão
	private void getOutputStreamZK() {
		try {
			ServletOutputStream outputStream = ((HttpServletResponse) Executions
					.getCurrent().getNativeResponse()).getOutputStream();
			setOutputStream(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
			new ExceptionManager(e).treatException();
		}
	}

	@SuppressWarnings({ "unchecked", "null" })
	public Return generateReport(List list) {
		Return ret = new Return(true);

		GenericControl controller = getGeneratorReport();
		
		if (list != null || !list.isEmpty()){
			listReport = list;
			ret.concat(controller.doAction("generateReport"));
		}
		else if (params != null || !params.isEmpty()){
			ret.concat(controller.doAction("generateReport"));
		} else {
			ret.setValid(false);
			String msg = "A lista do parâmetro ou o listEntity não podem ser nulos";
			throw new NullPointerException(msg);
			//TODO Tratar como para disparar uma mensagem de excption..
		}

		return ret;
	}

	public void showReport() {
		Hashtable h = new Hashtable();
		h.put("Path", "/var/temp/");
		h.put("File", "test.pdf");
		
		String pathClass = this.getClass().getResource("").getFile();
		String pathApplication = pathClass.substring(0,
				pathClass.indexOf("WEB-INF"));
		
		Executions.getCurrent().createComponents(
				"/template/reportView.zul",null, h);
	}

}
