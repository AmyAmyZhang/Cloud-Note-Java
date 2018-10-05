package cn.tedu.note.dao;

import cn.tedu.note.entity.User;

public interface UserDao {
	
	User findUserByName(String name);
	User findUserById(String id);
	int addUser(User user);

}
