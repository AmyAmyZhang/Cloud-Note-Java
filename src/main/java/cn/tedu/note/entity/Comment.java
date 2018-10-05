package cn.tedu.note.entity;

import java.io.Serializable;

public class Comment implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5282836546048993534L;
	private Integer id;
	private String title;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return "Comment [id=" + id + ", title=" + title + "]";
	}
	public Comment(Integer id, String title) {
		super();
		this.id = id;
		this.title = title;
		
	}
	public Comment() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Comment other = (Comment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
