package br.com.vexillum.vexreports.control;

import java.io.FileNotFoundException;
import java.util.Collection;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;

public class GenericReporter {
	
	public GenericReporter() {
		
	}

	@SuppressWarnings("rawtypes")
	public void doReport(DynamicReport report, Collection data,
			String name) {
		try {
			final JasperPrint jasperPrint = DynamicJasperHelper
					.generateJasperPrint(report, new ClassicLayoutManager(),
							data);
			JasperViewer.viewReport(jasperPrint);
//			ReportExporter.exportReport(jasperPrint,
//					System.getProperty("user.dir")
//							+ "/target/ReflectiveReportTest " + name + ".pdf");
			//TODO Serve para exportar o relatório em um ficheiro.
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
