package br.com.vexillum.view.converter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;
import org.zkoss.zul.Image;

public class BooleanToImageConverter implements TypeConverter {
	
	private String pathImage1 = "";
	private String pathImage2 = "";
	private String width = "22px";
	private String height = "22px";
	
	public BooleanToImageConverter(String pathImage1) {
		this.pathImage1 = pathImage1;
	}
	
	public BooleanToImageConverter(String pathImage1, String pathImage2) {
		this.pathImage1 = pathImage1;
		this.pathImage2 = pathImage2;
	}
	
	public BooleanToImageConverter(String pathImage1, String pathImage2, Integer width, Integer height) {
		this.pathImage1 = pathImage1;
		this.pathImage2 = pathImage2;
		this.width = width + "px";
		this.height = height + "px";
	}
	
	@Override
	public Object coerceToUi(Object val, Component comp) {
		String image  = "";
		Image img;
		if((Boolean) val){
			image = pathImage1;
		} else {
			image = pathImage2;
		}
		img = new Image(image);
		img.setWidth(width);
		img.setHeight(height);
		img.setParent(comp);		
		return img;
//		img.setParent(component);
	}

	@Override
	public Object coerceToBean(Object val, Component comp) {
		return val;
	}
	
}
