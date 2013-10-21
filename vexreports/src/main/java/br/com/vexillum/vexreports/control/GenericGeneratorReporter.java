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
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
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

	public GenericGeneratorReporter() {
		super(null);
		try {
			reportConfig =  SpringFactory.getInstance().getBean("reportConfiguration", Properties.class);
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

	public Return doReport() {
		Return ret = new Return(true);
		try {
			generateDataReport(); // Gera valores nas listas de dados..
			DynamicReport report = buildReport();
			
			JasperPrint jasperPrint = DynamicJasperHelper.generateJasperPrint(
					report, new ClassicLayoutManager(), listReport);
			// JasperViewer.viewReport(jasperPrint);
			ReportExporter.exportReport(jasperPrint,
					"D:/Reports TESTE/ReflectiveReportTest.pdf");
			// TODO Serve para exportar o relatório em um ficheiro.
			return ret;
		} catch (JRException e) {
			ret.setValid(false);
			new ExceptionManager(e).treatException();
		} catch (FileNotFoundException e) {
			ret.setValid(false);
			new ExceptionManager(e).treatException();
		} catch (NullPointerException e) {
			ret.setValid(false);
			e = new NullPointerException(
					"As lista do relatório não pode ser nulla, listReport");
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
					resultListItens.add(annotation.order() - 1, field.getName());
				else
					resultListItens.add(field.getName());

			}
			listItens = convertListInArray(resultListItens);
		}
	}

	private String[] convertListInArray(List<String> resultListItens) {
		String[] array = new String[]{};
		for (String string : resultListItens) {
			array = ArrayUtils.add(array, string);
		}
		return array;
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
	protected abstract FastReportBuilder getTemplateReport(FastReportBuilder reportBuilder);

	/**
	 * Deverá ser implementado para gerar o relatorio para cada projeto
	 * 
	 * @param orderItens
	 * @param listItens
	 * @param listReport
	 * 
	 * @return
	 */
	protected abstract DynamicReport buildReport();

	public Return generateReport() {
		initReport(); // Não esquecer de chamar essse método antes de executar o
						// doReport()
		Return ret = new Return(true);

		doReport();
		return ret;
	}
}
