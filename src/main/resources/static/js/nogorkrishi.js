
$(document).ready(function() {
	var path = window.location.pathname;
	var parts = path.split("/");
	if (parts.length > 3) {
		path = "/" + parts[1] + "/" + parts[2];
	}
	$(".dropdown-item[href='" + path + "']").addClass("active");

	$("body").on("click", ".rmvbtn", function(){
		var btn = $(this);
		bootbox.confirm("Are you sure?", function(rs) {
			if (!rs) return;
			$.post(btn.data("remote"), function(dlrs) {
				var rmme = btn.parents(".rememberme");
				if (rmme.length > 0 && rmme.find(".clickme").length > 0) {
					$.cookie("clickme", rmme.find(".clickme").attr("id"));
				} else {
					$.removeCookie("clickme");
				}
				location.reload();
			}).fail(function() {
				bootbox.alert("Failed to delete!");
			});
		});
	});

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
			dfs.push({data: itm[0], visible: (itm.length > 1 ? itm[1] === "true" : true), className: (itm.length > 2 ? itm[2] : "")});
		}

		var supportsReOrdering = tbl.is('.reorder') && tbl.data("actionurl") !== undefined;
		var rwrord = supportsReOrdering ? {dataSrc: 'sequence'} : false;

		var dttblref = tbl.DataTable( {
			columns: dfs,
			rowReorder: rwrord,
			initComplete: function () {
				var count = 0;
				this.api().columns(".filter").every( function () {
					var column = this;
					var select = $('<select></select>')
						.appendTo( $(column.header()).empty() )
						.on( 'change', function () {
							var val = $.fn.dataTable.util.escapeRegex(
								$(this).val()
							);

							column
								.search( val ? '^'+val+'$' : '', true, false )
								.draw();
						});

					if ($(column.header()).is(".all")) {
						select.append( '<option value="">All</option>' );
					}

					column.data().unique().sort().each( function ( d, j ) {
						select.append( '<option value="'+d+'">'+d+'</option>' )
					});
					count++;
				});
				if (count > 0) {
					tbl.find("th.filter select option").trigger("change");
				}
			}
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

	var rmmeco = $.cookie("clickme");
	console.log(rmmeco);
	if (rmmeco) {
		console.log("I am at here");
		$.removeCookie("clickme");
		$("#" + rmmeco).trigger("click");
	} else if ($(".clickonload").length > 0) {
		$(".clickonload").trigger("click");
	}
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