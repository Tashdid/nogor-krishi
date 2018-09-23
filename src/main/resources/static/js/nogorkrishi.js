var dhaka = {
	lat : 23.758658,
	lng : 90.383703
};

var etb = {'0': '০', '1': '১', '2': '২', '3': '৩', '4': '৪', '5': '৫', '6': '৬', '7': '৭', '8': '৮', '9': '৯', '.': '.'};

function engToBen(str) {
	var rst = "";
	for (var i = 0; i < str.length; i++) {
		var bn = etb[str.charAt(i)];
		if (bn === undefined)
			bn = str.charAt(i);
		rst += bn;
	}
	return rst;
}

$(document).ready(function() {
	$("[data-page]").trigger("init");

//	$("select[multiple='multiple'].multiple").multipleSelect({
//		selectAll: false,
//		multiple: true
//	});
//
//	$("select[multiple='multiple']:not(.multiple)").multipleSelect({
//		selectAll: false
//	});

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
	}).on("submit", "#registerform", function() {
		var btn = $(this);
		var pswd = $("#password").val();
		var cnpswd = $("#confirmPassword").val();
		if (pswd !== cnpswd) {
			alert("Passwords are not same!");
			return false;
		}
		blockui();
	}).on("submit", "#changepasswordform", function() {
		var btn = $(this);
		var pswd = $("#newPassword").val();
		var cnpswd = $("#confirmNewPassword").val();
		if (pswd !== cnpswd) {
			alert("Passwords are not same!");
			return false;
		}
		blockui();
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
