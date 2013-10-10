package br.com.vexillum.view;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Combobox;

import br.com.vexillum.control.FilteredSearchController;
import br.com.vexillum.control.GenericControl;
import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.model.interfaces.IFilteredSearch;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.SpringFactory;

@SuppressWarnings({ "serial", "rawtypes" })
@Component
@Scope("prototype")
public class FilteredSearchComposer extends CRUDComposer {

	private ICommonEntity bindInEntity;
	
	private String bindInVariable;
	
	private String controllerClass;
	
	private String listSize;
	
	private String criterias;
	
	public ICommonEntity getBindInEntity() {
		return bindInEntity;
	}

	public void setBindInEntity(ICommonEntity bindInEntity) {
		this.bindInEntity = bindInEntity;
	}

	public String getBindInVariable() {
		return bindInVariable;
	}

	public void setBindInVariable(String bindInVariable) {
		this.bindInVariable = bindInVariable;
	}

	public String getControllerClass() {
		return controllerClass;
	}

	public void setControllerClass(String controllerClass) {
		this.controllerClass = controllerClass;
	}

	public String getListSize() {
		return listSize;
	}

	public void setListSize(String listSize) {
		this.listSize = listSize;
	}

	public String getCriterias() {
		return criterias;
	}

	public void setCriterias(String criterias) {
		this.criterias = criterias;
	}

	@Override
	public void doAfterCompose(org.zkoss.zk.ui.Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	
	@SuppressWarnings({ "unchecked" })
	public void refreshList(Combobox cmb, String search){
		Object temp = getControl();
		GenericControl controller = temp == null ? null : (GenericControl)temp;
		if(controller != null){
			controller.getData().put("searchKey", search);
			
			Integer listSize = getListSize() == null ? 5 : Integer.parseInt(getListSize()); 
			
			List<?> list = (List<?>) controller.doAction("searchByFilters", false).getList();
			list = list.size() > listSize ? list.subList(0, listSize) : list;
			BindingListModelList md = new BindingListModelList(list, true);
			cmb.setModel(md);
		}
	}
	
	public void initFilteredBinder(){
		binder.bindBean("entity", getBindInEntity());
		binder.addBinding(component, "selectedItem", "entity." + getBindInVariable());
		binder.addBinding(component, "value", "entity." + getBindInVariable(), new String[]{"self.onChange"}, "none", "load", null);
		loadBinder();
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericControl getControl() {
		GenericControl controller = null;
		if(getControllerClass() == null){
			controller = SpringFactory.getController("filteredSearchController", FilteredSearchController.class, ReflectionUtils.prepareDataForPersistence(this));
		} else {
			try {
				Class controllerClass = Class.forName(getControllerClass());
				String className = controllerClass.getSimpleName().substring(0, 1).toLowerCase() + controllerClass.getSimpleName().substring(1);
				controller = (GenericControl) SpringFactory.getController(className, controllerClass, ReflectionUtils.prepareDataForPersistence(this));
				if(!(controller instanceof IFilteredSearch)){
					controller = null;
					throw new IllegalArgumentException("A classe " + controllerClass.getSimpleName() + " deve implementar a interface IFilteredSearch!");
				}
			} catch (Exception e) {
				new ExceptionManager(e).treatException();
			}
		}
		return controller;
	}

	@Override
	public ICommonEntity getEntityObject() {
		return null;
	}
	
}
