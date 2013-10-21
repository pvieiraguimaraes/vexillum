package br.com.vexillum.vexreports.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.util.SortUtils;
import br.com.vexillum.vexreports.control.ReportExporter;

public abstract class BaseDjReportTest {

	public Map getParams() {
		return params;
	}

	protected static final Log log = LogFactory.getLog(BaseDjReportTest.class);

	protected JasperPrint jp;
	protected JasperReport jr;
	protected Map params = new HashMap();
	protected DynamicReport dr;

	public abstract DynamicReport buildReport() throws Exception;

	public void testReport() throws Exception {
		dr = buildReport();

		/**
		 * Get a JRDataSource implementation
		 */
		JRDataSource ds = getDataSource();

		/**
		 * Creates the JasperReport object, we pass as a Parameter the
		 * DynamicReport, a new ClassicLayoutManager instance (this one does the
		 * magic) and the JRDataSource
		 */
		jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(),
				params);

		/**
		 * Creates the JasperPrint object, we pass as a Parameter the
		 * JasperReport object, and the JRDataSource
		 */
		log.debug("Filling the report");
		if (ds != null)
			jp = JasperFillManager.fillReport(jr, params, ds);
		else
			jp = JasperFillManager.fillReport(jr, params);

		log.debug("Filling done!");
		log.debug("Exporting the report (pdf, xls, etc)");
		exportReport();

		log.debug("test finished");

	}

	protected LayoutManager getLayoutManager() {
		return new ClassicLayoutManager();
	}

	protected void exportReport() throws Exception {
		ReportExporter.exportReport(jp, System.getProperty("user.dir")
				+ "/target/reports/" + this.getClass().getName() + ".pdf");
		exportToJRXML();
	}

	protected void exportToJRXML() throws Exception {
		if (this.jr != null) {
			DynamicJasperHelper.generateJRXML(this.jr, "UTF-8",
					System.getProperty("user.dir") + "/target/reports/"
							+ this.getClass().getName() + ".jrxml");

		} else {
			DynamicJasperHelper.generateJRXML(this.dr, this.getLayoutManager(),
					this.params, "UTF-8", System.getProperty("user.dir")
							+ "/target/reports/" + this.getClass().getName()
							+ ".jrxml");
		}
	}

	protected void exportToHTML() throws Exception {
		ReportExporter.exportReportHtml(this.jp, System.getProperty("user.dir")
				+ "/target/reports/" + this.getClass().getName() + ".html");
	}

	/**
	 * @return JRDataSource
	 */
	protected JRDataSource getDataSource() {
		Collection dummyCollection = TestRepositoryProducts
				.getDummyCollection();
		dummyCollection = SortUtils.sortCollection(dummyCollection,
				dr.getColumns());

		JRDataSource ds = new JRBeanCollectionDataSource(dummyCollection); // Create
																			// a
																			// JRDataSource,
																			// the
																			// Collection
																			// used
																			// here
																			// contains
																			// dummy
																			// hardcoded
																			// objects...
		return ds;
	}

	public Collection getDummyCollectionSorted(List columnlist) {
		Collection dummyCollection = TestRepositoryProducts
				.getDummyCollection();
		return SortUtils.sortCollection(dummyCollection, columnlist);

	}

	public DynamicReport getDynamicReport() {
		return dr;
	}

	/**
	 * Uses a non blocking HSQL DB. Also uses HSQL default test data
	 * 
	 * @return a Connection
	 * @throws Exception
	 */
	public static Connection createSQLConnection() throws Exception {
		Connection con = null;
		Class.forName("org.hsqldb.jdbcDriver");
		con = DriverManager.getConnection(
				"jdbc:hsqldb:file:target/test-classes/hsql/test_dj_db", "sa",
				"");

		return con;
	}
}
