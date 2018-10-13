$("[data-page='editor']").on("init", function() {
	initSummerNote($('#content'));
});

$("[data-page='settings']").on("init", function() {
	initSummerNote($('#footerText'));
});

function initSummerNote(fld) {
	fld.summernote({
		onImageUpload: function(files, editor, welEditable) {
			sendFile(files[0], editor, welEditable);
		}
	});
}

function sendFile(file, editor, welEditable) {
	var data = new FormData();
	data.append("file", file);
	$.post("/pages/editor/image", data, function(url) {
		editor.insertImage(welEditable, url);
	});
}