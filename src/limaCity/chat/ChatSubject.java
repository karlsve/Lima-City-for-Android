package limaCity.chat;

public class ChatSubject {
    private String subject = "";
    private String from = "";

    public ChatSubject(String subject, String from) {
	this.subject = subject;
	this.from = from;
    }

    public String getSubject() {
	return subject;
    }

    public String getFrom() {
	return from;
    }

    public String getSubjectPrecompiled() {
	String subjectprecompiled = "";
	subjectprecompiled += this.from;
	subjectprecompiled += " hat das Thema ge�ndert auf ";
	subjectprecompiled += this.subject;
	return subjectprecompiled;
    }
}
