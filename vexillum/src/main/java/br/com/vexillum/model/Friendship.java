package br.com.vexillum.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.vexillum.model.annotations.ValidatorClass;

@SuppressWarnings("serial")
@ValidatorClass(validatorClass="br.com.vexillum.control.validator.FriendshipValidator")
@Entity
@Table(name="friendship")
public class Friendship extends CommonEntityActivated {

	public Friendship() {
		super();
		setActive(false);
		setDateFriendship(new Date());
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_owner", unique=false, nullable=false, updatable=false)
	private UserBasic owner;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_friend", unique=false, nullable=false, updatable=false)
	private UserBasic friend;
	
    @Column(name="date_of_friendship", unique=false, nullable=false, updatable=true)
	private Date dateFriendship;
    
	public UserBasic getOwner() {
		return owner;
	}

	public void setOwner(UserBasic owner) {
		this.owner = owner;
	}

	public UserBasic getFriend() {
		return friend;
	}

	public void setFriend(UserBasic friend) {
		this.friend = friend;
	}

	public Date getDateFriendship() {
		return dateFriendship;
	}

	public void setDateFriendship(Date dateFriendship) {
		this.dateFriendship = dateFriendship;
	}

	@Override
	public String toString() {
		return getFriend().getEmail();
	}
}
