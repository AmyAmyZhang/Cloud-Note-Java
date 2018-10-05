package cn.tedu.note.test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import cn.tedu.note.dao.NoteDao;
import cn.tedu.note.entity.Note;


public class NoteDaoTest extends BaseTest{
	
	NoteDao dao;
	
	@Before
	public void initDao() {
		dao = ctx.getBean("noteDao",NoteDao.class);
	}
	
	@Test
	//select cn_notebook_id from cn_note;
	public void testFindNotesByNotebookId() {
		String id = "fa8d3d9d-2de5-4cfe-845f-951041bcc461";
		List<Map<String, Object>> list = dao.findNotesByNotebookId(id);
		for(Map<String,Object> map : list) {
			System.out.println(map);
		}
		
	}
	
	@Test
	public void testGetNote() {
		String id = "07305c91-d9fa-420d-af09-c3ff209608ff";
		Note note = dao.findNoteById(id);
		System.out.println(note);
	}
	
	@Test
	public void testAddNote() {
		String id = UUID.randomUUID().toString();
		String userId = "48595f52-b22c-4485-9244-f4004255b972";
		String notebookId = "1db556b9-d1dc-4ed9-8274-45cf0afbe859";
		String statusId = "0";
		String typeId = "0";
		String title = "随笔";
		String body = "有时候有的人仔细想来真的很渣，但是感谢那时候对我的帮助！";
		long createtime = System.currentTimeMillis();
		long lastModifyTime = System.currentTimeMillis();
		Note note = new Note(id,notebookId,userId,statusId,typeId,title,body,createtime,lastModifyTime);
		System.out.println(note);
		dao.addNote(note);
	}
	
	@Test
	public void testUpdateNote() {
		Note note = new Note();
		String noteId = "20d13284-37f6-41d3-a3ce-005cbf36b5fd";
		
		note.setId(noteId);
		note.setTitle("I miss you");
		note.setBody("Fight on!");
		note.setLastModifyTime(System.currentTimeMillis());
		dao.updateNote(note);
		note = dao.findNoteById(noteId);
		System.out.println(note);	
	}
	
	@Test
	public void testShowTrash() {
		String userId = "48595f52-b22c-4485-9244-f4004255b972";
		
		Note note = dao.showTrashByUserId(userId);
		
		System.out.println(note);
	}
	
	@Test
	public void testDeleteNotes() {
		String id1 = "1ec185d6-554a-481b-b322-b562485bb8e8";
		String id2 = "003ec2a1-f975-4322-8e4d-dfd206d6ac0c";
		String id3 = "019cd9e1-b629-4d8d-afd7-2aa9e2d6afe0";
		int n = dao.deleteNotes(id1,id2,id3);
		System.out.println(n);
		
	}
	

}
