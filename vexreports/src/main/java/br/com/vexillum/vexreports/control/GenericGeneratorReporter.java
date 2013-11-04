package br.com.vexillum.vexreports.control;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.ExporterBuilders;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.base.DRReportTemplate;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilders;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilders;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.exception.DRException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.spi.SessionFactoryImplementor;

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

	protected String titleReport;

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
	 * True, Caso queira adicionar um cabeçalho ao relatório
	 */
	protected boolean withHeader = true;

	/**
	 * True, Caso queira adicionar um rodapé ao relatório
	 */
	protected boolean withFooter = true;

	/**
	 * True, Caso queira que seja adicionado um título.
	 */
	protected boolean withTitle = true;

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
	protected ServletOutputStream outputStream;// Não usado ainda

	/**
	 * Construtores de Estilos
	 */
	protected StyleBuilder columnTitleStyle;

	protected StyleBuilder boldCenteredStyle;

	protected StyleBuilder boldStyle;

	/**
	 * Contrutores de componentes
	 */
	protected ComponentBuilders component;

	protected ComponentBuilder<?, ?> dynamicReportsComponent;

	protected ComponentBuilder<?, ?> footerComponent;

	protected String pathTemplate;

	protected ReportTemplateBuilder templateBuilder;

	protected DRReportTemplate templateReport;

	public GenericGeneratorReporter() {
		super(null);
		report = new JasperReportBuilder();
		styleBuider = new StyleBuilders();
		component = new ComponentBuilders();
		templateReport = new DRReportTemplate();
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

		titleReport = (String) data.get("titleReport");

		withTemplate = (Boolean) data.get("withTemplate");
		withHeader = (Boolean) data.get("withHeader");
		withFooter = (Boolean) data.get("withFooter");
		withTitle = (Boolean) data.get("withTitle");

		followAnnotation = (Boolean) data.get("followAnnotation");

		listItens = (String[]) data.get("listItens");
		mapFieldsName = (Map<String, String>) data.get("mapFieldsName");

		params = (Map) data.get("params");

		// TODO Ainda não está sendo usado
		outputStream = (ServletOutputStream) data.get("outputStream");

		pathTemplate = (String) data.get("pathTemplate");

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

	@SuppressWarnings("unchecked")
	public JasperReportBuilder createReport(Collection<?> dataSource,
			Map param, boolean subReportTemplate, boolean subReportHeader,
			String pathTemplate, boolean subReportFooter,
			boolean subReportTitle, boolean subFollowAnnotation,
			String actionHeader, String actionFooter, String actionTitle, String sql) {
		JasperReportBuilder report = new JasperReportBuilder();

		report.setParameters(param);
		report.setDataSource(dataSource);

		if (subFollowAnnotation)
			readAnnotatedFields();

		if (listItens.length != 0 && !mapFieldsName.isEmpty())
			report = createColluns(listItens, mapFieldsName, report);

		if (subReportTemplate)
			report = setTemplateReport(report, pathTemplate, sql);

		if (subReportHeader)
			report = getHeaderReport(report, actionHeader);

		if (subReportFooter)
			report = getFooterReport(report, actionHeader);

		if (subReportTitle)
			report = getTitleReport(report, actionTitle);

		return report;
	}

	public Return doReport() {
		Return ret = new Return(true);
		try {
			report = buildReport();

			// report.show(); Funciona somente para Java Application

			ExporterBuilders export = new ExporterBuilders();
			JasperPdfExporterBuilder pdfExporter = export
					.pdfExporter("D:/Reports TESTE/certos/report.pdf");

			// JasperHtmlExporterBuilder htmlExporterBuilder =
			// export.htmlExporter(outputStream);

			// report.toHtml(htmlExporterBuilder);

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
	 * Método responsável por ler os campos anotados e mapear os nomes que
	 * deverão aparecer no relatório sendo assim sua execução é a seguinte: 1º)
	 * Tenta ler a anotação {@link ReportField} os valores para os campos name e
	 * order, nome e ordem, respectivamente; 2º) Caso encontre valores para
	 * essas variaveis seta o nome no Map dos nomes e coloca o item na ordem
	 * dada no vetor de itens; 3º) Por fim, se não encontrar na anotação o nome
	 * seta o próprio nome do field mesmo;
	 */
	private void readAnnotatedFields() {
		List<String> resultListItens = new ArrayList<>();
		if (!listReport.isEmpty()) {
			ICommonEntity entity = (ICommonEntity) listReport.get(0);
			List<Field> fields = ReflectionUtils.getAnnotatedFields(entity,
					ReportField.class);
			for (Field field : fields) {
				ReportField annotation = field.getAnnotation(ReportField.class);
				if (annotation.name() != "" || annotation.name() != null)
					mapFieldsName.put(field.getName(), annotation.name());
				else
					mapFieldsName.put(field.getName(), field.getName());
				if (annotation.order() != 0)
					resultListItens
							.add(annotation.order() - 1, field.getName());
				else
					resultListItens.add(field.getName());

			}
			listItens = convertListInArray(resultListItens);
		}
	}

	public String generateTitleReport() {
		if (!listReport.isEmpty()) {
			ICommonEntity entity = (ICommonEntity) listReport.get(0);
			String nameEntity = entity.getClass().getSimpleName();

			String nameReport = "REPORT_NAME_"
					+ StringUtils.upperCase(nameEntity);

			return reportConfig.getKey(nameReport);
		}
		return null;
	}

	private String[] convertListInArray(List<String> resultListItens) {
		String[] array = new String[] {};
		for (String string : resultListItens) {
			array = ArrayUtils.add(array, string);
		}
		return array;
	}

	public JasperReportBuilder createColluns(String[] listItens,
			Map<String, String> mapFields, JasperReportBuilder report) {
		JasperReportBuilder reportBuilder = new JasperReportBuilder();
		for (String item : listItens) {
			String nameCollum = mapFields.get(item);
			reportBuilder = createColumn(item, nameCollum, getClassField(item),
					report);
		}
		return reportBuilder;
	}

	@SuppressWarnings("unchecked")
	public JasperReportBuilder createColumn(String item, String nameCollum,
			Class classField, JasperReportBuilder report) {
		return report.addColumn(Columns
				.column(nameCollum, item, classField)
				.setWidth(getColumnWidth(classField))
				.setStyle(
						styleBuider.style().setAlignment(
								HorizontalAlignment.CENTER,
								VerticalAlignment.JUSTIFIED)));
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
	@SuppressWarnings("deprecation")
	protected JasperReportBuilder setTemplateReport(JasperReportBuilder report,
			String pathTemplate, String sql) {
		try {
			Connection connection = ((SessionFactoryImplementor) this
					.getPersistence().getSession().getSessionFactory())
					.getConnectionProvider().getConnection();
//			report.setConnection(connection);
			report.setDataSource(sql, connection);
			report.setTemplateDesign(pathTemplate);
		} catch (DRException e) {
			e.printStackTrace();
			new ExceptionManager(e).treatException();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return report;
	}

	/**
	 * Método que seta o cabeçalho para o relatório, sobrescrevê-lo para setar
	 * um cabeçalho.
	 */
	protected JasperReportBuilder getHeaderReport(JasperReportBuilder report,
			String actionHeader) {
		Return ret = new Return(true);
		data.put("report", report);
		ret.concat(doAction(actionHeader, false));
		return (JasperReportBuilder) ret.getList().get(0);
	}

	/**
	 * Método que seta o rodapé no relatório, sobrescrevê-lo para setar um
	 * rodapé.
	 */
	protected JasperReportBuilder getFooterReport(JasperReportBuilder report,
			String actionFooter) {
		Return ret = new Return(true);
		data.put("report", report);
		ret.concat(doAction(actionFooter, false));
		return (JasperReportBuilder) ret.getList().get(0);
	}

	/**
	 * Método que seta o Título no relatório, de acordo com o que for
	 * implementado, bastanto sobrescrevê-lo
	 */
	protected JasperReportBuilder getTitleReport(JasperReportBuilder report,
			String actionTitle) {
		Return ret = new Return(true);
		data.put("report", report);
		ret.concat(doAction(actionTitle, false));
		return (JasperReportBuilder) ret.getList().get(0);
	}

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

	/**
	 * Método tirado do DynamicJasper, que seta por padrão os valores da largura
	 * das colunas
	 * 
	 * @param classz
	 * @return
	 */
	private int getColumnWidth(Class classz) {
		if (Float.class.isAssignableFrom(classz)
				|| Double.class.isAssignableFrom(classz)) {
			return 70;
		} else if (classz == Boolean.class) {
			return 10;
		} else if (Number.class.isAssignableFrom(classz)) {
			return 60;
		} else if (classz == String.class) {
			return 100;
		} else if (Date.class.isAssignableFrom(classz)) {
			return 50;
		} else {
			return 50;
		}
	}
}
