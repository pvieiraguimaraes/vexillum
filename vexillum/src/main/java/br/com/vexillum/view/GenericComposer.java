package br.com.vexillum.view;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.control.manager.ConfigurationManager;
import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.UserBasic;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;

@SuppressWarnings("rawtypes")
public class GenericComposer<U extends UserBasic> extends
		GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	protected AnnotateDataBinder binder;

	protected Component component;
	
	protected GenericComposer<?> thisComposer = this;
	
	protected U userLogged;
	
	protected static Properties messages;
	
	public U getUserLogged() {
		return userLogged;
	}

	public void setUserLogged(U userLogged) {
		this.userLogged = userLogged;
	}

	public GenericComposer<?> getThisComposer() {
		return thisComposer;
	}

	public void setThisComposer(GenericComposer<?> thisComposer) {
		this.thisComposer = thisComposer;
	}


	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.setAttribute("controller", this);
		this.component = comp;
		initBinder(component);
		setUserLogged((U) getUserInSession());
		try {
			messages =  SpringFactory.getInstance().getBean("messagesProperties", Properties.class);
		} catch (Exception e) {
			messages = null;
		}
	}

	protected AnnotateDataBinder initBinder(Component comp) {
		binder = new AnnotateDataBinder(comp);
		binder.setLoadOnSave(false);
		// binder.loadAll();
		return binder;
	}

	public void loadBinder() {
		binder.loadAll();
	}

	public boolean doAction(String action) {
		Return ret = null;

		// binder.saveAll();
		Class<?> rclass = this.getClass();
		try {
			Method m = rclass.getMethod(action, new Class[] {});
			ret = (Return) m.invoke(this, new Object[] {});
		} catch (Exception e) {
			ret = new ExceptionManager(e).treatException();
		}
		loadBinder();
		treatReturn(ret);
		return ret.isValid();
	}

	public void treatReturn(Return ret) {
		if (ret != null && ret.getMessages() != null
				&& !ret.getMessages().isEmpty()) {
			String type = ret.isValid() ? "info" : "error";
			showNotifications(ret.getMessages(), type);
		}
	}

	/**
	 * M�todo que procurar um Component do ZK baseado no ID do mesmo
	 * 
	 * @param comp
	 *            Componente pai, onde ser� procuradom o componente com o ID
	 *            correspondente
	 * @param id
	 *            ID do componente a ser procurado
	 * @return Retorna o componente com o ID procurado, ou retorna null caso n�o
	 *         seja encontrado
	 */
	public Component getComponentById(Component comp, String id) {
		if (comp.getId().equals(id)) {
			return comp;
		}
		List<Component> allComps = comp.getChildren();
		for (Component c : allComps) {
			Component aux = getComponentById(c, id);
			if (aux != null) {
				return aux;
			}
		}
		return null;
	}
	
	public Component getComponentById(String id) {
		return getComponentById(component, id);
	}

	public Component getComponentByType(Component comp, String type) {
		if (comp.getClass().getSimpleName().equalsIgnoreCase(type)) {
			return comp;
		}
		List<Component> allComps = comp.getChildren();
		for (Component c : allComps) {
			Component aux = getComponentByType(c, type);
			if (aux != null) {
				return aux;
			}
		}
		return null;
	}

	public Component getParent(Component comp, String compTarget) {
		Component parent = comp.getParent();
		if (parent == null) {
			return null;
		} else if (parent.getClass().getSimpleName()
				.equalsIgnoreCase(compTarget)) {
			return parent;
		} else {
			return this.getParent(parent, compTarget);
		}
	}

	public static String getProperty(String key) {
		return ConfigurationManager.getManager().getProperty(key,
				getUserInSession());
	}

	public static String getSystemProperties(String key) {
		Properties prop = SpringFactory.getInstance().getBean(
				"systemProperties", Properties.class);
		return prop.getKey(key);
	}
	
	public static String getMessage(String key){
		return messages.getKey(key);
	}

	public static boolean isLogged() {
		return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);

		// Session s = Sessions.getCurrent(false);
		// if(s == null || s.getAttribute("logged") == null ||
		// !s.getAttribute("logged").equals("true")){
		// //TODO Mudar para false quando o login for implementado
		// return true;
		// }
		// return true;
	}

	public void showNotifications(List<Message> messages, String type) {
		String geralMessage = "";
		for (Message message : messages) {
			if (message.getMessage() == null || message.getMessage().isEmpty())
				continue;
			if (message.getAssociated() == null
					|| message.getAssociated().equals("")) {
				geralMessage += message.getMessage() + "<br><br>";
			} else {
				Clients.showNotification(
						message.getMessage(),
						type,
						getComponentById(
								component,
								"fld"
										+ StringUtils.capitalize(message
												.getAssociated())),
						"end_center", 0, true);
			}
		}
		if (!geralMessage.equals("")) {
			Clients.showNotification(geralMessage, type, null, "middle_center",
					0, true);
		}
	}

	public void showNotification(String message, String type) {
		Clients.showNotification(message, type, null, "middle_center", 0, true);
	}

	public void showBusyServer(Component comp, String message) {
		String aux = message + "...";
		if (comp != null)
			Clients.showBusy(comp, aux);
		else
			Clients.showBusy(aux);
	}

	public static UserBasic getUserInSession() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth != null) {
			Object user = auth.getDetails();
			if (user instanceof UserBasic) {
				return (UserBasic) user;
			}
		}
		return null;
	}
	
	public void showWindowConfirmation(String message, EventListener<Event> event){
		Messagebox.show(message, "Confirmação", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, event);
	}

	/**
	 * Recebe um combobox e um listbox, pega o item selecionado do combobox e
	 * adicionar no model do listbox.
	 * 
	 * @param cb
	 * @param lb
	 */
	protected void addItemInListBoxFromCombobox(Combobox cb, Listbox lb) {
		BindingListModelList<Object> blm = (BindingListModelList<Object>) lb.getModel();
		if (cb.getSelectedItem() == null) {
			alert("Selecione um item primeiro!");
		}
		blm.add(cb.getSelectedItem().getValue());

		removeSelectedItem(cb);
	}

	/**
	 * remove o item selecionado do listbox e adiciona o item removido ao
	 * combobox
	 * 
	 * @param cb
	 * @param lb
	 */
	protected void removeSelectedItemFromListbox(Combobox cb, Listbox lb) {
		BindingListModelList<Object> modelDeterminadores = (BindingListModelList<Object>) lb.getModel();
		BindingListModelList<Object> modelDeterminadoresAvaliable = (BindingListModelList<Object>) cb.getModel();

		CommonEntity entity = (CommonEntity) lb.getSelectedItem().getValue();
		modelDeterminadores.remove(entity);
		modelDeterminadoresAvaliable.add(entity);
		
//		for (Object e : modelDeterminadores) {
//			CommonEntity entity = (CommonEntity) lb.getSelectedItem().getValue();
//			if (((CommonEntity) e).getId().equals(entity.getId())) {
//				modelDeterminadores.remove(entity);
//				modelDeterminadoresAvaliable.add(entity);
//				break;
//			}
//		}
	}

	/**
	 * @param cb
	 */
	protected void removeSelectedItem(Combobox cb) {
		BindingListModelList<Object> model = (BindingListModelList<Object>) cb.getModel();
		CommonEntity determinador = (CommonEntity) cb.getSelectedItem().getValue();
		model.remove(determinador);
//		for (Object e : model) {
//			CommonEntity e2 = (CommonEntity) e;
//			CommonEntity determinador = (CommonEntity) cb.getSelectedItem().getValue();
//			if (e2.getId().equals(determinador.getId())) {
//				model.remove(determinador);
//				break;
//			}
//		}
	}
	
	/**Método que exibe uma janela de confirmação para efetuar uma ação dada
	 * @param messageQuestion, mensagem exibida para confirmação
	 * @param nameAction, nome do método que será executado em caso afirmativo
	 */
	@SuppressWarnings({ "unchecked" })
	public void showActionConfirmation(String messageQuestion, final String nameAction) {
		EventListener evt = new EventListener() {
			public void onEvent(Event evt) throws InterruptedException {
				if (evt.getName().equals("onYes")) {
					doAction(nameAction);
				}
			}
		};
		Messagebox.show(messageQuestion, "Confirmação", Messagebox.YES | Messagebox.NO,
				Messagebox.QUESTION, evt);
	}
}
