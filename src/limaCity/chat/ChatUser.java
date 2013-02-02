package limaCity.chat;

public class ChatUser {
	private String nick = "";
	private String jid = "";
	private String role = "";
	private String username = "";

	public ChatUser(String nick, String jid, String role) {
		this.nick = nick;
		this.jid = jid;
		this.role = role;
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
