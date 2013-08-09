package br.com.vexillum.view.renderer.property;

import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

public class TextRenderer extends RendererImpl {
	
	public TextRenderer(String value){
		super(value);
	}
	
	@Override
	public InputElement render(){
		Textbox text = new Textbox();
		text.setInplace(true);
		text.setWidth("80px");
		
		return text;
	}
}
