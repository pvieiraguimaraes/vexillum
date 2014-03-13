package br.com.vexillum.vexhierarchia.view.composer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.annotations.TransientField;
import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.vexhierarchia.model.HierarchyEntity;
import br.com.vexillum.vexhierarchia.view.utils.HierarchyEntityTreeModel;
import br.com.vexillum.view.GenericComposer;

@SuppressWarnings({ "rawtypes", "serial" })
@org.springframework.stereotype.Component
@Scope("prototype")
public abstract class HierarchyEntityComposer<E extends HierarchyEntity<E>, G extends GenericControl<E>, H extends HierarchyEntityTreeModel<E>>
		extends GenericComposer {

	protected Properties messages;
	@TransientField
	protected H hierarchyTree;
	private Boolean showInactive = true;
	@Wire
	private Vbox cadBox;
	@Wire
	private Vbox boxOperations;
	@Wire
	private Tree treeStructure;
	@Wire
	private Grid gridHierarchyType;
	@Wire
	private Combobox fldComboTypes;
	private E entity;
	protected HierarchyEntityComposer parentComposer;

	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		messages = SpringFactory.getInstance().getBean("messagesProperties",
				Properties.class);
		setParentComposer( (HierarchyEntityComposer) arg.get("thisComposer"));
		loadBinder();
	}
	
	public abstract G getControl();
	public abstract E getTreeRoot();
	public abstract H getHierarchyTree();
	public abstract void setHierarchyTree(H hierarchyTree);

	public void callModalWindow(String page) {
		Map<String, Object> map = ReflectionUtils.prepareDataForPersistence(this);
		Component comp = Executions.createComponents(page, null, map);
		if (comp instanceof Window) {
			((Window) comp).doModal();
		}
	}
	
	
	public Return refreshTree() {
		this.setHierarchyTree(null);
		clearCadBox();
		return new Return(true);
	}

	public void refreshNodeAdition(E parent,
			E newNode) {
		try {
			getHierarchyTree().add(parent, newNode);
		} catch (NullPointerException e) {
		}
	}

	@SuppressWarnings({ "unchecked" })
	protected void showDeleteConfirmation(String message) {
		Messagebox.show(message, "Confirma��o", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
			public void onEvent(Event evt) throws InterruptedException {
				if (evt.getName().equals("onOk")) {
					doAction("deleteHierarchyEntity");
				}
			}

		});
	}

	@SuppressWarnings("unchecked")
	public Return removeOperation() {
		Return operationReturn;
		E entity = (E) getTreeStructure()
				.getSelectedItem().getValue();
		if (getHierarchyTree().isLeaf(entity)) {
			showDeleteConfirmation(getRemoveMessage(entity));
			operationReturn = new Return(true);
		} else {
			operationReturn = new Return(
					false,
					new Message(
							null,
							"O Item selicionado possui filhos na estrutura hier�rquica. Apenas itens sem filhos podem"
									+ " ser removidos."));
		}
		return operationReturn;
	}
	
	public String getRemoveMessage(E entity){
		return "Tem certeza que deseja remover o item?";
	}

	protected abstract Return deleteHierarchyEntity();

	public void refreshNodeDeletion(E node) {
		try {
			getHierarchyTree().removeNode(node);
		} catch (NullPointerException e) {
		}
	}

	public void refreshNodeModifications(E node) {
		try {
			getHierarchyTree().nodeUpdated(node);
		} catch (NullPointerException e) {

		}
	}

	public Return showCadBox() {
		if(cadBox == null){
			return new Return(false);
		}
		clearCadBox();
		cadBox.setVisible(true);
		boxOperations.setVisible(true);
		Treeitem treeItem = (Treeitem) getTreeStructure().getSelectedItem();
		HierarchyEntity entity = (HierarchyEntity) getTreeStructure()
				.getSelectedItem().getValue();
		HibernateUtils.initialize(entity);
		HashMap<String, Object> map = prepareDatesForPage(treeItem,entity);
		Executions.createComponents(getViewPage(),
					cadBox, map);
		return new Return(true);
	}
	
	public Return updateOperation()
	{
		clearCadBox();
		cadBox.setVisible(true);
		Treeitem treeItem = (Treeitem) getTreeStructure().getSelectedItem();
		HierarchyEntity entity = (HierarchyEntity) getTreeStructure()
				.getSelectedItem().getValue();
		HashMap<String, Object> map = prepareDatesForPage(treeItem,entity);
		Executions.createComponents(getSavePage(),
					cadBox, map);
		return new Return(true);
		
	}
	
	public Return addOperation()
	{
		restartCadBox();
		gridHierarchyType.setVisible(true);
		Treeitem treeItem = (Treeitem) getTreeStructure().getSelectedItem();
		HierarchyEntity entity = getEntityInTree();
		//HashMap<String, Object> map = prepareDatesForPage(treeItem,entity);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("patternEntity", entity);
		map.put("treeItem", treeItem);
		map.put("treeComposer", this);
		map.put("entityType", getHierarchyTypeSelected());
		Executions.createComponents(getSavePage(),
				cadBox, map);
		return new Return(true);
		
	}
	
	protected HierarchyEntity getEntityInTree(){
		return (HierarchyEntity) getTreeStructure().getSelectedItem().getValue();
	}
	
	public String getViewPage() {
		return "/paginas/estruturahierarquica/frmVisualizacao.zul";
	}

	public String getSavePage() {
		return "/paginas/estruturahierarquica/frmCadastro.zul";
	}
	
	public abstract Object getHierarchyTypeSelected();

	protected HashMap<String, Object> prepareDatesForPage(Treeitem treeItem, HierarchyEntity entity) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("entity", entity);
		map.put("treeItem", treeItem);
		map.put("treeComposer", this);
		map.put("entityType", getHierarchyTypeSelected());
		return map;
	}

	public void clearCadBox() {
		int numChildren = cadBox.getChildren().size();
		for (int i=0; i< numChildren ;i++) {
			Component child = cadBox.getChildren().get(i);
			if (child != boxOperations && child != gridHierarchyType)
				{
				cadBox.getChildren().remove(child);
				i--;
				numChildren--;
				}
			if (child == gridHierarchyType)
				gridHierarchyType.setVisible(false);
		}
		cadBox.setVisible(false);
	}
	
	public void restartCadBox(){
		clearCadBox();
		cadBox.setVisible(true);
	}
	
	public Return viewTypes()
	{
		restartCadBox();
		HierarchyEntity entity = (HierarchyEntity) getTreeStructure()
				.getSelectedItem().getValue();
		gridHierarchyType.setVisible(true);
		//fldComboTypes.setValue(null);
		//fldComboTypes.setText("Selecione");
		///Coloca para estar selecionado nehum item
		fldComboTypes.setSelectedIndex(-1);
		setHierarchyTypes(entity.getLevel());
		binder.saveComponent(fldComboTypes);
		fldComboTypes.focus();
		return new Return(true);
	}

	public void clearTreeSelection()
	{
		treeStructure = getTreeStructure();
		treeStructure.setSelectedItem(null);
		binder.saveComponent(treeStructure);
	}
	public Boolean getShowInactive() {
		return showInactive;
	}

	public void setShowInactive(Boolean showInactive) {
		this.showInactive = showInactive;
	}

	public abstract void setHierarchyTypes(Integer level);

	public Vbox getCadBox() {
		return cadBox;
	}

	public void setCadBox(Vbox cadBox) {
		this.cadBox = cadBox;
	}

	public Tree getTreeStructure() {
		return (Tree) getComponentById("treeStructure");
	}

	public void setTreeStructure(Tree treeStructure) {
		this.treeStructure = treeStructure;
	}

	public E getEntity() {
		return entity;
	}

	public void setEntity(E entity) {
		this.entity = entity;
	}

	public HierarchyEntityComposer getParentComposer() {
		return parentComposer;
	}

	public void setParentComposer(HierarchyEntityComposer parentComposer) {
		this.parentComposer = parentComposer;
	}

}
