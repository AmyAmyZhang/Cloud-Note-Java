package cn.tedu.note.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PointcutAspect {
	
	//@Before("bean(userSerrvice) || bean(noteService) || bean(notebookService)")
//	@Before("bean(*Service)")
//	@Before("within(cn.tedu.note.*.impl.*ServiceImpl)")
	//@Before("execution(* cn.tedu.note.service.UserService.login(..))")
//	@Before("execution(* cn.tedu.note.*.*Service.list*(..))")
 	public void test() {
		System.out.println("切入点测试");
	}
	
	
}
