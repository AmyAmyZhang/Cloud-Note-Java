package cn.tedu.note.entity;

public class Stars {
	private String id;
	private String userId;
	private Integer stars;
	
	public Stars() {
		super();
	}
	
	public Stars(String id, String userId, Integer stars) {
		super();
		this.id = id;
		this.userId = userId;
		this.stars = stars;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	@Override
	public String toString() {
		return "Stars [id=" + id + ", userId=" + userId + ", stars=" + stars + "]";
	}
	
	

}
