package br.com.vexillum.configuration;

import java.util.ResourceBundle;

/**
 * @author e670560
 *Classe respon�vel pelo properties de Configuraões
 */
@Deprecated
public class ConfigProperties extends Properties {	 
	
	/**
	 * Iniciando os properties passando o caminho dos properties de configuração.
	 */
	public ConfigProperties(){
        config = ResourceBundle.getBundle("properties/config");
	}

}
