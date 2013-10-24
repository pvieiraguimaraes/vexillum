package br.com.vexillum.vexreports.test;

import java.util.Collection;
import java.util.Date;

import net.sf.jasperreports.view.JasperViewer;
import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;

public class SubReportRecursiveTest extends BaseDjReportTest {

	public DynamicReport buildReport() throws Exception {



		FastReportBuilder drb = new FastReportBuilder();
		drb.addColumn("State", "state", String.class.getName(),30)
			.addColumn("Branch", "branch", String.class.getName(),30)
			.addGroups(2)
			.setMargins(5, 5, 20, 20)
			.addField("statistics", Collection.class.getName())
			.setTitle("November 2006 sales report")
			.setSubtitle("This report was generated at " + new Date())
			.setUseFullPageWidth(true);

		//Create level 2 sub-report
		DynamicReport drLevel2 = createLevel2Subreport();

		//now create and put level2 subreport in the main subreport
		drb.addSubreportInGroupFooter(2, drLevel2, new ClassicLayoutManager(),
				"statistics", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION);

		DynamicReport mainReport = drb.build();


		return mainReport;
	}

	private DynamicReport createLevel2Subreport() throws Exception {
		FastReportBuilder rb = new FastReportBuilder();
		DynamicReport dr = rb
			.addColumn("Date", "date", Date.class.getName(), 100)
			.addColumn("Average", "average", Float.class.getName(), 50)
			.addColumn("%", "percentage", Float.class.getName(), 50)
			.addColumn("Amount", "amount", Float.class.getName(), 50)
			.addGroups(1)
			.addField("dummy3", Collection.class.getName())
			.setMargins(5, 5, 20, 20)
			.setUseFullPageWidth(true)
			.setTitle("Level 2 Subreport")
			.addSubreportInGroupFooter(1, createLevel3Subreport(), new ClassicLayoutManager(),
				"dummy3", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION)
			.build();
		return dr;
	}

	private DynamicReport createLevel3Subreport() throws Exception {
		FastReportBuilder rb = new FastReportBuilder();
		DynamicReport dr = rb
		.addColumn("Name", "name", String.class.getName(), 100)
		.addColumn("Number", "number", Long.class.getName(), 50)
		.setMargins(5, 5, 20, 20)
		.setUseFullPageWidth(false)
		.setTitle("Level 3 Subreport")
		.build();
		return dr;
	}

	public static void main(String[] args) throws Exception {
		SubReportRecursiveTest test = new SubReportRecursiveTest();
		test.testReport();
		JasperViewer.viewReport(test.jp);
	}

}