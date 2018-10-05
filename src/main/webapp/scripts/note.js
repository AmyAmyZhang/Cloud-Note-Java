// script/note.js 编码一定是utf-8

var SUCCESS = 0;
var ERROR = 1;

$(function() {
	
	//在document 对象中存储翻页页号状态
	$(document).data('page', 0);

	//加载第一页数据
	loadPagedNotebooks();
	
	//点击more 时候加载下一页数据
	$('#notebook-list').on('click','.more', loadPagedNotebooks);
	//网页加载以后,立即读取笔记本列表
//	loadNotebooks();	
	//on() 方法绑定事件可以区分时间源
	//click() 方法绑定事件,无法区别时间源
	//绑定笔记本列表区域的点击事件
	$('#notebook-list').on('click','.notebook', loadNotes);
	
	
	//console.log('click');	
	
	//监听笔记列表中的笔记点击事件，在点击时候加载显示笔记信息
	$('#note-list').on('click','.note', loadNote); 
	
	//新建笔记功能
	$('#note-list').on('click','#add_note', showAddNoteDialog);
	
	//监听新建笔记对话框中的创建笔记按钮
	$('#can').on('click','.create-note', addNote);
	
	//监听对话框中的关闭和取消按钮
	//其中'.close,.canel'是组选择器,表示
	//选择.close 或者 .cancel
	$('#can').on('click','.close,.cancel',closeDialog);
	
	$('#add_notebook').click(demo);
	
	//绑定笔记子菜单的触发事件
	$('#note-list').on('click', '.btn-note-menu', showNoteMenu);
	
	//监听整体的文档区域,任何位置点击都要关闭笔记
	$(document).click(hideNoteMenu);
	
	//监听笔记子菜单中移动按钮的点击
	$('#note-list').on('click','.btn_move', showMoveNoteDialog);
	
	//监听移动笔记中对话框中的的确定按钮
	$('#can').on('click','.move-note',moveNote);
	
	//监听笔记子菜单中删除按钮的点击
	$('#note-list').on('click','.btn_delete', showDeleteNoteDialog);
	
	//监听删除笔记中对话框中的的确定按钮
	$('#can').on('click', '.delete-note', deleteNote);
	
	//监听回收站按钮被点击
	$('#trash_button').click(showTrashBin);
});

function loadPagedNotebooks() {
	console.log('6666');
	var page = $(document).data('page');
	var userId = getCookie('userId');
	//从服务器拉取数据
	var url = 'notebook/page.do';
	var data = {userId: userId, page:page};
	$.getJSON(url, data, function(result) {
		if(result.state == SUCCESS) {
			var notebooks = result.data;
			showPagedNotebooks(notebooks, page);
			$(document).data('page', page+1);
		} else {
			alert(result.message);
		}
	});
}

function showPagedNotebooks(notebooks, page) {
	console.log('666');
	var ul = $("#notebook-list ul");
	if (page == 0) { //第一页时候清空ul中的li
		ul.empty();
	} else {//不是第一页,只删除.more元素
		//删除more
		ul.find('.more').remove();
	}	
	for (var i=0; i < notebooks.length; i++) {
		var notebook = notebooks[i];
		var li = notebookTemplate.replace(
				'[name]', notebook.name);
		li = $(li);
		li.data('notebookId', notebook.id);
		ul.append(li);
	}
	if (notebooks.length != 0) {
		ul.append(moreTemplate);
	}
}

var moreTemplate = '<li class="online more">' +
					'<a><i class="fa fa-plus" title="online" ' + 
					'rel="tooltip-bottom"></i> 加载更多...</a></li>';

/** 监听回收站按钮被点击 */
function showTrashBin() {
	$('#trash-bin').show();
	$('#note-list').hide();
	
}

function deleteNote() {
	var url = 'note/delete.do';
	var id = $(document).data('note').id;
	var data = {noteId: id};
	$.post(url, data, function(result) {
		if (result.state == SUCCESS) {
			//删除成功,在当前笔记列表中删除笔记
			//将笔记列表中后面的笔记放在选定的位置
			var li = $('#note-list .checked').parent();
			var lis = li.siblings();
			if (lis.size() > 0) {
				lis.eq(0).click();
			} else {
				$('#input_note_title').val("");
				um.setContent("");
			}
			li.remove();
			closeDialog();
		} else {
			alert(result.message);
		}
	});
}

/** 显示删除笔记对话框 */
function showDeleteNoteDialog() {
	
	var id = $(document).data('note').id;
	if (id) {
		$('#can').load('alert/alert_delete_note.html');
		$('.opacity_bg').show();
		return;
	}
	alert('必须选择笔记!');
}

