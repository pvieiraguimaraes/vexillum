package br.com.vexillum.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.model.UserProperties;

@Service
@Scope("prototype")
public class UserPropertiesController extends GenericControl<UserProperties> {

	public UserPropertiesController() {
		super(UserProperties.class);
	}

}
