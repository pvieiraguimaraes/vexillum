package br.com.vexillum.view.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

public class FormatedDateConverter implements TypeConverter {

	public Object coerceToUi(Object val, Component comp) {
		if(val instanceof Date){
			SimpleDateFormat sdf1= new SimpleDateFormat("dd/MM/yyyy");
			return sdf1.format(val);
		}
		return val;
	}

	public Object coerceToBean(Object val, Component comp) {
		return null;
	}

}
