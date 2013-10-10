package br.com.vexillum.view.components;

import org.zkoss.zk.ui.HtmlMacroComponent;
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
	
	private ICommonEntity bindInEntity;
	
	private String bindInVariable;
	
	private String controller;
	
	private String listSize;
	
	private String criterias;

	public ICommonEntity getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(ICommonEntity targetEntity) {
		this.targetEntity = targetEntity == null ? null : HibernateUtils.materializeProxy(targetEntity);
		initBinder();
	}
	
	public ICommonEntity getBindInEntity() {
		return bindInEntity;
	}

	public void setBindInEntity(ICommonEntity bindInEntity) {
		this.bindInEntity = bindInEntity;
		initBinder();
	}

	public String getBindInVariable() {
		return bindInVariable;
	}

	public void setBindInVariable(String bindInVariable) {
		this.bindInVariable = bindInVariable;
		initBinder();
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

	@SuppressWarnings({ "unchecked" })
	private void initBinder() {
		if(getTargetEntity() != null && getBindInEntity() != null && getBindInVariable() != null){
			FilteredSearchComposer composer = getComposer();
			composer.setEntity(targetEntity);
			composer.setBindInEntity(bindInEntity);
			composer.setBindInVariable(bindInVariable);
			composer.initFilteredBinder();
		}
	}
	
	private FilteredSearchComposer getComposer(){
		return (FilteredSearchComposer) cmbFilteredSearch.getAttribute("controller");
	}

	public FilteredSearch() {
		super.compose();
	}
	
}
