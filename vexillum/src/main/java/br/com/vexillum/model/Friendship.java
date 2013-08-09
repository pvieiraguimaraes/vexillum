package br.com.vexillum.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
public class Friendship extends CommonEntity {

	public Friendship() {
		super();
		this.ativo = false;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_owner", unique=false, nullable=false, updatable=false)
	private UserBasic owner;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_friend", unique=false, nullable=false, updatable=false)
	private UserBasic friend;
	
    @Column(name="date_of_friendship", unique=false, nullable=false, updatable=false)
	private Date dateFriendship;
    
    @Column(name="date_of_friendship", unique=false, nullable=false, updatable=true)
	private Boolean ativo;

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

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
}
