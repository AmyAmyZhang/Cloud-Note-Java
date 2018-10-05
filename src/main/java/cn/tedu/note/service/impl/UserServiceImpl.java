package cn.tedu.note.service.impl;

import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tedu.note.dao.UserDao;
import cn.tedu.note.entity.User;
import cn.tedu.note.service.PasswordException;
import cn.tedu.note.service.UserNameException;
import cn.tedu.note.service.UserNotFoundException;
import cn.tedu.note.service.UserService;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{
	
	
	@Value("#{jdbc.salt}")
	private String salt;
	
	@Resource
	private UserDao userDao;
	
	public User login(String name, String password) 
			throws UserNotFoundException, 
			PasswordException {
		
//		String s = null;
//		s.charAt(0);
		
		System.out.println("login");
		
		if (password == null || password.trim().isEmpty()) {
			throw new PasswordException("密码为空");
		}
		if (name == null || name.trim().isEmpty()) {
			throw new PasswordException("用户名为空");
		}
		User user = userDao.findUserByName(name.trim());
		if (user == null) {
			throw new UserNotFoundException("用户名不存在");
		} 
		String salt="I'll be fine!";
		String pwd = DigestUtils.md5Hex(salt+password.trim());
		if (pwd.equals(user.getPassword())) {
			return user;
		} 
		throw new PasswordException("密码错误");	
	}

	// UserServiceImpl
	
	public User regist(String name, 
			String nick, String password, 
			String confirm)
			throws UserNameException, PasswordException {
		String id = UUID.randomUUID().toString();
		//检查name,不能重复
		if(name == null || name.trim().isEmpty()) {
			throw new UserNameException("不能空");
		}
		User one = userDao.findUserByName(name);
		if (one != null) {
			throw new UserNameException("用户名已被占用");
		}
		//检查密码
		if (password == null || password.trim().isEmpty() ) {
			throw new PasswordException("密码不能为空");
		}
		if (! password.equals(confirm)) {
			throw new PasswordException("确认密码");
		}
		//检查nick(nick为空的时候，将用户名当作nick，降低异常，能处理的就处理)
		if (nick == null || nick.trim().isEmpty()) {
			nick = name;
		}
		String token = "";
		
		password = DigestUtils.md5Hex(salt + password);
		User user = new User(
				id,name,password,token,nick);
		int n = userDao.addUser(user);
		if (n!=1) {
			throw new RuntimeException("添加失败");
		}
		return user;
	}
	
}
