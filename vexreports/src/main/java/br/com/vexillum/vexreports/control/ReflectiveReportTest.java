package br.com.vexillum.vexreports.control;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ReflectiveReportBuilder;
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
		final Collection data = TestRepositoryProducts.getDummyCollection();
		final List items = SortUtils.sortCollection(data,
				Arrays.asList(new String[] { "productLine", "item", "state" }));
		String[] columOrders = new String[] { "productLine", "item", "state",
				"id", "branch", "quantity", "amount" };
		DynamicReport dynamicReport = new ReflectiveReportBuilder(items,
				columOrders).addGroups(3).build();
		doReport(dynamicReport, items, "ordered");
	}

	public void doReport(final DynamicReport _report, final Collection _data,
			String name) {
		try {
			final JasperPrint jasperPrint = DynamicJasperHelper
					.generateJasperPrint(_report, new ClassicLayoutManager(),
							_data);
			JasperViewer.viewReport(jasperPrint);
			ReportExporter.exportReport(jasperPrint,
					System.getProperty("user.dir")
							+ "/target/ReflectiveReportTest " + name + ".pdf");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public static void main(final String[] _args) {
		final ReflectiveReportTest reportTest = new ReflectiveReportTest();
		reportTest.testReport();
		reportTest.testOrderedReport();
	}
}
