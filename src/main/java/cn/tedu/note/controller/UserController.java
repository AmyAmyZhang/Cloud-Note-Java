package cn.tedu.note.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.tedu.note.entity.User;
import cn.tedu.note.service.PasswordException;
import cn.tedu.note.service.UserNameException;
import cn.tedu.note.service.UserNotFoundException;
import cn.tedu.note.service.UserService;
import cn.tedu.note.utilJsonResult.JsonResult;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {
	
	@Resource
	private UserService userService;
	
	@RequestMapping("/login.do")
	@ResponseBody
	public Object login (String name, String password, HttpSession session) throws UserNotFoundException, PasswordException {
		
		User user = userService.login(name, password);
		//登录成功的时候,将user信息保存到session
		//用于在过滤器中检查登录情况
		session.setAttribute("loginUser", user);
		return new JsonResult(user);
	}
		
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseBody
	public JsonResult handlerUserNotFound(UserNotFoundException e) {
		e.printStackTrace();
		return new JsonResult(2,e);
	}
	
	@ExceptionHandler(PasswordException.class)
	@ResponseBody
	public JsonResult handlerPasswordExcpetion(PasswordException e) {
		e.printStackTrace();
		return new JsonResult(3,e);
	}
	
	@ExceptionHandler(UserNameException.class)
	@ResponseBody
	public JsonResult handlerUserName(UserNameException e) {
		e.printStackTrace();
		return new JsonResult(4,e);
	}
	
	
		
		@RequestMapping("/regist.do")
		@ResponseBody
		public JsonResult regist(String name, String nick, String password,
				String confirm) throws UserNameException, PasswordException {
			User user = userService.regist(name, nick, password, confirm);
			return new JsonResult(user);
		}
		
		/*
		 * @ResponseBody 注解会自动处理控制返回值
		 * 1.如果是JavaBean(数组，集合)返回Bean
		 * 2.如果是Byte数字，则将byte数组直接装入
		 * 响应消息的body中 
		 */
		//produces="image/png" 用于设置contentType
		@RequestMapping(value="/image.do",
				produces="image/png")
		@ResponseBody
		public byte[] image() throws Exception{
			return createPng();			
		}
		
		@RequestMapping(value="/downloadimg.do",
				produces="application/octet-stream")
		@ResponseBody
		public byte[] downloadimg(
				HttpServletResponse res) 
			throws IOException{
			//Content-Disposition: attachment;filename
			res.setHeader("Content-Disposition", 
					"attachment; filename=\"demo.png\"");
			
			return createPng();
		}
		
		@RequestMapping(value="/excel.do",
				produces="application/octet-stream")
		@ResponseBody
		public byte[] excel(
				HttpServletResponse res) 
			throws IOException{
			//Content-Disposition: attachment;filename
			res.setHeader("Content-Disposition", 
				"attachment; filename=\"demo.xls\"");
			return createExcel();
		}
		
		private byte[] createExcel() 
			throws IOException{
			//创建工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			//创建工作表
			HSSFSheet sheet = workbook.createSheet("Demo");
			//在工作表中创建数据行
			HSSFRow row = sheet.createRow(0);
			//创建行中的格子
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("Hello World");
			//将Excel文件保存为byte数组
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			return out.toByteArray();	
		}
		
		
		/**
		 * 创建一张图片,并且编码为png格式,返回
		 * 编码以后的数据
		 * @return
		 * @throws IOException 
		 */
		private byte[] createPng() throws IOException {
			BufferedImage img = 
					new BufferedImage(200,80,
					BufferedImage.TYPE_3BYTE_BGR);
			//在图片上绘制内容
			img.setRGB(100, 40, 0xffffff);
			//将图片编码为png
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(img, "png", out);
			out.close();
			byte[] png = out.toByteArray();
			return png;	
		}
		
		@RequestMapping("/upload.do")
		@ResponseBody
		public JsonResult upload(
				MultipartFile userfile1,
				MultipartFile userfile2) throws IllegalStateException, IOException {
			//Spring MVC中可以利用multipartFile接收上传的文件！
			//文件中一切数据都可以从multipartFile 对象中找到
			
			//获取上传的原始文件名
			String file1 = userfile1.getOriginalFilename();
			String file2 = userfile2.getOriginalFilename();
			
			System.out.println(file1);
			System.out.println(file2);
			//保存文件的3种方法:
			//1. transferTo(目标文件)
			//将文件直接保存到目标文件，可以处理大文件
			//2. userfile1.getBytes() 获取文件的全部
			//将文件全部读取到内存,适合处理小文件
			//3.userfile.getInputStream()
			//
			
			//保存的目标文件夹:/home/soft01/demo 有待考证
			File dir  = new File("/home/soft01/demo");
			dir.mkdir();
			
			File f1  = new File(dir, file1);
			File f2 = new File(dir, file2);
			//第一种保存文件
			userfile1.transferTo(f1);
			userfile2.transferTo(f2);
			//第三种 利用流复制数据
			InputStream in1 = userfile1.getInputStream();
			FileOutputStream out1 = new FileOutputStream(f1);
			int b;
			while((b=in1.read())!=-1) {
				out1.write(b);
			}
			in1.close();
			out1.close();
			
			InputStream in2 = userfile1.getInputStream();
			FileOutputStream out2 = new FileOutputStream(f1);
			byte[] buf = new byte[8*1024];
			int n;
			while((n=in2.read(buf))!=-1) {
				out2.write(buf,0,n);
			}
			in2.close();
			out2.close();
			
			//获取上传文件的流，适合处理大文件
			return new JsonResult(true);
		}
		
}
		
		//这样写太复杂了，try-catch太多了
//		try {
//			User user = userService.login(name, password);
//			return new JsonResult(user);
//			
//		} catch(PasswordException e) {
//			e.printStackTrace();
//			return new JsonResult(e);
//		} catch(UserNotFoundException e) {
//			e.printStackTrace();
//			return new JsonResult(e);
//		} //数据库的访问永远不安全，通信断了或者线被老鼠咬断了
//		catch (Exception e) {
//			e.printStackTrace();
//			return new JsonResult(e);
//		}
