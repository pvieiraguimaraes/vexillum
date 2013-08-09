package br.com.vexillum.view;

import br.com.vexillum.control.FriendshipController;
import br.com.vexillum.model.Friendship;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.SpringFactory;

@SuppressWarnings("serial")
public class FriendshipComposer extends CRUDComposer<Friendship, FriendshipController> {

	@Override
	public FriendshipController getControl() {
		return SpringFactory.getController("friendshipController", FriendshipController.class, ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public Friendship getEntityObject() {
		return new Friendship();
	}

}
