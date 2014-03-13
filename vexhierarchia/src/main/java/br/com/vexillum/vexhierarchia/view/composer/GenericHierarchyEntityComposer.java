package br.com.vexillum.vexhierarchia.view.composer;

import org.zkoss.zk.ui.Component;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.Return;
import br.com.vexillum.vexhierarchia.model.HierarchyEntity;
import br.com.vexillum.view.CRUDComposer;


@SuppressWarnings("rawtypes")
public abstract class GenericHierarchyEntityComposer<E extends HierarchyEntity, G extends GenericControl<E>>
		extends CRUDComposer<E , G> {


	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		E entity = (E) arg.get("entity");
		if (entity != null) {
			this.setUpdate(true);
			this.setEntity((E) entity.cloneEntity());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Return saveEntity() {
		HierarchyEntity pattern = (HierarchyEntity) this.arg
				.get("patternEntity");
		if (pattern != null)
			getEntity().setPattern(pattern);
		Return ret = super.saveEntity();
		HierarchyEntityComposer treeComposer = (HierarchyEntityComposer) this.arg
				.get("treeComposer");
		if (ret.isValid()) {
			if (!getUpdate()) {
				treeComposer.refreshNodeAdition(pattern, getEntity());
			}
			if (getUpdate()) {
				treeComposer.refreshNodeModifications(getEntity());
			}

			treeComposer.clearCadBox();
			treeComposer.clearTreeSelection();
		} else {
			clearForm();
		}
		loadBinder();
		return ret;
	}
}
