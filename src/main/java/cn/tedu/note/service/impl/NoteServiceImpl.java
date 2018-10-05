package cn.tedu.note.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import cn.tedu.note.dao.NoteDao;
import cn.tedu.note.dao.NotebookDao;
import cn.tedu.note.dao.StarsDao;
import cn.tedu.note.dao.UserDao;
import cn.tedu.note.entity.Note;
import cn.tedu.note.entity.Stars;
import cn.tedu.note.entity.User;
import cn.tedu.note.service.NoteNotFoundException;
import cn.tedu.note.service.NoteService;
import cn.tedu.note.service.NotebookNotFoundException;
import cn.tedu.note.service.UserNotFoundException;

@Service("noteService")
public class NoteServiceImpl implements NoteService {

	@Resource
	private UserDao userDao;
	@Resource
	private NoteDao noteDao;
	@Resource
	private NotebookDao notebookDao;
	@Resource
	private StarsDao starsDao;
	
	public List<Map<String, Object>> listNotes(
				String notebookId) 
				throws NotebookNotFoundException {
	
		if (notebookId == null || notebookId.trim().isEmpty()) {
			throw new NotebookNotFoundException("笔记本不存在");
		}
//		Notebook notebook = notebookDao.findNotebookId(notebookId);
//		if (notebook == null) {
//			throw new NotebookNoteFoundException();
//		}
		 
		int n = notebookDao.countNotebookById(notebookId);
		if (n != 1) {
			throw new NotebookNotFoundException("没有笔记");
		} 
		
		return noteDao.findNotesByNotebookId(notebookId);
	}

	public Note getNote(String noteId) throws NoteNotFoundException {
		if (noteId == null || noteId.trim().isEmpty()) {
			throw new NoteNotFoundException("笔记本ID不存在 ！");
		}
		Note note = noteDao.findNoteById(noteId);
		//保存读取记录...
		if (note == null) {
			throw new NoteNotFoundException("笔记不存在！");
		}
		return note;
	}

//	@Transactional
	public Note addNote(String userId,
			String notebookId, String title) 
			throws UserNotFoundException, NoteNotFoundException{
		
		if (userId == null || userId.trim().isEmpty()) {
			throw new UserNotFoundException("用户id为空！");
		}
		User user = userDao.findUserById(userId);
		if (user == null) {
			throw new UserNotFoundException("没有人");
		}
		
		if (notebookId == null || notebookId.trim().isEmpty()) {
			throw new NotebookNotFoundException("笔记本id不存在！");
		}
		int n = notebookDao.countNotebookById(notebookId);
		if (n != 1) {
			throw new NotebookNotFoundException("没有笔记");
		} 
		if (title == null || title.trim().isEmpty()) {
			title = "爱与光明";
		}
		String id = UUID.randomUUID().toString();
		String statusId = "1";
		String typeId = "1";
		String body = "";
		long time = System.currentTimeMillis();
		Note note = new Note (id,notebookId, userId,
					statusId, typeId, title, body, time,time);
		n = noteDao.addNote(note);
		if (n != 1) {
			throw new NoteNotFoundException("保存失败！");
		}
		//当前的事务,会传播到addStar方法中
		//整合为一个事务！
		addStars(userId, -11);
		
		return note;
	}

	@Transactional(isolation=Isolation.READ_COMMITTED)
	public boolean update(String noteId, String title, 
			String body) throws NoteNotFoundException {
		if (noteId == null || noteId.trim().isEmpty()) {
			throw new NoteNotFoundException("Id不能为空");
		}
		Note note = noteDao.findNoteById(noteId);
		if (note == null) {
			throw new NoteNotFoundException("没有对应的笔记");
		}
		Note data = new Note();
		if (title != null && !title.equals(note.getTitle())) {
			data.setTitle(title);
		}
		if (body != null && !body.equals(note.getBody())) {
			data.setBody(body);
		}
		data.setId(noteId);
		data.setLastModifyTime(System.currentTimeMillis());
		System.out.println(data);
		int n =  noteDao.updateNote(data);
		return n == 1;
		
	}

	public boolean moveNote(String noteId, String notebookId) 
		throws NoteNotFoundException, NotebookNotFoundException {
		
		if (noteId == null || noteId.trim().isEmpty()) {
			throw new NoteNotFoundException("Id不为空");
		}
		Note note = noteDao.findNoteById(noteId);
		if (note == null) {
			throw new NoteNotFoundException("没有对应的笔记!");
		}
		if (notebookId == null || notebookId.trim().isEmpty()) {
			throw new NotebookNotFoundException("笔记本id不能为空！");
		}
		int n = notebookDao.countNotebookById(notebookId);
		if (n != 1) {
			throw new NotebookNotFoundException("没有笔记本!");
		}
		Note data = new Note();
		data.setId(noteId);
		data.setNotebookId(notebookId);
		data.setLastModifyTime(System.currentTimeMillis());
		
		n = noteDao.updateNote(data);
		return n == 1;
		
	}

	@Transactional
	public boolean deleteNote(String noteId) 
		throws NoteNotFoundException {
		if (noteId == null || noteId.trim().isEmpty()) {
			throw new NoteNotFoundException("笔记ID为空！");
		}
		Note note = noteDao.findNoteById(noteId);
		if (note == null) {
			throw new NoteNotFoundException("没有对应的笔记!");
		}
		Note data = new Note();
		data.setId(noteId);
		data.setLastModifyTime(System.currentTimeMillis());
		data.setStatusId("0");
		
		int  n = noteDao.updateNote(data);
		return n == 1;
	}
	
	//NoteServiceImpl
	@Transactional
	public int deleteNotes(String... noteIds) throws NoteNotFoundException {
//		for (String id: noteIds) {
//			int n = noteDao.deleteNoteById(id);
//			if(n!=1) {
//				throw new NoteNotFoundException("ID不存在");
//			}
//		}
//		return noteIds.length;
		int n = noteDao.deleteNotes(noteIds);
		if(n!= noteIds.length) {
			throw new NoteNotFoundException("ID不存在！");
		}
		return n;
	}

	
	@Transactional
	public boolean addStars(String userId, int stars) 
			throws UserNotFoundException {

		if (userId == null || userId.trim().isEmpty()) {
			throw new UserNotFoundException("用户id为空！");
		}
		User user = userDao.findUserById(userId);
		if (user == null) {
			throw new UserNotFoundException("没有人");
		}
        //检查是否已经有星星了
		Stars st = starsDao.findStarsByUserId(userId);
		if (st == null) {
			//如果没有星星
			String id = UUID.randomUUID().toString();
			
			st = new Stars(id,userId,stars);
			int n = starsDao.insertStars(st);
			if (n!=1) {
				throw new RuntimeException("失败");
			}
		} else {
			//如果有星星，就在原有星星数量上增加
			int n = st.getStars() + stars;
			if (n < 0) {
				// n=0;
				throw new RuntimeException("扣分太多");
			}
			st.setStars(n);
			n = starsDao.updateStars(st);
			if (n!=1) {
				throw new RuntimeException("失败");
			}
		}
		return true;
	}
	
	//NoteServiceImpl
	

	
}
