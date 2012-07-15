package limaCity.forum;

public class ForumItem {

    private String url, name, description, moderators;

    public ForumItem(String url, String name, String description,
	    String moderators) {
	this.url = url;
	this.name = name;
	this.description = description;
	this.moderators = moderators;
    }

    public String getUrl() {
	return url;
    }

    public String getName() {
	return name;
    }

    public String getDescription() {
	return description;
    }

    public String getModerators() {
	return moderators;
    }
}
