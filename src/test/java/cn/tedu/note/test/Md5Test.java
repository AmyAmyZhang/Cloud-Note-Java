 package cn.tedu.note.test;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

public class Md5Test {

	@Test
	public void testMd5() {
		String str = "123456";
		String md5 = DigestUtils.md5Hex(str);
		System.out.println(md5);
		//e10adc3949ba59abbe56e057f20f883e
		//加盐摘要
		String salt = "I'll be fine!";
		md5 = DigestUtils.md5Hex(salt+str);
		System.out.println(md5);
		// update cn_user
		//set cn_user_password='cef9a4e3dbf864698473dcb886bb6df1'
		//where cn_user_name='demo';
		
	}
}
