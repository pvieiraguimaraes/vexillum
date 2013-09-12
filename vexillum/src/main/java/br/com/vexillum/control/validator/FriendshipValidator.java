package br.com.vexillum.control.validator;

import java.util.HashMap;
import java.util.Map;

import br.com.vexillum.control.FriendshipController;
import br.com.vexillum.model.UserBasic;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;

public class FriendshipValidator extends Validator {

	public FriendshipValidator(Map<String, Object> mapData) {
		super(mapData);
	}
	
	@Override
	public Return validateSave() {
		Return ret = isFriend();
		return ret;
	}
	
	private Return isFriend(){
		Return ret = new Return(true);
		UserBasic owner = (UserBasic) mapData.get("owner");
		UserBasic friend = (UserBasic) mapData.get("friend");
		String sql = "FROM Friendship f "
					+ "WHERE (f.owner = '" + owner.getId() + "' AND f.friend = '" + friend.getId() + "') OR "
					+ "(f.owner = '" + friend.getId() + "' AND f.friend = '" + owner.getId() + "')";
		mapData.put("sql", sql);
		
		FriendshipController controller = SpringFactory.getController("friendshipController", FriendshipController.class, ((HashMap<String, Object>)mapData));
		if(!controller.searchByHQL().getList().isEmpty()){
			ret.concat(creatReturn(null, getValidationMessage(null, "isfriend", true)));
		}
		return ret;
	}

}
