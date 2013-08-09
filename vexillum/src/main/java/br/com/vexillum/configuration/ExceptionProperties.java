package br.com.vexillum.configuration;

import java.util.ResourceBundle;

@Deprecated
public class ExceptionProperties extends Properties {

	public ExceptionProperties(){
        config = ResourceBundle.getBundle("properties/exception");
	}
	
}
