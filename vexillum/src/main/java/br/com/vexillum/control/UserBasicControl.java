package br.com.vexillum.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.model.Category;
import br.com.vexillum.model.UserBasic;
import br.com.vexillum.util.Return;

@Service
@Scope("prototype")
public class UserBasicControl<U extends UserBasic> extends GenericControl<U> {
	
	public UserBasicControl() {
		super(null);
	}
	
	public UserBasicControl(Class<U> classEntity) {
		super(classEntity);
	}

	@SuppressWarnings("unchecked")
	public U getUserByMail(String email) {
		Return retUser = new Return(true);
		UserBasic user = new UserBasic();
		user.setEmail(email);
		entity = (U) user;
		retUser = searchByCriteria();
		if (retUser.isValid() && !retUser.getList().isEmpty())
			return (U) retUser.getList().get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public U getUser(String name, String password) {
		UserBasic user = new UserBasic();
		user = getUserByMail(name);
		if (user != null && user.getPassword().equals(password) && user.getActive())
			return (U) user;
		return null;

	}

	public Category getCategoryUser(UserBasic user) {
		String hql = "from Category where id in (select u.category.id from UserBasic as u where id ='"
				+ user.getId() + "')";
		data.put("sql", hql);
		Return retCategory = searchByHQL();
		return (Category) retCategory.getList().get(0);
	}

}
