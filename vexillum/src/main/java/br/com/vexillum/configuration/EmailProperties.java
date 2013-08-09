package br.com.vexillum.configuration;

import java.util.ResourceBundle;

@Deprecated
public class EmailProperties extends Properties {
	
	public EmailProperties(){
        config = ResourceBundle.getBundle("properties/email");
	}
	
}
