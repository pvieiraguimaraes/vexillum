package br.com.vexillum.view.renderer;

import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.ListitemRenderer;

import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.view.CRUDComposer;

@SuppressWarnings("rawtypes")
public abstract class ConfigurationOrSystemPropertiesRenderer<T extends CommonEntity> implements ListitemRenderer {

	AnnotateDataBinder binder;
	CRUDComposer  composer;
	
	public ConfigurationOrSystemPropertiesRenderer(CRUDComposer composer){
		super();
		this.composer = composer;
		this.binder = new AnnotateDataBinder();
	}
	
}
