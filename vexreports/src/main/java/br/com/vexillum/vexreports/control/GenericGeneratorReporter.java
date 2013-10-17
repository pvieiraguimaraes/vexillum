package br.com.vexillum.vexreports.control;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.Return;

@SuppressWarnings("rawtypes")
public abstract class GenericGeneratorReporter extends GenericControl<ICommonEntity> {

	private List listReport;
	private String[] listItens;
	private String[] orderItens;
	private boolean withTemplate = false;

	public GenericGeneratorReporter() {
		super(null);
	}

	@SuppressWarnings("unchecked")
	private void initReport() {
		listReport = (List<CommonEntity>) data.get("listReport");
		withTemplate = (Boolean) data.get("withTemplate");
//		listItens = (String[]) data.get("listItens");
//		orderItens = (String[]) data.get("orderItens");
	}

	public void doReport(Collection data) {
		try {
			DynamicReport report = buildReport(listReport, listItens,
					orderItens);
			DynamicReport reportTemplate = getTemplateReport();
			if(withTemplate && reportTemplate != null)
				report = getTemplateReport();
			JasperPrint jasperPrint = DynamicJasperHelper
					.generateJasperPrint(report, new ClassicLayoutManager(),
							data);
//			JasperViewer.viewReport(jasperPrint);
			 ReportExporter.exportReport(jasperPrint,"D:/Reports TESTE/ReflectiveReportTest.pdf");
			// TODO Serve para exportar o relatório em um ficheiro.
		} catch (JRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
			String[] listItens, String[] orderItens);

	public Return generateReport() {
		Return ret = new Return(true);
		initReport();
//		List items = SortUtils.sortCollection(listReport,
//				Arrays.asList(listItens));

		doReport(listReport);
		return ret;
	}
}
