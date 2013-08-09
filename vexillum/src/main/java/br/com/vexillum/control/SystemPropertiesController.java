package br.com.vexillum.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.model.SystemProperties;

@Service
@Scope("prototype")
public class SystemPropertiesController extends GenericControl<SystemProperties> {

	public SystemPropertiesController() {
		super(SystemProperties.class);
	}
}
