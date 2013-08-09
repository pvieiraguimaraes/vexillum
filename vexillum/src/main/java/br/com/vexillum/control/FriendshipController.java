package br.com.vexillum.control;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.model.Friendship;
import br.com.vexillum.model.UserBasic;
import br.com.vexillum.util.Return;

@Service
@Scope("prototype")
public class FriendshipController extends GenericControl<Friendship> {

	public FriendshipController() {
		super(Friendship.class);
	}

	@Override
	public Return save() {
		if(data.get("owner") != null && data.get("friend") != null){
			Friendship f = (Friendship) (data.get("entity") == null ? new Friendship() : data.get("entity"));
			f.setOwner((UserBasic) data.get("owner"));
			f.setFriend((UserBasic) data.get("friend"));
			f.setAtivo(false);
			data.put("entity", f);
		}
		return super.save();
	}
	
	public Return searchAllFriends(){
		UserBasic user = (UserBasic) data.get("user");
		String sql = "FROM Friendship f "
					+ "WHERE (f.owner = '" + user.getId() + "' OR f.friend = '" + user.getId() + "') AND f.ativo = true";
		data.put("sql", sql);
		return searchByHQL();
	}
	
	public Return searchFriends(){
		UserBasic user = (UserBasic) data.get("user");
		String searchKey = (String) data.get("searchKey");
		
		String sql = "FROM Friendship f "
					+ "WHERE ((f.owner = '" + user.getId() + "' OR f.friend = '" + user.getId() + "') AND f.ativo = true) AND "
					+ "((f.owner.name like '" + searchKey + "%' OR f.friend.name like '" + searchKey + "%') OR "
					+ "(f.owner.email like '" + searchKey + "%' OR f.friend.email like '" + searchKey + "%') OR "
					+ "(f.owner.city like '" + searchKey + "%' OR f.friend.city like '" + searchKey + "%'))";
		data.put("sql", sql);
		Return ret = searchByHQL();
		ret.getList().remove(user);
		return ret;
	}
	
	public Return searchFriendRequests(){
		UserBasic user = (UserBasic) data.get("user");
		String sql = "FROM Friendship f "
					+ "WHERE f.friend = '" + user.getId() + "' AND f.ativo = false";
		data.put("sql", sql);
		return searchByHQL();
	}
	
	public Return searchPendentRequests(){
		UserBasic user = (UserBasic) data.get("user");
		String sql = "FROM Friendship f "
					+ "WHERE f.owner = '" + user.getId() + "' AND f.ativo = false";
		data.put("sql", sql);
		return searchByHQL();
	}
	
	public Return activeFriend(){
		Friendship f =  (Friendship) data.get("entity");
		f.setAtivo(true);
		f.setDateFriendship(new Date());
		return super.update();
	}

	@Override
	public Return delete() {
		if(data.get("owner") != null && data.get("friend") != null){
			UserBasic owner = (UserBasic) data.get("user");
			UserBasic friend = (UserBasic) data.get("user");
			String sql = "DELETE Friendship f "
						+ "WHERE (f.owner = '" + owner.getId() + "' AND f.friend = '" + friend.getId() + "') OR "
						+ "(f.owner = '" + friend.getId() + "' AND f.friend = '" + owner.getId() + "')";
			data.put("sql", sql);
			return searchByHQL(); 
		}
		return super.delete();
	}
	
	
	
}
