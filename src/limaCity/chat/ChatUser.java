package limaCity.chat;

public class ChatUser {
	private String nick = "";
	private String jid = "";
	private String role = "";
	private String username = "";

	public ChatUser(String nick, String jid, String role, String username) {
		this.nick = nick;
		this.jid = jid;
		this.role = role;
		this.username = username;
	}

	public String getNick() {
		return nick;
	}

	public String getJid() {
		return jid;
	}

	public String getRole() {
		return role;
	}

	public String getUsername() {
		return username;
	}
}
