package br.com.vexillum.vexreports.control;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ReflectiveReportBuilder;
import ar.com.fdvs.dj.util.SortUtils;
import br.com.vexillum.control.GenericControl;
import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.util.Return;

@SuppressWarnings("rawtypes")
@Service
@Scope("prototype")
public class ReportsControl extends GenericControl<CommonEntity> {
	
	public ReportsControl() {
		super(null);
	}
	
	@SuppressWarnings("unchecked")
	public Return generateReport(){
		Return ret = new Return(true);
		List<CommonEntity> listReport = (List<CommonEntity>) data.get("listReport");
		
		
		String[] listItens = (String[]) data.get("listItens");
		String[] orderItens = (String[]) data.get("orderItens");
		List items = SortUtils.sortCollection(listReport,
				Arrays.asList(listItens));
		DynamicReport dynamicReport = new ReflectiveReportBuilder(items,
				orderItens).addGroups(3).build();
		doReport(dynamicReport, items, "ordered");
		return ret;
	}
	
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
			new ExceptionManager(e).treatException();
		}
	}
	
	public void showReport(){
		
	}

}
