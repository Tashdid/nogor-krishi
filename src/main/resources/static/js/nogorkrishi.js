var dhaka = {
	lat : 23.758658,
	lng : 90.383703
};

$(document).ready(function() {
	$("[data-page]").trigger("init");

	if ($("#lgnmn").length === 0 && $("#header-nav-items").length > 0) {
		$("#header-nav-items").removeClass("col-lg-7").addClass("col-lg-8");
	}

	$("body").on("click", ".rmvbtn", function(){
		var btn = $(this);
		bootbox.confirm("Are you sure?", function(rs) {
			if (!rs) return;
			blockui();
			$.post(btn.data("remote"), function(dlrs) {
				if (dlrs !== true) {
					bootbox.alert(dlrs);
					return false;
				}
				var rmme = btn.parents(".rememberme");
				if (rmme.length > 0 && rmme.find(".clickme").length > 0) {
					$.cookie("clickme", rmme.find(".clickme").attr("id"));
				} else {
					$.removeCookie("clickme");
				}
				location.reload();
			}).fail(function() {
				bootbox.alert("Failed to delete!");
			}).always(function() {
				unblockui();
			});
		});
	}).on("click", "#registerformbtn", function() {
		var btn = $(this);
		var pswd = $("#password").val();
		var cnpswd = $("#confirmPassword").val();
		if (pswd !== cnpswd) {
			alert("Passwords are not same!");
			return false;
		}
		blockui();
		btn.parents("form").submit();
		return false;
	});
});


var blockoptions = {
	message : $('#block-ui'),
	baseZ : 1055,
	overlayCSS : { opacity : 0.5, backgroundColor : 'silver' },
	css: { left: '0px', width: '100%'}
};

function blockui(target) {
	if ($(".blockUI.blockPage")) unblockui($(".blockUI.blockPage").parent());
	if (target) target.block(blockoptions);
	else $.blockUI(blockoptions);
}

function unblockui(target) {
	if (target) target.unblock();
	else $.unblockUI();
}
