package limaCity.forum;

public class BoardItem {
	private String url, name, views, replies, author, date;

	public BoardItem(String url, String name, String views, String replies,
			String author, String date) {
		this.url = url;
		this.name = name;
		this.views = views;
		this.replies = replies;
		this.author = author;
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	public String getViews() {
		return views;
	}

	public String getReplies() {
		return replies;
	}

	public String getAuthor() {
		return author;
	}

	public String getDate() {
		return date;
	}
}
