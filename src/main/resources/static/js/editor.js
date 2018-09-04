$("[data-page='editor']").on("init", function() {
	$('#content').froalaEditor({
		toolbarInline: false,

		// Set the image upload parameter.
		imageUploadParam: 'file',

		// Set the image upload URL.
		imageUploadURL: '/pages/editor/image',

		// Set request type.
		imageUploadMethod: 'POST',

		// Set max image size to 5MB.
		imageMaxSize: 5 * 1024 * 1024,

		// Allow to upload PNG and JPG.
		imageAllowedTypes: ['jpeg', 'jpg', 'png'],

		// Set the file upload parameter.
		fileUploadParam: 'file',

		// Set the file upload URL.
		fileUploadURL: '/pages/editor/file',

		// Set request type.
		fileUploadMethod: 'POST',

		// Set max file size to 5MB.
		fileMaxSize: 5 * 1024 * 1024,

		// Allow to upload all type files.
		fileAllowedTypes: ['*']
	});
	console.log("Editor should be loaded!");
});