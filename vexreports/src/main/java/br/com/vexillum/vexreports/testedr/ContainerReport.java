package br.com.vexillum.vexreports.testedr;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class ContainerReport {
	private StyleBuilder boldCenteredStyle;
	private StyleBuilder borderedStyle;

	public ContainerReport() {
		build();
	}

	private void build() {
		boldCenteredStyle = stl.style().bold()
				.setHorizontalAlignment(HorizontalAlignment.CENTER);
		borderedStyle = stl.style(stl.pen1Point());

		try {
			report()// create new report design
			.title(createTextField("Horizontal list (contains 10 textfields)"),
					createHorizontalList(),
					cmp.verticalGap(20),
					createTextField("Multi row horizontal list (contains 10 textfields)"),
					createMultiRowHorizontalList(),
					cmp.verticalGap(20),
					createTextField("Horizontal flow list (contains 9 textfields)"),
					createHorizontalFlowList(),
					cmp.verticalGap(20),
					createTextField("Vertical list (contains 4 textfields)"),
					createVerticalList(),
					cmp.verticalGap(20),
					createTextField("Nested list (contains 1 horizontal and 3 vertical lists)"),
					createNestedList()).show();// create and show report
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	private ComponentBuilder<?, ?> createCustomerComponent(String label) {
		HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(
				stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));

		addCustomerAttribute(list, "Name", "Pedro Henrique");
		addCustomerAttribute(list, "RG", "5052468SPTCGO");
		addCustomerAttribute(list, "CPF", "036.049.571-09");
		addCustomerAttribute(list, "Email", "pvieiraguimaraes@gmail.com");
		return cmp.verticalList(cmp.text(label).setStyle(Templates.boldStyle),
				list);
	}

	private void addCustomerAttribute(HorizontalListBuilder list, String label,
			String value) {
		if (value != null) {
			list.add(cmp.text(label + ":").setFixedColumns(5).setStyle(Templates.boldStyle), cmp.text(value)).setStyle(borderedStyle)
					.newRow();
		}
	}

	private TextFieldBuilder<String> createTextField(String label) {
		return cmp.text(label).setStyle(boldCenteredStyle);
	}

	private ComponentBuilder<?, ?> createHorizontalList() {
		HorizontalListBuilder list = cmp.horizontalList();
		
		addCustomerAttribute(list, "Name", "Pedro Henrique");
		addCustomerAttribute(list, "RG", "5052468SPTCGO");
		addCustomerAttribute(list, "CPF", "036.049.571-09");
		addCustomerAttribute(list, "Email", "pvieiraguimaraes@gmail.com");
		
		list.add(cmp.text("Nome: Pedro Henrique").setStyle(
				borderedStyle));
		return list;
	}

	private ComponentBuilder<?, ?> createMultiRowHorizontalList() {
		HorizontalListBuilder horizontalList = cmp.horizontalList();
		for (int i = 0; i < 3; i++) {
			horizontalList.add(cmp.text("").setStyle(borderedStyle));
		}
		horizontalList.newRow();
		for (int i = 0; i < 7; i++) {
			horizontalList.add(cmp.text("").setStyle(borderedStyle));
		}
		return horizontalList;
	}

	private ComponentBuilder<?, ?> createHorizontalFlowList() {
		HorizontalListBuilder horizontalList = cmp.horizontalFlowList();
		for (int i = 0; i < 10; i++) {
			horizontalList.add(cmp.text("Teste").setStyle(borderedStyle));
		}
		return horizontalList;
	}

	private ComponentBuilder<?, ?> createVerticalList() {
		VerticalListBuilder verticalList = cmp.verticalList();
		for (int i = 0; i < 4; i++) {
			verticalList.add(cmp.text("").setStyle(borderedStyle));
		}
		return verticalList;
	}

	private ComponentBuilder<?, ?> createNestedList() {
		HorizontalListBuilder horizontalList = cmp.horizontalList();
		for (int i = 0; i < 3; i++) {
			horizontalList.add(createVerticalList());
		}
		return horizontalList;
	}

	public static void main(String[] args) {
		new ContainerReport();
	}
}
