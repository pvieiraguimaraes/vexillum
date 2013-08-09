package br.com.vexillum.view.renderer.property;

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.AbstractComponent;

import br.com.vexillum.model.Configuration;
import br.com.vexillum.model.Property;

public class RenderProperty {
	
	private Object obj;
	
	public RenderProperty(Configuration conf){
		this.obj = conf;
	}
	
	public RenderProperty(Property prop){
		this.obj = prop;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractComponent render(){
		AbstractComponent field = null;
		String type = obj instanceof Configuration ? ((Configuration)obj).getProperty().getType() : ((Property)obj).getType();
		String value = obj instanceof Configuration ? ((Configuration)obj).getValue() : ((Property)obj).getDefaultValue();
		try {
			Class crenderer = Class.forName("br.com.vexillum.view.renderer.property." + StringUtils.capitalize(type) + "Renderer");
			Constructor cons = crenderer.getConstructor(new Class[]{String.class});
			IRenderer renderer = (IRenderer) cons.newInstance(value);
			field = renderer.render();
		} catch (ClassNotFoundException e) {
			field =  new TextRenderer(type).render();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return field;
	}
}
