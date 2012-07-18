package limaCity.forum;

public class PostItem {

    private String content = "";
    private String username = "";
    private String creationtime = "";
    private String posttype = "";
    private int postid = 0;
    private boolean deleted = false;
    private boolean userdeleted = false;
    
    public PostItem(String content, String username, String creationtime, String posttype, int postid, boolean userdeleted)
    {
	this.content = content;
	this.username = username;
	this.creationtime = creationtime;
	this.posttype = posttype;
	this.postid = postid;
	this.userdeleted = userdeleted;
    }
    
    public String getUserName() {
	return username;
    }

    public String getCreationTime() {
	return creationtime;
    }

    public int getPostId() {
	return postid;
    }

    public String getPostType() {
	return posttype;
    }

    public boolean isDeleted() {
	return deleted;
    }

    public String getContent() {
	return content;
    }

    public boolean isUserDeleted() {
	return userdeleted;
    }

}
