package br.com.vexillum.view.renderer;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import br.com.vexillum.control.manager.ConfigurationManager;
import br.com.vexillum.model.Property;
import br.com.vexillum.model.SystemProperties;
import br.com.vexillum.view.PageConfigurationComposer;
import br.com.vexillum.view.renderer.property.RenderProperty;

public class SystemPropertiesListboxRenderer extends ConfigurationOrSystemPropertiesRenderer<SystemProperties> {
	
	public SystemPropertiesListboxRenderer(PageConfigurationComposer composer){
		super(composer);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void render(Listitem item, Object data, int index) throws Exception {
		SystemProperties prop = ConfigurationManager.getManager().getSystemPropertyInstance(((Property) data).getProperty());
		AnnotateDataBinder binder = new AnnotateDataBinder();
		
		binder.bindBean("each", prop);
		
		Listcell name = new Listcell();
		binder.addBinding(name, "label", "each.name");
		name.setParent(item);
		
		Listcell description = new Listcell();
		binder.addBinding(description, "label", "each.description");
		description.setParent(item);
		
		Listcell value = new Listcell();
		AbstractComponent fldValue = new RenderProperty(prop).render();
		binder.addBinding(fldValue, "value", "each.value");
		fldValue.addEventListener("onChange", getOnChangeConfigurationListener(prop));
		fldValue.setParent(value);
		value.setParent(item);
		
		Listcell cellDefault = new Listcell();
		Image icon = new Image("~./images/default-icon.png");
		icon.addEventListener("onClick", getDefaultValueEvent(prop));
		icon.setWidth("20px");
		icon.setHeight("20px");
		cellDefault.appendChild(icon);
		cellDefault.setParent(item);
		
		binder.loadAll();
	}
	
	@SuppressWarnings("rawtypes")
	protected EventListener getOnChangeConfigurationListener(final SystemProperties target){
		EventListener el = new EventListener(){
			@Override
			public void onEvent(Event evt) throws Exception {
				binder.saveAll();
				SystemProperties conf = target;
				((PageConfigurationComposer) composer).setPersistedProperty(conf);
				composer.doAction("saveSystemProperty");
			}
		};
		return el;
	}
	
	@SuppressWarnings("rawtypes")
	protected EventListener getDefaultValueEvent(final SystemProperties target){
		EventListener el = new EventListener(){
			@SuppressWarnings("unchecked")
			@Override
			public void onEvent(Event evt) throws Exception {
				SystemProperties prop = target;
				prop.setValue(null);
				composer.setEntity(prop);
				composer.doAction("saveEntity");
			}
		};
		return el;
	}

}
