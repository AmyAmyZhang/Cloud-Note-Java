package cn.tedu.note.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tedu.note.utilJsonResult.JsonResult;

public abstract class AbstractController {

	public AbstractController() {
		super();
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public JsonResult handlerException(Exception e) {
		e.printStackTrace();
		return new JsonResult(e);
	}

}