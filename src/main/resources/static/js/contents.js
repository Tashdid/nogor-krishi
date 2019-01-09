
$("[data-page='contents']").on("init", function() {
	$("body").on("click", ".viewbtn", function() {
		var btn = $(this);
		var dialog = bootbox.dialog({
			backdrop : true,
			onEscape : true,
			message : '<p><i class="fa fa-spin fa-spinner"></i> Loading...</p>'
		});
		dialog.init(function(){
			$.get(btn.data("remote"), function(rsp) {
				dialog.find('.bootbox-body').html(rsp);
			});
		});
	}).on("click", '.pubtog', function() {
		var btn = $(this);
		btn.append('<i class="fa fa-spin fa-spinner"></i>').attr("disabled", "disabled");
		$.post(btn.data("remote"), function(rs) {
			btn.find('i').remove();
			btn.removeAttr("disabled");
			btn.find("span").removeClass("glyphicon-cloud-upload").removeClass("glyphicon-cloud-download")
						.addClass(rs ? "glyphicon-cloud-download" : "glyphicon-cloud-upload");
			btn.removeClass("btn-danger").removeClass("btn-success").addClass(rs ? "btn-danger" : "btn-success");
			btn.parents("tr").find(".pubstat").text(rs ? "প্রকাশিত" : "অপ্রকাশিত");

			var pubtbl = $("#pubtable");
			pubtbl.load("/contents/pubtable", function() {
				loadDataTables(pubtbl);
			});
		});
	});
});
