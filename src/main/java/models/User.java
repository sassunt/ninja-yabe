package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;

@Entity
public class User {

	@Id
	public Long id;

	public final String email;
	public final String password;
	public final String fullname;
	public boolean isAdmin;

	public User(String email, String password, String fullname) {
		this.email = email;
		this.password = password;
		this.fullname = fullname;
	}

	public static User connect(String email, String password) {
		return Ebean.find(User.class)
				.where()
				.eq("email", email)
				.eq("password", password)
				.findUnique();
	}
}
