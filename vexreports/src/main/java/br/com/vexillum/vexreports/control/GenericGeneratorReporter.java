package br.com.vexillum.vexreports.control;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.ExporterBuilders;
import net.sf.dynamicreports.jasper.builder.export.JasperHtmlExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.ComponentBuilders;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilders;
import net.sf.dynamicreports.report.exception.DRException;

import org.apache.commons.lang3.ArrayUtils;

import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import br.com.vexillum.configuration.Properties;
import br.com.vexillum.control.GenericControl;
import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.vexreports.annotation.ReportField;

@SuppressWarnings("rawtypes")
public abstract class GenericGeneratorReporter extends
		GenericControl<ICommonEntity> {

	/**
	 * Parâmetro utilizados para o setar variáveis no relatório seguindo o
	 * padrão JasperReport
	 */
	protected Map params;

	protected Properties reportConfig;

	/**
	 * Lista da qual será gerada o relatório
	 */
	protected List listReport;

	/**
	 * Lista de itens que deverão aparecer no relatório, caso não queira seguir
	 * os valores anotados pela anotação {@link ReportField}
	 */
	protected String[] listItens;

	/**
	 * True, Caso queira adicionar um template ao relatório
	 */
	protected boolean withTemplate = false;

	/**
	 * Se True estabelece que utilizará os valores anotados {@link ReportField}
	 */
	protected boolean followAnnotation = true;

	/**
	 * Caso queira definir nomes diferentes dos anotados pela anotação
	 * {@link ReportField} e que aparecerão no relatório, deverão ser colocados
	 * o nome do campo como é na classe e o valor desejado. Ex. active =
	 * "Ativado"
	 */
	protected Map<String, String> mapFieldsName;

	protected JasperReportBuilder report;
	
	
	
	protected StyleBuilders styleBuider;
	
	
	
	/**
	 * Output Stream para devolver o relatório para o ZK
	 */
	protected ServletOutputStream outputStream;
	
	
	
	
	
	/**
	 * Estilo do Título na coluna do relatório podendo ser alterado
	 */
	protected StyleBuilder columnTitleStyle;
	
	
	
	protected StyleBuilder boldCenteredStyle;
	
	protected StyleBuilder boldStyle;

	
	protected ComponentBuilders component;	

	public GenericGeneratorReporter() {
		super(null);
		report = DynamicReports.report();
		styleBuider = new StyleBuilders();
		component = new ComponentBuilders();
		try {
			reportConfig = SpringFactory.getInstance().getBean(
					"reportConfiguration", Properties.class);
		} catch (Exception e) {
			reportConfig = null;
		}
	}

	@SuppressWarnings("unchecked")
	private void initReport() {
		listReport = (List<CommonEntity>) data.get("listReport");
		withTemplate = (Boolean) data.get("withTemplate");

		followAnnotation = (Boolean) data.get("followAnnotation");

		listItens = (String[]) data.get("listItens");
		mapFieldsName = (Map<String, String>) data.get("mapFieldsName");

		params = (Map) data.get("params");
		outputStream = (ServletOutputStream) data.get("outputStream");

		initEntities();
	}

	@SuppressWarnings("unchecked")
	private void initEntities() {
		if (listReport == null)
			listReport = new ArrayList();
		if (listItens == null)
			listItens = new String[] {};
		if (mapFieldsName == null)
			mapFieldsName = new HashMap();
		if (params == null)
			params = new HashMap();
	}

	public Return doReport() {
		Return ret = new Return(true);
		try {
			generateDataReport();
			report = buildReport();
//			report.show(); Funciona somente para Java Application
			
			ExporterBuilders export = new ExporterBuilders();
			JasperPdfExporterBuilder pdfExporter = export.pdfExporter("D:/report.pdf");
			
//			JasperHtmlExporterBuilder htmlExporterBuilder = export.htmlExporter(outputStream);
			
//			report.toHtml(htmlExporterBuilder);
			
			report.toPdf(pdfExporter);
			
		} catch (NullPointerException e) {
			ret.setValid(false);
			e = new NullPointerException(
					"As lista do relatório não pode ser nulla, listReport");
			new ExceptionManager(e).treatException();
		} catch (DRException e) {
			ret.setValid(false);
			new ExceptionManager(e).treatException();
		}
		return ret;
	}

	/**
	 * Método seta os valores nas listas para gerar o relatório
	 */
	private void generateDataReport() throws NullPointerException {
		if (followAnnotation)
			readAnnotatedFields();
	}

	private void readAnnotatedFields() {
		List<String> resultListItens = new ArrayList<>();
		if (!listReport.isEmpty()) {
			ICommonEntity entity = (ICommonEntity) listReport.get(0);
			List<Field> fields = ReflectionUtils.getAnnotatedFields(entity,
					ReportField.class);
			for (Field field : fields) {
				ReportField annotation = field.getAnnotation(ReportField.class);
				if (annotation.name() != "")
					mapFieldsName.put(field.getName(), annotation.name());
				if (annotation.order() != 0)
					resultListItens
							.add(annotation.order() - 1, field.getName());
				else
					resultListItens.add(field.getName());

			}
			listItens = convertListInArray(resultListItens);
		}
	}

	private String[] convertListInArray(List<String> resultListItens) {
		String[] array = new String[] {};
		for (String string : resultListItens) {
			array = ArrayUtils.add(array, string);
		}
		return array;
	}

	public void createColluns(String[] listItens, Map<String, String> mapFields) {
		for (String item : listItens) {
			String nameCollum = mapFields.get(item);
			createColumn(item, nameCollum, getClassField(item));
		}
	}

	@SuppressWarnings("unchecked")
	public void createColumn(String item, String nameCollum, Class classField) {
		report.addColumn(Columns.column(nameCollum, item, classField));
	}

	private Class getClassField(String nameField) {
		ICommonEntity fieldEntity = (ICommonEntity) listReport.get(0);
		Field[] fields = ReflectionUtils.getFields(fieldEntity.getClass());
		for (Field field : fields) {
			if (field.getName().equalsIgnoreCase(nameField))
				return field.getType();
		}
		return null;
	}

	/**
	 * Metodo que seta o template jrxml padrão para gerar os relatórios do
	 * projeto se houver
	 * 
	 * @return
	 */
	protected abstract void getTemplateReport();

	/**
	 * Deverá ser implementado para gerar o relatorio para cada projeto
	 * 
	 * @return
	 */
	protected abstract JasperReportBuilder buildReport();

	public Return generateReport() {
		initReport();

		Return ret = new Return(true);
		ret.concat(doReport());

		return ret;
	}
}
