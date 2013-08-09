package br.com.vexillum.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.model.UserPropertiesCategory;

@Service
@Scope("prototype")
public class UserPropertiesCategoryController extends GenericControl<UserPropertiesCategory> {

	public UserPropertiesCategoryController() {
		super(UserPropertiesCategory.class);
	}
	
	

}
