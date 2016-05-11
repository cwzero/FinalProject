package edu.nwtc.chat;

import java.util.Arrays;

public class User {
	protected int id = -1;
	protected String username;
	protected char[] password;

	public User() {

	}

	public User(String username, char[] password) {
		this.username = username;
		this.password = password;
	}
	
	public User(int id, String username, char[] password) {
		this(username, password);
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}
	
	public void setPassword(String password) {
		this.password = password.toCharArray();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toJson() {
		String json = "\t{\n\t\t";

		json += "\"username\" : \"" + username + "\",\n\t\t";
		json += "\"password\" : \"" + new String(password) + "\"\n";

		json += "\t}";
		return json;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(password);
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (!Arrays.equals(password, other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
