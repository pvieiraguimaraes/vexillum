package br.com.vexillum.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@SuppressWarnings("rawtypes")
@Service
@Scope("prototype")
public class ValidatorController extends GenericControl {

	@SuppressWarnings("unchecked")
	public ValidatorController() {
		super(null);
	}

}
