package br.com.vexillum.view.components;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;

import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.view.FilteredSearchComposer;

@SuppressWarnings("serial")
@VariableResolver(value = {org.zkoss.zkplus.spring.DelegatingVariableResolver.class})
public class FilteredSearch extends HtmlMacroComponent {
	
	@Wire
	private Combobox cmbFilteredSearch;
	
	private ICommonEntity targetEntity;
	
	private String bindIn;
	
	private String controller;
	
	private String listSize;
	
	private String criterias;
	
	private String disabled = "false";

	public ICommonEntity getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(ICommonEntity targetEntity) {
		this.targetEntity = targetEntity == null ? null : HibernateUtils.materializeProxy(targetEntity);
		initBinder();
	}
	
	public String getBindIn() {
		return bindIn;
	}

	public void setBindIn(String bindIn) {
		this.bindIn = bindIn;
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
		getComposer().setControllerClass(controller);
	}

	public String getListSize() {
		return listSize;
	}

	public void setListSize(String listSize) {
		this.listSize = listSize;
		getComposer().setListSize(listSize);
	}
	
	public String getCriterias() {
		return criterias;
	}

	public void setCriterias(String criterias) {
		this.criterias = criterias;
		getComposer().setCriterias(criterias);
	}
	
	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	@SuppressWarnings({ "unchecked" })
	private void initBinder() {
		if(getTargetEntity() != null && getBindIn() != null){
			FilteredSearchComposer composer = getComposer();
			composer.setEntity(targetEntity);
			composer.setBindIn(bindIn);
			composer.initFilteredBinder();
			
		}
	}
	
	private FilteredSearchComposer getComposer(){
		return (FilteredSearchComposer) cmbFilteredSearch.getAttribute("controller");
	}

	public FilteredSearch() {
		super.compose();
		cmbFilteredSearch.addForward(Events.ON_CHANGE, this, Events.ON_CHANGE);
		cmbFilteredSearch.setDisabled(Boolean.parseBoolean(getDisabled()));
	}
	
}
