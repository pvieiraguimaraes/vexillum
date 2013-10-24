package br.com.vexillum.vexreports.test;

import java.util.Date;
import java.util.HashMap;

import br.com.vexillum.vexreports.control.ReportExporter;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperDesignViewer;
import net.sf.jasperreports.view.JasperViewer;
import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Font;

public class ConcatenatedReportTest extends BaseDjReportTest {

	public DynamicReport buildReport() throws Exception {

		Style titleStyle = new Style();
		titleStyle.setFont(new Font(24, Font._FONT_VERDANA, true));

		/**
		 * 1st) Whe create an empty report (just title) and add 2 sub-reports
		 */
		DynamicReportBuilder drb = new DynamicReportBuilder();
		Integer margin = new Integer(20);
		drb
			.setTitleStyle(titleStyle)
			.setTitle("Concatenated reports")					//defines the title of the report
			.setSubtitle("All the reports shown here are concatenated as sub reports")
			.setDetailHeight(new Integer(15)).setLeftMargin(margin)
			.setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
			.setUseFullPageWidth(true)
			.setWhenNoDataAllSectionNoDetail()
			.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER,AutoText.ALIGNMENT_CENTER)
			.addConcatenatedReport(createSubreport1("Sub report 1"), new ClassicLayoutManager(), "statistics",
									DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION,false)
			.addConcatenatedReport(createSubreport2("Sub report 2"), "statistics",
									DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION,true)
			.addConcatenatedReport(createSubreport2("Sub report 3"), "statistics",
											DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION,true)
		.addConcatenatedReport(createSubreport2("Sub report 4"), "statistics",
				DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION,true);


		//Add the data source of the sub-report as a parameter
		params.put("statistics", Product.statistics_ );
		//add the subreport to the main report builder

		//Add the data source of the sub-report as a parameter
		params.put("statisticsArray", Product.statistics_.toArray() );

		/**
		 * Add one more report (from another test)
		 */
		//Create a subreport
//		drb.addConcatenatedReport(chartJr, "subreportsDataSource",
//									DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION,true);
		//Add the data source of the sub-report as a parameter
		params.put("subreportsDataSource", TestRepositoryProducts.getDummyCollection()  );

		//thats it!!!!
//		DynamicReport dr = drb.build();
		dr = drb.build();

		return dr;
	}

	public void testReport() throws Exception {
			dr = buildReport();

			jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);

			/**
			 * Creates the JasperPrint object, we pass as a Parameter
			 * the JasperReport object, and the JRDataSource
			 */
			jp = JasperFillManager.fillReport(jr, params);			
			ReportExporter.exportReport(jp, System.getProperty("user.dir") + "/target/ConcatenatedReportTest.pdf");

	}

	/**
	 * Creates a dynamic reports and compiles it in order to be used
	 * as a subreport
	 * @return
	 * @throws Exception
	 */
	private DynamicReport createSubreport1(String title) throws Exception {
		FastReportBuilder rb = new FastReportBuilder();
		DynamicReport dr = rb
			.addColumn("Date", "date", Date.class.getName(), 100)
			.addColumn("Average", "average", Float.class.getName(), 50)
			.addColumn("%", "percentage", Float.class.getName(), 50)
			.addColumn("Amount", "amount", Float.class.getName(), 50)
			.setMargins(5, 5, 20, 20)
			.setUseFullPageWidth(true)
			.setTitle(title)
			.build();
		return dr;
	}

	/**
	 * Creates a dynamic reports and compiles it in order to be used
	 * as a subreport
	 * @return
	 * @throws Exception
	 */
	private JasperReport createSubreport2(String title) throws Exception {
		FastReportBuilder rb = new FastReportBuilder();
		DynamicReport dr = rb
		.addColumn("Area", "name", String.class.getName(), 100)
		.addColumn("Average", "average", Float.class.getName(), 50)
		.addColumn("%", "percentage", Float.class.getName(), 50)
		.addColumn("Amount", "amount", Float.class.getName(), 50)
		.setMargins(5, 5, 20, 20)
		.setUseFullPageWidth(true)
		.setTitle(title)
		.build();
		return DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(),null);
	}

	public static void main(String[] args) throws Exception {
		ConcatenatedReportTest test = new ConcatenatedReportTest();
		test.testReport();
		JasperViewer.viewReport(test.jp);
		JasperDesignViewer.viewReportDesign(test.jr);
		test.exportToJRXML();
	}

}
