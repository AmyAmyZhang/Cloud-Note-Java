package cn.tedu.note.service;

import java.util.List;
import java.util.Map;

import cn.tedu.note.entity.Note;

public interface NoteService {
	
	List<Map<String, Object>> listNotes(String notebookId)
		throws NotebookNotFoundException;
	
	Note getNote(String noteId)
		throws NoteNotFoundException;

	Note addNote(String userId, String notebookId, String title)
			throws UserNotFoundException,NoteNotFoundException;
	
	boolean update(String noteId, String title, String body)
		throws NoteNotFoundException;
	
	boolean moveNote(String noteId, String notebookId)
		throws NoteNotFoundException, NotebookNotFoundException;
	
	boolean deleteNote(String noteId) 
		throws NoteNotFoundException;
	
	//NoteService
	int deleteNotes(String... noteIds)
		throws NoteNotFoundException;
	
	boolean addStars(String userId, int stars)
		throws UserNotFoundException;
	
	

}
