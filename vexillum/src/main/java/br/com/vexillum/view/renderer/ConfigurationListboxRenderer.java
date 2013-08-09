package br.com.vexillum.view.renderer;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import br.com.vexillum.control.manager.ConfigurationManager;
import br.com.vexillum.model.Configuration;
import br.com.vexillum.model.Property;
import br.com.vexillum.view.PageConfigurationComposer;
import br.com.vexillum.view.renderer.property.RenderProperty;

public class ConfigurationListboxRenderer extends ConfigurationOrSystemPropertiesRenderer<Configuration> {

	public ConfigurationListboxRenderer(PageConfigurationComposer composer){
		super(composer);
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void render(Listitem item, Object data, int index) throws Exception {
		Configuration conf = ConfigurationManager.getManager().getConfigurationInstance(((Property)data).getProperty(), composer.getUserInSession());
		AnnotateDataBinder binder = new AnnotateDataBinder();
		
		binder.bindBean("each", conf);
		
		Listcell name = new Listcell();
		binder.addBinding(name, "label", "each.property.name");
		name.setParent(item);
		
		Listcell description = new Listcell();
		binder.addBinding(description, "label", "each.property.description");
		description.setParent(item);
		
		Listcell value = new Listcell();
		AbstractComponent fldValue = new RenderProperty(conf).render();
		binder.addBinding(fldValue, "value", "each.value");
		fldValue.addEventListener("onChange", getOnChangeConfigurationListener(conf));
		fldValue.setParent(value);
		value.setParent(item);
		
		Listcell cellDefault = new Listcell();
		Image icon = new Image("~./images/default-icon.png");
		icon.addEventListener("onClick", getDefaultValueEvent(conf));
		icon.setWidth("20px");
		icon.setHeight("20px");
		cellDefault.appendChild(icon);
		cellDefault.setParent(item);
		
		binder.loadAll();
		
	}
	
	@SuppressWarnings("rawtypes")
	protected EventListener getOnChangeConfigurationListener(final Configuration target){
		EventListener el = new EventListener(){
			@SuppressWarnings("unchecked")
			@Override
			public void onEvent(Event evt) throws Exception {
				binder.saveAll();
				Configuration conf = target;
				composer.setEntity(conf);
				composer.doAction("saveEntity");
			}
		};
		return el;
	}
	
	@SuppressWarnings("rawtypes")
	protected EventListener getDefaultValueEvent(final Configuration target){
		EventListener el = new EventListener(){
			@SuppressWarnings("unchecked")
			@Override
			public void onEvent(Event evt) throws Exception {
				Configuration conf = target;
				conf.setValue(conf.getProperty().getDefaultValue());
				composer.setEntity(conf);
				composer.doAction("saveEntity");
			}
		};
		return el;
	}
}
