package br.com.vexillum.view.renderer.property;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zkex.zul.Colorbox;

public class ColorRenderer extends RendererImpl {

	public ColorRenderer(String value) {
		super(value);
	}

	@Override
	public AbstractComponent render() {
		Colorbox field = new Colorbox();
		field.setHeight("15px");
		return field;
	}

}
