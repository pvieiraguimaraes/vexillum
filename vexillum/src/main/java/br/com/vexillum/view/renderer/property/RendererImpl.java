package br.com.vexillum.view.renderer.property;

import org.zkoss.zk.ui.AbstractComponent;

public abstract class RendererImpl implements IRenderer {

	protected String value;
	
	public RendererImpl(String value){
		this.value = value;
	}
	
	@Override
	public abstract AbstractComponent render();

}
