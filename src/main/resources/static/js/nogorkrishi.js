var dhaka = {
	lat : 23.758658,
	lng : 90.383703
};

$(document).ready(function() {
	$("[data-page]").trigger("init");

	if ($("#lgnmn").length === 0 && $("#header-nav-items").length > 0) {
		$("#header-nav-items").removeClass("col-lg-7").addClass("col-lg-8");
	}

	$("body").on("click", "#registerformbtn", function() {
		var btn = $(this);
		var pswd = $("#password").val();
		var cnpswd = $("#confirmPassword").val();
		if (pswd !== cnpswd) {
			alert("Passwords are not same!");
			return false;
		}
		var result  = zxcvbn(pswd),
			score   = result.score,
			message = result.feedback.warning || 'The password is weak';
		if (score < 3) {
			bootbox.alert(message);
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
