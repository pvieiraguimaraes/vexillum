package br.com.vexillum.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.model.Configuration;

@Service
@Scope("prototype")
public class ConfigurationController extends GenericControl<Configuration> {

	public ConfigurationController() {
		
		super(Configuration.class);
	}

}
