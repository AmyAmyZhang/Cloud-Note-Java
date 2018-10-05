package cn.tedu.note.test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import cn.tedu.note.entity.Note;
import cn.tedu.note.service.NoteService;

public class NoteServiceTest extends BaseTest{
	
	NoteService service;
	
	@Before
	public void initService() {
		service = ctx.getBean("noteService", NoteService.class);
	}
	
	@Test
	public void TestListNotes() {
		String id = "fa8d3d9d-2de5-4cfe-845f-951041bcc461";
		List<Map<String,Object>> list = service.listNotes(id);
		for (Map<String,Object> map : list) {
			System.out.println(map);
		}	
	}
	
	
	@Test 
	public void TestGetNote() {
		String noteId = "07305c91-d9fa-420d-af09-c3ff209608ff";
		Note note = service.getNote(noteId);
		System.out.println(note);
	}
	
	@Test
	public void TestAddNote() {
//		String id = UUID.randomUUID().toString();
//		String statusId = "0";
//		String typeId = "";
//		long time = System.currentTimeMillis();
//		Note note = new Note(id, note,bookId, userId, 
//				statusId, typeId, title, body, time,time);
//		int n = noteDao.
		String userId = "48595f52-b22c-4485-9244-f4004255b972";
		String notebookId = "1db556b9-d1dc-4ed9-8274-45cf0afbe859";
		Note note = service.addNote(userId, notebookId, "peace and love!");
		System.out.println(note.getId());
	}
	
//	@Test
//	public void testNote() {
//		Note note = new Note();
//		note.setCreateTime(System.currentTimeMillis());
//		System.out.println(note);
//	}
	
	@Test 
	public void testUpdateNote() {
		String noteId = "2013b419-4439-4109-b4db-c2cc1b58a1b9";
		String title = "Test";
		String body = "It's a nice day!";
		boolean b = service.update(noteId, title, body);
		Note note = service.getNote(noteId);
		System.out.println(b);
		System.out.println(note);	
	}
	
	@Test
	public void testMoveNote() {
		String noteId = "003ec2a1-f975-4322-8e4d-dfd206d6ac0c";
		String notebookId = "fa8d3d9d-2de5-4cfe-845f-951041bcc461";
		Note note = service.getNote(noteId);
		System.out.println(note);
		boolean b = service.moveNote(noteId, notebookId);
		note = service.getNote(noteId);
		System.out.println(note);
	}
	
//	@Test
//	public void testDeleteNotes() {
//		
//		String id1 = "3febebb3-a1b7-45ac-83ba-50cdb41e5fc1";
//		String id2 = "9187ffd3-4c1e-4768-9f2f-c600e835b823";
//		String id3 = "ebd65da6-3f90-45f9-b045-782928a5e2c0";
//		String id4 = "fed920a0-573c-46c8-ae4e-368397846efd";
//		
//		int n = service.deleteNotes(id1, id2, id3, id4);
//		System.out.println(n);
//	}
	
	@Test
	public void testAddStars() {
		String userId="333c6d0b-e4a2-4596-9902-a5d98c2f665a";
		boolean b = service.addStars(userId,5);
		System.out.println(b);
		b = service.addStars(userId,6);
		System.out.println(b);
	}
	
	
}
