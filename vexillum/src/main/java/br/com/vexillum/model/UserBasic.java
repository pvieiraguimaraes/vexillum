package br.com.vexillum.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.vexillum.model.annotations.SearchField;
import br.com.vexillum.model.annotations.Validate;
import br.com.vexillum.model.enums.Sexo;

@SuppressWarnings("serial")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
@Table(name = "users")
public class UserBasic extends CommonEntity {

	@Validate(notNull = true, min = 2, max = 200)
	@SearchField
	@Column(name = "name", unique = false, nullable = false, updatable = true, length = 200)
	private String name;

	@Validate(notNull = true, min = 5, max = 200)
	@SearchField
	@Column(name = "email", unique = true, nullable = false, updatable = false, length = 200)
	private String email;

	@Validate(notNull = true, min = 6, max = 50)
	@Column(name = "password", unique = false, nullable = false, updatable = true, length = 200)
	private String password;

	@Validate(notNull = true, future = true)
	@Column(name = "birthDate", unique = false, nullable = false, updatable = true, length = 50)
	private Date birthDate;

	@Validate(notNull = true)
	@Column(name = "sex", unique = false, nullable = false, updatable = true, length = 50)
	@Enumerated(EnumType.STRING)
	private Sexo sex;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_category", insertable = true, updatable = true)
	protected Category category;
	
	@Column(name = "verification_code", unique = false, nullable = true, updatable = true, length = 256)
	private String verificationCode;
	
	@Column(name = "active", nullable = false)
	private Boolean active;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Sexo getSex() {
		return sex;
	}

	public void setSex(Sexo sex) {
		this.sex = sex;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}