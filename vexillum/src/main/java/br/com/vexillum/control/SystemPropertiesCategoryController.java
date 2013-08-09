package br.com.vexillum.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.model.SystemPropertiesCategory;

@Service
@Scope("prototype")
public class SystemPropertiesCategoryController extends GenericControl<SystemPropertiesCategory> {

	public SystemPropertiesCategoryController() {
		super(SystemPropertiesCategory.class);
	}
	
	

}
