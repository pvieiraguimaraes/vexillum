package br.com.vexillum.vexreports.control;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.lang3.ArrayUtils;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import br.com.vexillum.control.GenericControl;
import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.vexreports.annotation.ReportField;

@SuppressWarnings("rawtypes")
public abstract class GenericGeneratorReporter extends
		GenericControl<ICommonEntity> {

	/**
	 * Lista da qual será gerada o relatório
	 */
	private List listReport;

	/**
	 * Lista de itens que deverão aparecer no relatório, caso não queira seguir
	 * os valores anotados pela anotação {@link ReportField}
	 */
	private String[] listItens;

	/**
	 * True, Caso queira adicionar um template ao relatório
	 */
	private boolean withTemplate = false;

	/**
	 * Se True estabelece que utilizará os valores anotados {@link ReportField}
	 */
	private boolean followAnnotation = true;

	/**
	 * Caso queira definir nomes diferentes dos anotados pela anotação
	 * {@link ReportField} e que aparecerão no relatório, deverão ser colocados
	 * o nome do campo como é na classe e o valor desejado. Ex. active =
	 * "Ativado"
	 */
	private Map<String, String> mapFieldsName;

	public GenericGeneratorReporter() {
		super(null);
	}

	@SuppressWarnings("unchecked")
	private void initReport() {
		listReport = (List<CommonEntity>) data.get("listReport");
		withTemplate = (Boolean) data.get("withTemplate");

		// True se for seguir as anotações
		followAnnotation = (Boolean) data.get("followAnnotation");

		// Se seguir a anotação será preenchidos em execução se não seguir
		// deverão ser preenchidos anteriormente
		listItens = (String[]) data.get("listItens");
		mapFieldsName = (Map<String, String>) data.get("mapFieldsName");
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
	}

	public void doReport() {
		try {
			generateDataReport(); // Gera valores nas listas de dados..
			DynamicReport report = buildReport(listReport, listItens,
					mapFieldsName);
			DynamicReport reportTemplate = getTemplateReport();
			if (withTemplate && reportTemplate != null)
				report = getTemplateReport();
			JasperPrint jasperPrint = DynamicJasperHelper.generateJasperPrint(
					report, new ClassicLayoutManager(), listReport);
			// JasperViewer.viewReport(jasperPrint);
			ReportExporter.exportReport(jasperPrint,
					"D:/Reports TESTE/ReflectiveReportTest.pdf");
			// TODO Serve para exportar o relatório em um ficheiro.
		} catch (JRException e) {
			new ExceptionManager(e).treatException();
		} catch (FileNotFoundException e) {
			new ExceptionManager(e).treatException();
		} catch (NullPointerException e) {
			e = new NullPointerException(
					"As lista do relatório não pode ser nulla, listReport");
			new ExceptionManager(e).treatException();
		}
	}

	/**
	 * Método seta os valores nas listas para gerar o relatório
	 */
	private void generateDataReport() throws NullPointerException {
		if (followAnnotation)
			readAnnotatedFields();

	}

	private void readAnnotatedFields() {
		if (!listReport.isEmpty()) {
			ICommonEntity entity = (ICommonEntity) listReport.get(0);
			List<Field> fields = ReflectionUtils.getAnnotatedFields(entity,
					ReportField.class);
			for (Field field : fields) {
				ReportField annotation = field.getAnnotation(ReportField.class);
				if (annotation.name() != "")
					mapFieldsName.put(field.getName(), annotation.name());
				if (annotation.order() != 0)
					listItens = ArrayUtils.add(listItens, annotation.order(),
							field.getName());
				else
					listItens = ArrayUtils.add(listItens, field.getName());

			}
		}
	}

	public AbstractColumn createAbstractColumn(String itemName,
			String nameCollum, Class valueClassName) {
		AbstractColumn column = ColumnBuilder.getNew()
				.setColumnProperty(itemName, valueClassName)
				.setTitle(nameCollum).build();
		return column;
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

	@SuppressWarnings("unchecked")
	public List<AbstractColumn> createColluns(String[] listItens,
			Map<String, String> mapFields) {
		List<AbstractColumn> cols = new ArrayList();

		for (String item : listItens) {
			String nameCollum = mapFields.get(item);
			cols.add(createAbstractColumn(item, nameCollum, getClassField(item)));
		}

		return cols;
	}

	/**
	 * Metodo que seta o template jrxml padrão para gerar os relatórios do
	 * projeto se houver
	 * 
	 * @return
	 */
	protected abstract DynamicReport getTemplateReport();

	/**
	 * Deverá ser implementado para gerar o relatorio para cada projeto
	 * 
	 * @param orderItens
	 * @param listItens
	 * @param listReport
	 * 
	 * @return
	 */
	protected abstract DynamicReport buildReport(List listReport,
			String[] listItens, Map<String, String> mapField);

	public Return generateReport() {
		initReport(); // Não esquecer de chamar essse método antes de executar o
						// doReport()
		Return ret = new Return(true);

		doReport();
		return ret;
	}
}