/** 显示移动笔记对话框 */
function showMoveNoteDialog() {

	var id = $(document).data('note').id;
	if (id) {
		$('#can').load('alert/alert_move.html', loadNotebookOptions)
		$('.opacity_bg').show();
		return;
	}  
	alert('必须选择笔记！');

}

/** 移动笔记事件处理方法 */
function moveNote() {
	var url = 'note/move.do';
	var id = $(document).data('note').id;
	var bookId = $('#moveSelect').val();
	//笔记本id没有变化就不移动了！
	if (bookId == $(document).data('notebookId')) {
		return;
	}
	var data = {noteId:id, notebookId:bookId};
	$.post(url, data, function(result) {
		if (result.state == SUCCESS) {
			//移动成功,在当前笔记列表中删除移动的笔记
			//将下个笔记设置为当前笔记,如果没有下个笔记就设置上一个为当前笔记
			//否则清空编辑区域
			var li = $('#note-list .checked').parent();
			var lis = li.siblings();
			if(lis.size() > 0) {
				lis.eq(0).click();
			} else {
				$('#input_note_title').val("");
				um.setContent("");
			}
			li.remove();
			closeDialog();//关闭对话框！
		} else {
			alert(result.message);
		}
	});
	
}


/** 加载移动笔记对话框中的笔记本 */
function loadNotebookOptions() {
	var url = 'notebook/list.do';
	var data = {userId:getCookie('userId')};
	$.getJSON(url, data, function(result) {
		if (result.state == SUCCESS) {
			
			var notebooks = result.data;
			//清除全部的笔记本下拉列表选项
			//添加新的笔记本列表选项
			$('#moveSelect').empty();
			var id = $(document).data('notebookId');
			for (var i = 0; i < notebooks.length; i++) {
				var notebook = notebooks[i];
				var opt = $('<option></option>')
					.val(notebook.id)
					.html(notebook.name);
				//默认选定当时的笔记本ID
				if(notebook.id == id) {
					opt.attr('selected','selected');
				}
				$('#moveSelect').append(opt);
			}
		} else {
			alert(result.message);
		}
	});
}


function updateNote() {
	var url = 'note/updata.do';
	var note = $(document).data('note');
	var data = {noteId: note.id};
} 

/** 关闭笔记子菜单事件处理方法*/
function hideNoteMenu() {
	$('.note_menu').hide();
}

/** 显示笔记子菜单事件处理方法*/
function showNoteMenu() {

	//找到菜单对象,调用show()方法 hide方法是异步的
	
	//$('.note_menu').hide(function(){
	
	var btn = $(this);
	//如果当前笔记是被选定的笔记项目就弹出子菜单
	//btn.parent('.checked')获取当前按钮的父元素这个元素必须符合选择器
	//'.checked'，如果不符合就返回空的jQuery元素
		btn.parent('.checked').next().toggle();
	//});
	//阻止点击事件的继续传播！
	return false;
}

function closeDialog() {
	//console.log("closeDialog");
	$('.opacity_bg').hide();
	$('#can').empty();	
}

function addNote() {
	//console.log("addNote");
	
	var url = 'note/add.do';
	var title = $('#input_note').val();
	var notebookId = $(document).data('notebookId');
	
	var data = {userId:getCookie('userId'), 
			notebookId:notebookId,
			title:title};
	$.post(url, data, function(result) {
		if (result.state == SUCCESS) {
			var note = result.data;
			showNote(note);
			//找到显示笔记列表的ul对象
			var ul = $('#note-list ul');
			//创建新的笔记列表项目 li
			var li = noteTemplate.replace('[title]', note.title);
			li = $(li);
			//绑定笔记ID到li
			li.data('noteId', note.id);
			//设置选定效果
			li.find('a').addClass('checked');
			ul.find('a').removeClass('checked');
			//插入到第一个位置
			ul.prepend(li);
			closeDialog();
		} else {
			alert(result.message);
		}
	})
	
}

function showAddNoteDialog() {
	var id = $(document).data('notebookId');
	if (id) {
		$('#can').load('alert/alert_note.html', function(){
			$('.opacity_bg').show();
			$('#input_note').focus();
		});	
		return;
	}  
	alert('必须选择笔记本！');

}


