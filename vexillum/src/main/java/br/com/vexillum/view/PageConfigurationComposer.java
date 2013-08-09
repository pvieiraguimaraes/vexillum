package br.com.vexillum.view;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;

import br.com.vexillum.control.ConfigurationController;
import br.com.vexillum.control.SystemPropertiesCategoryController;
import br.com.vexillum.control.SystemPropertiesController;
import br.com.vexillum.control.UserPropertiesCategoryController;
import br.com.vexillum.control.manager.ConfigurationManager;
import br.com.vexillum.model.Configuration;
import br.com.vexillum.model.Property;
import br.com.vexillum.model.SystemProperties;
import br.com.vexillum.model.SystemPropertiesCategory;
import br.com.vexillum.model.UserProperties;
import br.com.vexillum.model.UserPropertiesCategory;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.renderer.ConfigurationListboxRenderer;
import br.com.vexillum.view.renderer.SystemPropertiesListboxRenderer;

@SuppressWarnings({ "serial" })
@org.springframework.stereotype.Component
@Scope("session")
public class PageConfigurationComposer extends CRUDComposer<Configuration, ConfigurationController> {

	private ConfigurationListboxRenderer configurationRenderer;
	private SystemPropertiesListboxRenderer systemPropertiesRenderer;
	
	private UserPropertiesCategory selectedUserCategory;
	private SystemPropertiesCategory selectedSystemCategory;
	
	private List<UserProperties> listUserProperties;
	private List<SystemProperties> listSystemProperties;
	
	private Property persistedProperty;
	
	public ConfigurationListboxRenderer getConfigurationRenderer() {
		return configurationRenderer;
	}

	public void setConfiguratioRenderer(ConfigurationListboxRenderer configurationRenderer) {
		this.configurationRenderer = configurationRenderer;
	}

	public SystemPropertiesListboxRenderer getSystemPropertiesRenderer() {
		return systemPropertiesRenderer;
	}

	public void setSystemPropertiesRenderer(
			SystemPropertiesListboxRenderer systemPropertiesRenderer) {
		this.systemPropertiesRenderer = systemPropertiesRenderer;
	}

	public UserPropertiesCategory getSelectedUserCategory() {
		return selectedUserCategory;
	}

	public void setSelectedUserCategory(UserPropertiesCategory selectedUserCategory) {
		this.selectedUserCategory = selectedUserCategory;
	}

	public SystemPropertiesCategory getSelectedSystemCategory() {
		return selectedSystemCategory;
	}

	public void setSelectedSystemCategory(SystemPropertiesCategory selectedSystemCategory) {
		this.selectedSystemCategory = selectedSystemCategory;
	}

	public List<UserProperties> getListUserProperties() {
		return listUserProperties;
	}

	public void setListUserProperties(List<UserProperties> listUserProperties) {
		this.listUserProperties = listUserProperties;
	}

	public List<SystemProperties> getListSystemProperties() {
		return listSystemProperties;
	}

	public void setListSystemProperties(List<SystemProperties> listSystemProperties) {
		this.listSystemProperties = listSystemProperties;
	}

	public Property getPersistedProperty() {
		return persistedProperty;
	}

	public void setPersistedProperty(Property persistedProperty) {
		this.persistedProperty = persistedProperty;
	}

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		this.configurationRenderer = new ConfigurationListboxRenderer(this);
		this.systemPropertiesRenderer = new SystemPropertiesListboxRenderer(this);
		loadBinder();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserPropertiesCategory> getUserPropertiesCategory() {
		Return ret = SpringFactory.getController("userPropertiesCategoryController", UserPropertiesCategoryController.class, ReflectionUtils.prepareDataForPersistence(this)).doAction("listAll");
		return (List<UserPropertiesCategory>) ret.getList();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserPropertiesCategory> getSystemPropertiesCategory() {
		Return ret = SpringFactory.getController("systemPropertiesCategoryController", SystemPropertiesCategoryController.class, ReflectionUtils.prepareDataForPersistence(this)).doAction("listAll");
		return (List<UserPropertiesCategory>) ret.getList();
	}
	
	public void getPropertiesByUserCategory() {
		listUserProperties = ConfigurationManager.getManager().getUserPropertiesByCategory(selectedUserCategory);
		loadBinder();
	}
	
	public void getPropertiesBySystemCategory() {
		listSystemProperties = ConfigurationManager.getManager().getSystemPropertiesByCategory(selectedSystemCategory);
		loadBinder();
	}
	
	public Return saveSystemProperty(){
		HashMap<String, Object> data =  new HashMap<String, Object>();
		
		data.put("entity", getPersistedProperty());
		SystemPropertiesController controller = SpringFactory.getController("systemPropertiesController", SystemPropertiesController.class, data);
		
		return controller.doAction("update");
	}
	
	@Override
	public ConfigurationController getControl() {
		return SpringFactory.getController("configurationController", ConfigurationController.class, ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public Configuration getEntityObject() {
		return new Configuration();
	}

}
