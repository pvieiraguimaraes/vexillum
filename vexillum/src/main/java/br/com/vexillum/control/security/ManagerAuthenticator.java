package br.com.vexillum.control.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import br.com.vexillum.model.Category;
import br.com.vexillum.model.UserBasic;

/**Classe implementada para gerenciar a autenticacao com o Spring Security
 * @author Pedro Henrique
 *
 * @param <U> que extenda de UerBasic
 * @param <P> que extenda de GrantedAuthority
 */
@SuppressWarnings("serial")
public class ManagerAuthenticator<U extends UserBasic, P extends GrantedAuthority> implements Authentication {
	
	private U user;
	private List<P> permissions;
	private Boolean authenticated;
	private Boolean administrator;
	
	public ManagerAuthenticator(U user, List<P> permissions) {
		this.user = user;
		this.permissions = permissions;
		this.administrator = permissions.contains(new Category("ROLE_ADMIN"));
	}

	@Override
	public String getName() {
		return user != null ? user.getName() : null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return permissions;
	}

	@Override
	public Object getCredentials() {
		return user != null ? user.getPassword() : null;
	}

	@Override
	public Object getDetails() {
		return user;
	}

	@Override
	public Object getPrincipal() {
		return user != null ? user.getEmail() : null;
	}

	@Override
	public boolean isAuthenticated() {
		return this.authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException {
		this.authenticated = isAuthenticated;

	}
	
	public boolean isAdministrator(){
		return administrator;
	}

}
