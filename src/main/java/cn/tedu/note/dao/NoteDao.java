 package cn.tedu.note.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tedu.note.entity.Note;

public interface NoteDao {
	/**
	 * 查询一个笔记本中的全部笔记信息
	 * @param notebookId
	 * @return 笔记列表,每个元素包含id和title
	 */ 
	List<Map<String,Object>> findNotesByNotebookId(String notebookId);

	Note findNoteById(String noteId);
	
	int addNote(Note note);
	
	int updateNote(Note note);
	
	Note showTrashByUserId(String userId);
	
	int deleteNoteById(String noteId);
	
	int deleteNotes(
			@Param("ids") String... ids);
	
	List<Map<String, Object>> findNotes(
			@Param("userId")String userId,
			@Param("notebookId")String notebookId,
			@Param("statusId")String statusId);
	

	
	
}
