package cn.tedu.note.test;

import org.junit.Before;
import org.junit.Test;

import cn.tedu.note.entity.User;
import cn.tedu.note.service.PasswordException;
import cn.tedu.note.service.UserNameException;
import cn.tedu.note.service.UserNotFoundException;
import cn.tedu.note.service.UserService;

/**
 * 5 测试
 * 测试登录方法
 * @author zhangyiyu
 *
 */
public class UserServiceTest extends BaseTest {

	private UserService service;
	@Before
	public void initService() {
		service = ctx.getBean("userService",UserService.class);
	}
	@Test
	public void testLogin() throws UserNotFoundException, PasswordException {
		String name = "demo";
		String password = "123456";
		User user = service.login(name, password);
		System.out.println(user);
		
	}
	
	@Test
	public void testRegist() throws UserNameException, PasswordException {
		User user = service.regist("Andy","Andy","123456","123456");
		System.out.println(user);
	}
	

}