function loadNote() {
	//获取当前点击的li 元素
	var li = $(this); //当前被点击的对象li

	var id = li.data('noteId');
	//在被点击的笔记本li增加选定效果
	li.parent().find('a').removeClass('checked');
	li.find('a').addClass('checked');

	var url = 'note/load.do';
	var data = {noteId: id};
	console.log(data);
	$.getJSON(url, data, function(result){
		if (result.state == SUCCESS) {
			var note = result.data;
			showNote(note);
			$(document).data('note', note);
		} else {
			alert(result.message);
		}
	});	
}

function showNote(note) {
	//绑定笔记信息,用于保存操作
	//$('#input_note_title').data('note',note);
	
	//显示笔记标题
	$('#input_note_title').val(note.title);
	//显示笔记内容  
	um.setContent(note.body);
}

/** 笔记本项目点击事件处理方法, 加载全部笔记 */
function loadNotes() {
	//在点击笔记本列表中的笔记时候，为了
	//显示笔记列表:关闭回收站 打开笔记列表
	$('#trash-bin').hide();
	$('#note-list').show();
	
	var li = $(this); //当前被点击的对象li
	
	//在被点击的笔记本li增加选定效果
	li.parent().find('a').removeClass('checked');
	li.find('a').addClass('checked');
	
	var url = 'note/list.do';
	var data = {notebookId:li.data('notebookId')};
	
	console.log(data);
	$.getJSON(url, data, function(result){
		if (result.state == SUCCESS) {
			var notes = result.data;
			showNotes(notes);
		} else {
			alert(result.message);
		}
	});	
	//绑定笔记本ID,用于添加笔记功能
	$(document).data('notebookId', li.data('notebookId'));
	
}

/**将笔记列表信息显示到屏幕上*/
function showNotes(notes) {
	console.log(notes);
	//将每个笔记对象显示到屏幕的ul区域上
	var ul = $('#note-list ul');
	ul.empty();
	for (var i = 0; i < notes.length; i++) {
		var note = notes[i];
		var li = noteTemplate.replace('[title]', note.title);
		li = $(li);
		//将笔记Id绑定到li,用在点击笔记时候显示
		li.data('noteId',note.id);
		ul.append(li);  
	}
}

var noteTemplate = '<li class="online note"> <a> <i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i>' + 
	'[title] <button type="button" class="btn btn-default btn-xs btn_position btn_slide_down btn-note-menu">' +
	'<i class="fa fa-chevron-down"></i></button></a><div class="note_menu" tabindex="-1"><dl>' + 
	'<dt><button type="button" class="btn btn-default btn-xs btn_move" title="移动至..."><i class="fa fa-random"></i></button></dt>' +
	'<dt><button type="button" class="btn btn-default btn-xs btn_share" title="分享"><i class="fa fa-sitemap"></i></button></dt>' +
	'<dt><button type="button" class="btn btn-default btn-xs btn_delete" title="删除"><i class="fa fa-times"></i></button></dt>' + 
    '</dl></div></li>';


function loadNotebooks() {
	//利用ajax从服务器获取(get)数据，使用getJSON方法
	var url = 'notebook/list.do';
	data = {userId:getCookie('userId')};
	$.getJSON(url, data, function(result){
		console.log(result);
		if(result.state == SUCCESS) {
			var notebooks = result.data;
			//在showNptebooks方法中将全部的笔记本数据
			//notebooks显示到notebook-list区域
			showNotebooks(notebooks);
		} else {
			alert(result.message);
		}
	});
}

/**  在notebook-list区域中显示笔记本列表 */
function showNotebooks(notebooks) {
	//找到显示笔记本列表的区域
	//遍历notebooks数组,将为每个对象创建一个元素,
	//添加到ul元素中.
	var ul = $('#notebook-list ul');
	ul.empty(); //清除ul中原有的内容
	for (var i = 0; i < notebooks.length; i++) {
		var notebook = notebooks[i];
		var li = notebookTemplate.replace('[name]', notebook.name);
		li = $(li);
		//将notebook.id绑定到li
		li.data('notebookId', notebook.id);
		ul.append(li);
	}	
}

var notebookTemplate = '<li class="online notebook">' +
					'<a><i class="fa fa-book" title="online" ' + 
					'rel="tooltip-bottom"></i>[name]</a></li>';



function demo() {
	var ary = [1,2,5,6,7,8.4,3,9];
	sort(ary);
	console.log(ary);
}

function sort(ary) {
	
	for(var i = 0; i < ary.length - 1; i++) {
		for (var j = 0; j < ary.length - i - 1; j++) {
			if (ary[j] > ary[j+1]) {
				var t = ary[j];
				ary[j] = ary[j+1];
				ary[j+1] = t;
			}
		}	
	}
}
