package br.com.vexillum.vexreports.control;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.util.SortUtils;
import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.Return;

@SuppressWarnings("rawtypes")
public abstract class GenericGeneratorReporter extends GenericControl<ICommonEntity> {

	private List listReport;
	private String[] listItens;
	private String[] orderItens;

	public GenericGeneratorReporter() {
		super(null);
		initReport();
	}

	@SuppressWarnings("unchecked")
	private void initReport() {
		listReport = (List<CommonEntity>) data.get("listReport");
//		listItens = (String[]) data.get("listItens");
//		orderItens = (String[]) data.get("orderItens");
	}

	public void doReport(Collection data) {
		try {
			DynamicReport report = buildReport(listReport, listItens,
					orderItens);
			String templateFileName = getTemplateReport();
			if (templateFileName != null)
				report.setTemplateFileName(templateFileName);
			final JasperPrint jasperPrint = DynamicJasperHelper
					.generateJasperPrint(report, new ClassicLayoutManager(),
							data);
			JasperViewer.viewReport(jasperPrint);
			// ReportExporter.exportReport(jasperPrint,
			// System.getProperty("user.dir")
			// + "/target/ReflectiveReportTest " + name + ".pdf");
			// TODO Serve para exportar o relatório em um ficheiro.
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que seta o template jrxml padrão para gerar os relatórios do
	 * projeto se houver
	 * 
	 * @return
	 */
	protected abstract String getTemplateReport();

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

		List items = SortUtils.sortCollection(listReport,
				Arrays.asList(listItens));

		doReport(items);
		return ret;
	}
}
