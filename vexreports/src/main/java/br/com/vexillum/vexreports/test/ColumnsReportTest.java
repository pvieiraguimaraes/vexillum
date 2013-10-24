package br.com.vexillum.vexreports.test;

import java.util.Date;

import net.sf.jasperreports.view.JasperViewer;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;

public class ColumnsReportTest extends BaseDjReportTest {

	public DynamicReport buildReport() throws Exception {


		/**
		 * Creates the DynamicReportBuilder and sets the basic options for
		 * the report
		 */
		FastReportBuilder drb = new FastReportBuilder();
		drb.addColumn("State", "state", String.class.getName(),30)
			.addColumn("Branch", "branch", String.class.getName(),30)
			.addColumn("Item", "item", String.class.getName(),50)
			.addColumn("Item", "item", String.class.getName(),50)
			.addGroups(4)
			.setTitle("November 2006 sales report")
			.setSubtitle("This report was generated at " + new Date())
			.setColumnsPerPage(4,1)
			.setUseFullPageWidth(false);

		DynamicReport dr = drb.build();

		return dr;
	}

	public static void main(String[] args) throws Exception {
		ColumnsReportTest test = new ColumnsReportTest();
		test.testReport();
		JasperViewer.viewReport(test.jp);	//finally display the report report
//			JasperDesignViewer.viewReportDesign(jr);
	}

}