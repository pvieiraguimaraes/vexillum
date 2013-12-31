package br.com.vexillum.control.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import br.com.vexillum.control.UserBasicControl;
import br.com.vexillum.model.Category;
import br.com.vexillum.model.UserBasic;
import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.SpringFactory;

public class AuthenticatorProvider<U extends UserBasic> implements
		AuthenticationProvider {

	protected UserBasicControl<?> control;
	
	public AuthenticatorProvider() {
		control = SpringFactory.getController(
				"userBasicControl", UserBasicControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) auth;
		String userName = token.getName();
		String password = token.getCredentials() != null ? token.getCredentials().toString() : null;
		U user = (U) control.getUser(userName, password);
		if (user == null)
			return null;
		//TODO Tratar permissoes depois sem ser como categoria
		//List<String> permissions = control.getPermissionsUser(user);
		List<Category> userCategories = new ArrayList<Category>();
		userCategories.add(control.getCategoryUser(user));
		ManagerAuthenticator<U, Category> manager = new ManagerAuthenticator<U, Category>(HibernateUtils.materializeProxy(user), userCategories);
		manager.setAuthenticated(user != null);
		return manager;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class
				.isAssignableFrom(authentication));
	}

}
