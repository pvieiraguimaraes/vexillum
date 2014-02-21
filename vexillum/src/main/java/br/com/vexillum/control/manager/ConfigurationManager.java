package br.com.vexillum.control.manager;

import java.util.HashMap;
import java.util.List;

import br.com.vexillum.control.ConfigurationController;
import br.com.vexillum.control.SystemPropertiesController;
import br.com.vexillum.control.UserPropertiesController;
import br.com.vexillum.model.Configuration;
import br.com.vexillum.model.SystemProperties;
import br.com.vexillum.model.SystemPropertiesCategory;
import br.com.vexillum.model.UserBasic;
import br.com.vexillum.model.UserProperties;
import br.com.vexillum.model.UserPropertiesCategory;
import br.com.vexillum.util.SpringFactory;

/**
 * Gerenciador das configurações do sistema.
 * @author fernando.lopes
 *
 */
public class ConfigurationManager implements IManager {	

	/**
	 * Classe singleton do gerenciador.
	 */
	static ConfigurationManager manager;
	
	/**
	 * Pega uma única instancia do {@link ConfigurationManager}
	 * @return {@link ConfigurationManager}
	 */
	public static ConfigurationManager getManager(){
		if(manager == null){
			manager = new ConfigurationManager();
		}
		return manager;
	}
	
	/**
	 * Pega a configuração de um usuário, baseado na chave.
	 * @param key Chave da configuração
	 * @param user Usuário
	 * @return Valor da configuração
	 */
	public String getProperty(String key, UserBasic user){
		String value = "";
		value = searchInSystemsProperties(key);
		if(value != null && !value.equals("")) return value;
		value = searchInConfigurations(key, user);
		if(value != null && !value.equals("")) return value;
//		value = searchInUserProperties(key);
//		if(value != null && !value.equals("")) return value;
		return value;
	}
	
	/**
	 * Procura a configuração nas configurações do sistema.
	 * @param key Chave da configuração
	 * @return Valor da configuração
	 */
	private String searchInSystemsProperties(String key){
		SystemProperties prop = getSystemPropertyInstance(key);
		if(prop == null || prop.getValue() == null ||  prop.getValue().isEmpty()){
			return "";
		}
		return prop.getValue();
	}
	
	/**
	 * Procura a configuração nas configurações do usuário.
	 * @param key Chave da configuração
	 * @return Valor da configuração
	 */
	private String searchInConfigurations(String key, UserBasic user){
		Configuration conf = getConfigurationInstance(key, user);
		if(conf == null || conf.getValue() == null || conf.getValue().isEmpty()){
			return "";
		}
		return conf.getValue();
	}
	
	/**
	 * Procura a configuração nas configurações do usuário.
	 * @param key Chave da configuração
	 * @return Valor da configuração
	 */
	@SuppressWarnings("unused")
	private String searchInUserProperties(String key){
		UserProperties prop = getUserPropertyInstance(key);
		if(prop.getDefaultValue() == null ||prop.getDefaultValue().isEmpty()){
			return "";
		}
		return prop.getDefaultValue();
	}
	
	
	/**
	 * Retorna a instancia de um {@link UserProperties}.
	 * @param key Chave da configuração
	 * @return Instancia da configuração
	 */
	@SuppressWarnings("unchecked")
	public UserProperties getUserPropertyInstance(String key){
		HashMap<String, Object> data =  new HashMap<String, Object>();
		
		UserProperties prop = new UserProperties();
		prop.setProperty(key);
		
		data.put("entity", prop);
		UserPropertiesController controller = SpringFactory.getController("userPropertiesController", UserPropertiesController.class, data);
		
		List<UserProperties> aux = (List<UserProperties>) controller.doAction("searchByCriteria").getList();
		if(aux == null || aux.isEmpty()){
			return null;
		}
		return aux.get(0);
	}
	
	/**
	 * Retorna a instancia de um {@link SystemProperties}
	 * @param key Chave da configuração
	 * @return Instancia da configuração
	 */
	public SystemProperties getSystemPropertyInstance(String key){
		HashMap<String, Object> data =  new HashMap<String, Object>();
		
		SystemProperties prop = new SystemProperties();
		prop.setProperty(key);
		
		data.put("entity", prop);
		//SystemPropertiesController controller = new SystemPropertiesController(data);
		SystemPropertiesController controller = SpringFactory.getController("systemPropertiesController", SystemPropertiesController.class, data);
		controller.setData(data);
		
		@SuppressWarnings("unchecked")
		List<SystemProperties> aux = (List<SystemProperties>) controller.doAction("searchByCriteria").getList();
		if(aux == null || aux.isEmpty()){
			return null;
		}
		prop = aux.get(0);
		if(prop.getValue() == null || prop.getValue().equals("")){
			prop.setValue(prop.getDefaultValue());
		}
		return prop;
	}
	
	/**
	 * Retorna a instancia de um {@link Configuration}
	 * @param key Chave da configuração
	 * @return Instancia da configuração
	 */
	@SuppressWarnings("unchecked")
	public Configuration getConfigurationInstance(String key, UserBasic user){
		HashMap<String, Object> data =  new HashMap<String, Object>();
		
		UserProperties prop = getUserPropertyInstance(key);
		if(prop == null) return null;
		
		Configuration conf = new Configuration();
		conf.setProperty(prop);
		conf.setUser(user);
		conf.setValue(prop == null ? "" : prop.getDefaultValue());
		
		if(user == null)
			return conf;
		
		data.put("entity", conf);
		ConfigurationController controlador = SpringFactory.getController("configurationController", ConfigurationController.class, data);
		
		List<Configuration> aux = (List<Configuration>) controlador.doAction("searchByCriteria").getList();
		if(aux == null || aux.isEmpty()){
			return conf;
		}
		return aux.get(0);
	}
	
	/**
	 * Carrega as configurações do usuário, baseado em uma categoria.
	 * @param cat Categoria
	 * @return Lista de {@link UserProperties}
	 */
	@SuppressWarnings("unchecked")
	public List<UserProperties> getUserPropertiesByCategory(UserPropertiesCategory cat){
		HashMap<String, Object> data =  new HashMap<String, Object>();
		
		UserProperties prop = new UserProperties();
		prop.setCategory(cat);
		
		data.put("entity", prop);
		UserPropertiesController controller = SpringFactory.getController("userPropertiesController", UserPropertiesController.class, data);
		
		List<UserProperties> list = (List<UserProperties>) controller.doAction("searchByCriteria").getList();
		
		return list;
	}
	
	/**
	 * Carrega as configurações do sistema, baseado em uma categoria.
	 * @param cat Categoria
	 * @return Lista de {@link SystemProperties}
	 */
	@SuppressWarnings("unchecked")
	public List<SystemProperties> getSystemPropertiesByCategory(SystemPropertiesCategory cat){
		HashMap<String, Object> data =  new HashMap<String, Object>();
		
		SystemProperties prop = new SystemProperties();
		prop.setCategory(cat);
		
		data.put("entity", prop);
		SystemPropertiesController controller = SpringFactory.getController("systemPropertiesController", SystemPropertiesController.class, data);
		
		List<SystemProperties> list = (List<SystemProperties>) controller.doAction("searchByCriteria").getList();
		
		return list;
	}
}
