package br.com.vexillum.configuration;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;


/**
 * @author e670560
 *Classe respons�vel por criar os properties, esta classe deve ser extendida de especializa��es. Ex: Email, Configs, etc.
 */
public class Properties{
	
	protected String pathName;
	protected ResourceBundle config;
    
	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	@PostConstruct
	public void createResourceBundle() throws IOException{
		treatPathName();
		try {
			config = ResourceBundle.getBundle(pathName, Locale.getDefault());
		} catch (MissingResourceException e) {
			config = null;
		}
		
	}
	
	private void treatPathName(){
		Integer i = pathName.lastIndexOf(".properties");
		if(i != -1){
			pathName = pathName.substring(0, i);
		}
	}

	/**
     * Fun��o que retorna um valor do properties, baseado na key.
     * @param key
     * @return String
     */
    public String getKey(String key){
    	String value;
    	try {
			value = config.getString(key);
		} catch (Exception e) {
			value = "";
		}
    	return value;
    }

}
