package br.com.vexillum.vexreports.control;

import java.util.Map;

import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DJValueFormatter;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;

public class GenericGeneratorReporter {
	
	private DynamicReportBuilder drb;
	
	public GenericGeneratorReporter() {
		drb = new DynamicReportBuilder();
	}
	
	public DynamicReport buildReport() throws Exception {
//		drb.addColumn("State", "state", String.class.getName(),30)
//			.addColumn("Branch", "branch", String.class.getName(),30)
//			.addColumn("Product Line", "productLine", String.class.getName(),50)
//			.addColumn("Item", "item", String.class.getName(),50)
//			.addColumn("Item Code", "id", Long.class.getName(),30,true)
//			.addColumn("Quantity", "quantity", Long.class.getName(),60,true)
//			.addColumn("Amount", "amount", Float.class.getName(),70,true)
//			.addGroups(2)
//			.setTitle("November \"2006\" sales report")
//			.setSubtitle("This report was generated at " + new Date())
//			.setPrintBackgroundOnOddRows(true)			
//			.setUseFullPageWidth(true);

	    drb.addGlobalFooterVariable(drb.getColumn(4), DJCalculation.COUNT, null, new DJValueFormatter() {

	        public String getClassName() {
	            return String.class.getName();
	        }


	        public Object evaluate(Object value, Map fields, Map variables,   Map parameters) {
	            return (value == null ? "0" : value.toString()) + " Clients";
	        }
	    });


		DynamicReport dr = drb.build();

		return dr;
	}

}
