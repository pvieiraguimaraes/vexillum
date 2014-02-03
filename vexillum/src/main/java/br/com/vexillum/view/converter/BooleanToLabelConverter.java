package br.com.vexillum.view.converter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

public class BooleanToLabelConverter implements TypeConverter {

	public Object coerceToUi(Object val, Component comp) {
		if(val instanceof Boolean){
			if((Boolean) val){
				return "Sim";
			}  else {
				return "N�o";
			}
		}
		return val;
	}

	public Object coerceToBean(Object val, Component comp) {
		return null;
	}

}
