package br.com.vexillum.view;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.control.FriendshipController;
import br.com.vexillum.control.GenericControl;
import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.model.Friendship;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.model.UserBasic;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;

@SuppressWarnings("rawtypes")
public abstract class CRUDComposer<E extends ICommonEntity, G extends GenericControl<E>>
		extends GenericComposer {

	private static final long serialVersionUID = 1L;

	protected E entity;

	protected Properties messages;

	@Wire
	private Window frmCrudBasico;

	@Wire
	protected Listbox resultPesquisa;

	protected List<E> listEntity;
	protected E selectedEntity;

	protected Boolean update = false;

	public Boolean getUpdate() {
		return update;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}

	public E getEntity() {
		return entity;
	}

	public void setEntity(E entidade) {
		this.entity = entidade;
	}

	public List<E> getListEntity() {
		return listEntity;
	}

	public void setListEntity(List<E> listEntity) {
		this.listEntity = listEntity;
	}

	public E getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(E selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

	protected void initEntity() {
		setEntity(getEntityObject());
	}

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		messages = SpringFactory.getInstance().getBean("messagesProperties",
				Properties.class);
		initEntity();
	}

	public Return saveEntity() {
		return saveEntity(true);
	}
	
	public Return saveEntity(Boolean transactionControlled) {
		if (getEntity().getId() != null && getEntity().getId() != 0) {
			return getControl().doAction("update", transactionControlled);
		}
		return getControl().doAction("save", transactionControlled);
	}

	public Return deactivateEntity() {
		return getControl().doAction("deactivate");
	}

	@SuppressWarnings("unchecked")
	public Return searchEntitys() {
		binder.saveAll();
		Return ret = getControl().doAction("searchByCriteria", false);
		setListEntity((List<E>) ret.getList());
		binder.loadAll();
		return ret;
	}

	@SuppressWarnings("unchecked")
	public Return searchByHQL() {
		Return ret = getControl().doAction("searchByHQL", false);
		setListEntity((List<E>) ret.getList());
		binder.loadAll();
		return ret;
	}

	@SuppressWarnings("unchecked")
	public Return listAll() {
		Return ret = getControl().doAction("listAll", false);
		setListEntity((List<E>) ret.getList());
		return ret;
	}

	public Return deleteEntity() {
		binder.saveAll();
		setEntity(selectedEntity);
		return getControl().doAction("delete");
	}

	@SuppressWarnings({ "unchecked" })
	protected void showDeleteConfirmation(String message) {
		Messagebox.show(message, "Confirmação", Messagebox.OK
				| Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
			public void onEvent(Event evt) throws InterruptedException {
				if (evt.getName().equals("onOK")) {
					deleteUser();
				}
			}
		});
	}

	protected void deleteUser() {
		if (doAction("deleteEntity")) {
			getListEntity().remove(getSelectedEntity());
			setSelectedEntity(null);
		}
	}

	public void alterarEntidade() {
		binder.saveAll();
		setEntity(selectedEntity);
		showCrudForm();
	}

	public void novaEntidade() {
		showCrudForm();
	}

	public static Boolean haveIdOnRequest() {
		String id = Executions.getCurrent().getParameter("id");
		return ((id != null && Integer.parseInt(id) >= 0));
	}

	protected void clearForm() {
		entity = getEntityObject();
	}

	public void callModalWindow(String page) {
		Map<String, Object> map = ReflectionUtils
				.prepareDataForPersistence(this);

		Component comp = Executions.createComponents(page, null, map);

		if (comp instanceof Window) {
			((Window) comp).doModal();
		}
	}

	public Return validateSelectedEntity() {
		Return ret = new Return(true);
		if (getSelectedEntity() == null) {
			ret = new Return(false, getSelectedErrorMessage(entity.getClass().getSimpleName()));
		}
		return ret;
	}
	
	protected Message getSelectedErrorMessage(String className){
		Message mes = null;
		String key = (className + "_" + "selectedentity" + "_" + false).toLowerCase();
		String message = messages.getKey(key);
		if(message == null){
			key = ("selectedentity" + "_" + false).toLowerCase();
			message = messages.getKey(key);
		}
		mes = new Message(null, message);
		return mes;
	}

	public void disableComponentsNoUpdatables(Component comp) {
		try {
			List<Field> comps = ReflectionUtils.getNonUpdatableFields(entity);
			for (Iterator<Field> i = comps.iterator(); i.hasNext();) {
				Field f = i.next();
				Component campo = getComponentById(comp,
						"fld" + StringUtils.capitalize(f.getName()));
				if (campo != null) {
					if (campo instanceof InputElement) {
						((InputElement) campo).setDisabled(true);
					}
				}
			}
		} catch (Exception e) {
			new ExceptionManager(e).treatException();
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void showDeactivateConfirmation(String message) {
		Return ret = validateSelectedEntity();

		EventListener evt = new EventListener() {
			public void onEvent(Event evt) throws InterruptedException {
				if (evt.getName().equals("onOK")) {
					onOkDeactivationEvent();
				}
			}
		};

		if (ret.isValid()) {
			showWindowConfirmation(message, evt);
		} else {
			treatReturn(ret);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void showDeactivateConfirmation(String message, EventListener event) {
		Return ret = validateSelectedEntity();

		if (ret.isValid()) {
			showWindowConfirmation(message, event);
		} else {
			treatReturn(ret);
		}
	}

	protected void onOkDeactivationEvent() {
		setEntity(getSelectedEntity());
		doAction("deactivateEntity");
		setSelectedEntity(null);
		initEntity();
		loadBinder();
	}
	
	protected Return saveFriendship(UserBasic owner, UserBasic friend) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("entity", new Friendship());
		data.put("owner", owner);
		data.put("friend", friend);
		
		FriendshipController controller = SpringFactory.getController("friendshipController", FriendshipController.class, data);
		return controller.doAction("save");
	}

	/**
	 * @return o controle espec�fico para cada composer
	 */
	public abstract G getControl();

	/**
	 * Dever� ser implementado o composer para retornar o objeto de dominio o
	 * qual ele corresponde.
	 * 
	 * @return classe que extends de CommonEntity
	 */
	public abstract E getEntityObject();

	/**
	 * @return o formul�rio para cadastrar e alterar uma entidade para que ela
	 *         retorne um formul�rio diferente basta sobrescrever este m�todo
	 */
	public Window getCrudFrm() {
		return frmCrudBasico;
	}

	public void showCrudForm() {
		getCrudFrm().setMode(Window.MODAL);
		getCrudFrm().setVisible(true);
		getCrudFrm().doModal();
	}

	public void hideCrudForm() {
		getCrudFrm().setVisible(false);
	}

}
