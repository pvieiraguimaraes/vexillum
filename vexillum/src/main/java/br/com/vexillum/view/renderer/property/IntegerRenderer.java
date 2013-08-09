package br.com.vexillum.view.renderer.property;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zul.Spinner;

public class IntegerRenderer extends RendererImpl {

	public IntegerRenderer(String value) {
		super(value);
	}

	@Override
	public AbstractComponent render() {
		Spinner field = new Spinner();
		field.setValue(Integer.parseInt(value));
		field.setInplace(true);
		field.setWidth("60px");
		field.setConstraint("min 0 max 100");
		
		return field;
	}

}
