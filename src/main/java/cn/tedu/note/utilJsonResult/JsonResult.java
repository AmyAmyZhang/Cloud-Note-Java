package cn.tedu.note.utilJsonResult;

import java.io.Serializable;



public class JsonResult implements Serializable {
	
	private static final long serialVersionUID = 9210538083041903035L;
	
	public static final int SUCCESS = 0;
	public static final int ERR0R = 1;
	
	private int state;
	/** 错误消息 */
	private String message; 
	/** 返回正确的数据 */
	private Object data;
	
	public JsonResult() {
		
	}
	
	public JsonResult(String error) {
		state = ERR0R;
		this.message = error;
	}
	
	public JsonResult(Throwable e) {
		state = ERR0R ;
		message = e.getMessage();
	}
	
	public JsonResult(Object data) {
		state = SUCCESS;
		this.data = data;
	}
	
	public JsonResult(int state, Throwable e) {
		this.state = state;
		this.message = e.getMessage();
	}
	

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	

}
