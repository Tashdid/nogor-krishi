$("[data-page='editor']").on("init", function() {
	$('#content').summernote({
		onImageUpload: function(files, editor, welEditable) {
			sendFile(files[0], editor, welEditable);
		}
	});
	console.log("Editor should be loaded!");
});

function sendFile(file, editor, welEditable) {
	var data = new FormData();
	data.append("file", file);
	$.post("/pages/editor/image", data, function(url) {
		editor.insertImage(welEditable, url);
	});
}