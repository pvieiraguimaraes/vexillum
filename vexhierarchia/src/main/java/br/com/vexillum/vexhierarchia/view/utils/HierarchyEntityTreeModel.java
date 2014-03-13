package br.com.vexillum.vexhierarchia.view.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.event.TreeDataEvent;
import org.zkoss.zul.ext.TreeSelectableModel;

import br.com.vexillum.vexhierarchia.model.HierarchyEntity;

public abstract class HierarchyEntityTreeModel<E extends HierarchyEntity<E>>
		extends AbstractTreeModel<E> implements TreeSelectableModel {

	private static final long serialVersionUID = 7275066525411104204L;

	protected Map<E, List<E>> mapChildren = new HashMap<E, List<E>>();

	public HierarchyEntityTreeModel(E root) {
		super(root);
		mapChildren.put(root, root.getChildrens());
	}

	public boolean isLeaf(E node) {
		// System.out.println("is Leaf for node " + node.getName() + "?  " +
		// Boolean.toString(getChildCount(node) == 0) + ".See size == " +
		// getChildCount(node));
		return getChildCount(node) == 0;
		// return node.getChildrens() == null || node.getChildrens().size() ==
		// 0;
	}

	public E getChild(E parent, int index) {

		if (mapChildren.get(parent) == null) {
			loadParent(parent);
		}
		return mapChildren.get(parent).get(index);
	}

	public int getChildCount(E parent) {
		if (mapChildren.get(parent) == null) {
			loadParent(parent);
		}
		return mapChildren.get(parent).size();
	}

	public void loadParent(E parent) {
		mapChildren.put(parent, getChildrens(parent));
	}

	public abstract List<E> getChildrens(E parent);

	public void add(E parent, E newNodes) {
		Integer length;
		isLeaf(parent); // /Apenas para garantir de que o n� foi carregado
		// List<E> children = mapChildren.get(parent);
		length = getChildCount(parent);
		/*
		 * children.add(newNodes); parent.setChildrens(children);
		 */
		loadParent(parent);
		// mapChildren.put(parent, parent.getChildrens());
		// mapChildren.remove(parent);
		fireEvent(TreeDataEvent.STRUCTURE_CHANGED, getPath(parent), 0,
				length - 1);
	}

	public void nodeUpdated(E node) {
		E parent = node.getPattern();
		if (parent != null) {
			Integer length = getChildCount(parent);
			loadParent(parent);
			fireEvent(TreeDataEvent.STRUCTURE_CHANGED, getPath(parent), 0,
					length - 1);
			/*
			 * Integer childrenIndex = parent.getChildrens().indexOf(node);
			 * loadParent(parent); fireEvent(TreeDataEvent.CONTENTS_CHANGED,
			 * getPath(parent), childrenIndex, childrenIndex);
			 */
		} else {
			fireEvent(TreeDataEvent.CONTENTS_CHANGED, getPath(node), 1, 1);
		}
	}

	public void removeNode(E node) {
		E parent = node.getPattern();
		if (parent != null) {
			// Integer childrenIndex = parent.getChildrens().indexOf(node);
			mapChildren.get(node.getPattern()).remove(node);
			mapChildren.remove(node);
			Integer lenght = getChildCount(parent);
			fireEvent(TreeDataEvent.INTERVAL_REMOVED, getPath(parent), 0,
					lenght);
		} else {
			mapChildren.remove(node);
			fireEvent(TreeDataEvent.INTERVAL_REMOVED, getPath(node), 1, 1);
		}
	}

}
