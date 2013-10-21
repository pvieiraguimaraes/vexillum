package br.com.vexillum.vexreports.test;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ReflectiveReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.util.SortUtils;

public class ReflectiveReportTest {

	// private DynamicReport buildOrderedReport(final Collection _data, final
	// String[] _properties) {
	// return new ReflectiveReportBuilder(_data,
	// _properties).addGroups(3).build();
	// }

	/**
	 * Test N� 1. With only the collection, the ReflectiveReportBuilder make
	 * some guesses
	 */
	public void testReport() {
		final Collection data = TestRepositoryProducts.getDummyCollection();
		DynamicReport dynamicReport = new ReflectiveReportBuilder(data).build();
		doReport(dynamicReport, data, "");
	}

	/**
	 * Test N�2, the same but we tell the builder the order of the columns, we
	 * also add 3 groups
	 */
	public void testOrderedReport() {
		final Collection data = TestRepositoryProducts
				.getDummyCollectionSmall();
		final List items = SortUtils.sortCollection(data,
				Arrays.asList(new String[] { "productLine", "item" }));
		String[] columOrders = new String[] { "productLine", "item" };
		DynamicReport dynamicReport = new ReflectiveReportBuilder(items,
				columOrders).build();
		
		List<AbstractColumn> columns = createColluns();
		dynamicReport.setColumns(columns);
		
		
		doReport(dynamicReport, items, "ordered");
	}

	private List<AbstractColumn> createColluns() {
		List<AbstractColumn> cols = new ArrayList<>();
		
		Style titleStyle = new Style();
			titleStyle.setFont(new Font(18, Font._FONT_VERDANA, true));
		
		Style headerStyle = new Style();
			headerStyle.setFont(Font.VERDANA_MEDIUM_BOLD);
			headerStyle.setBorderBottom(Border.PEN_2_POINT());
			headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
			headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
			headerStyle.setBackgroundColor(Color.DARK_GRAY);
			headerStyle.setTextColor(Color.WHITE);
			headerStyle.setTransparency(Transparency.OPAQUE);

		AbstractColumn productLine = ColumnBuilder.getNew()
				.setColumnProperty("productLine", String.class.getName())
				.setTitle("Linha Produto").setWidth(new Integer(85))
				.setStyle(titleStyle).setHeaderStyle(headerStyle).build();
		AbstractColumn item = ColumnBuilder.getNew()
				.setColumnProperty("item", String.class.getName())
				.setTitle("Item pt_BR").setWidth(new Integer(85))
				.setStyle(titleStyle).setHeaderStyle(headerStyle).build();
		
		cols.add(productLine);
		cols.add(item);
		
		return cols;
	}
	
	private AbstractColumn createAbstractColumn(String itemName, String nameCollum, String valueClassName){
		AbstractColumn column = ColumnBuilder.getNew()
				.setColumnProperty(itemName, valueClassName)
				.setTitle(nameCollum).build();
		return column;
	}

	public void doReport(final DynamicReport _report, final Collection _data,
			String name) {
		try {
			final JasperPrint jasperPrint = DynamicJasperHelper
					.generateJasperPrint(_report, new ClassicLayoutManager(),
							_data);
			JasperViewer.viewReport(jasperPrint);
			// ReportExporter.exportReport(jasperPrint,
			// System.getProperty("user.dir")
			// + "/target/ReflectiveReportTest " + name + ".pdf");
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public static void main(final String[] _args) {
		final ReflectiveReportTest reportTest = new ReflectiveReportTest();
		// reportTest.testReport();
		reportTest.testOrderedReport();
	}
}
