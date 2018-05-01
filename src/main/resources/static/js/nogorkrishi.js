
$(document).ready(function() {
	var path = window.location.pathname;
	var parts = path.split("/");
	if (parts.length > 3) {
		path = "/" + parts[1] + "/" + parts[2];
	}
	$(".nav-link[href='" + path + "']").addClass("active");

	$(".uploadimg").on("change", function(){
		showImage(this);
	});

	$(".flupl").fileinput({
		browseClass : "btn btn-primary btn-block",
		showCaption : false,
		showRemove : false,
		showUpload : false
	});

	$('table.dttable').each(function() {
		var tbl = $(this);

		var dfs = [];
		var flds = tbl.data("columndefs").split(",");
		for (var i in flds) {
			var itm = flds[i].split('-');
			dfs.push({data: itm[0], visible: (itm.length > 1 ? itm[1] === "true" : true)});
		}

		var supportsReOrdering = tbl.is('.reorder') && tbl.data("actionurl") !== undefined;
		var rwrord = supportsReOrdering ? {dataSrc: 'sequence'} : false;

		var dttblref = tbl.DataTable( {
			columns: dfs,
			rowReorder: rwrord
		});
		
		if (supportsReOrdering) {
			dttblref.on( 'row-reorder', function (e, diff, edit) {
				var ien=diff.length;
				if (ien < 1) return false;
				var data = {};
				for (var i=0; i < ien; i++) {
					data["data["+ dttblref.row(diff[i].node).data().id + "]"] = diff[i].newData;
				}
				$.post(tbl.data("actionurl"), data, function() {
					console.log("should be successful!");
				});
			});
		}
	});
});

function showImage(input) {
	var imgid = $($(input).parent().find(".imgtoshow")[0]);
	var fldtosub = $($(input).parent().find(".imgtosub")[0]);

	if (imgid && input.files && input.files[0]) {
		var reader = new FileReader();

		reader.onload = function(e) {
			var rslt = e.target.result;
			imgid.attr('src', rslt);
			fldtosub.val(rslt.substr(rslt.indexOf('base64,') + 7));
		}

		reader.readAsDataURL(input.files[0]);
	}
}