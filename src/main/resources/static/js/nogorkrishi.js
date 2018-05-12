
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
	if (rmmeco) {
		$.removeCookie("clickme");
		$("#" + rmmeco).trigger("click");
	} else if ($(".clickonload").length > 0) {
		$(".clickonload").trigger("click");
	}

	$("body").on("click", ".types", function() {
		$("#pclp-container").load($(this).attr("href"));
		$("#alltype").text($(this).text()).show();
		$(".alltype").hide();
		$("#paginationdiv").show();
		$(".pagestoshow").show();
		loadPagination();
		return false;
	}).on("click", ".products", function() {
		var ob = $(this);
		$("#pclp-container").load(ob.attr("href"));
		$("#typetext").attr("href", ob.data("typehref")).text(ob.data("typetext"));
		$("#producttext").text($(".productname", ob).text());
		$("#singleproduct").show();
		$(".singleproduct").hide();
		$("#paginationdiv").hide();
		$(".pagestoshow").hide();
		return false;
	}).on("click", "#areaselection", function() {
		var ar = $(this);
		$(".nurseries").show();
		if (ar.val() !== "")
			$(".nurseries").not("." + ar.val()).hide();
		return false;
	}).on("change", ".pg", function() {
		var ob = $(this);
		$("." + ob.attr("id")).val(ob.val());
		loadPagination();
	}).on("click", ".arrows", function() {
		var cp = parseInt($(".pagination > .pages.disabled").data("page"));
		showPage((cp + ($(this).is(".leftarrow") ? -1 : 1)));
		return false;
	});
	loadPagination();
});

function showPage(pageno) {
	if ($("[data-page='" + pageno + "']").length === 0) return false;

	var total = parseInt($("#totalproducts").val());
	var itemsToShow = parseInt($("#pg1").val());
	var pages = parseInt(total / itemsToShow);
	if (total % itemsToShow > 0)
		pages += 1;

	var strt = (itemsToShow * (pageno - 1)) + 1;
	var nd = itemsToShow * pageno;

	$(".ponnoitems").hide();

	for (var i = strt; i <= nd; i++) {
		var itm = $("[data-index='" + i + "']");
		if (itm.length > 0)
			itm.show();
	}

	var dsbcls = "disabled";
	var lft = $(".pagination > .left");
	lft.removeClass(dsbcls);
	if (pageno < 3 || pages < 4)
		lft.addClass(dsbcls);

	var rgt = $(".pagination > .right");
	rgt.removeClass(dsbcls);
	if ((pages - pageno) < 3)
		rgt.addClass(dsbcls);

	$(".pagination > .pages").removeClass(dsbcls).hide();
	$('[data-page="' + pageno + '"]').addClass(dsbcls).show();
	if (pageno > 1) {
		$('[data-page="' + (pageno - 1) + '"]').show();
	} else if ((pageno + 2) <= pages) {
		$('[data-page="' + (pageno + 2) + '"]').show();
	}
	if (pageno < pages) {
		$('[data-page="' + (pageno + 1) + '"]').show();
	} else if ((pageno - 2) > 0) {
		$('[data-page="' + (pageno - 2) + '"]').show();
	}
	return false;
}

function loadPagination() {
	var total = parseInt($("#totalproducts").val());
	var itemsToShow = parseInt($("#pg1").val());
	var pages = parseInt(total / itemsToShow);
	if (total % itemsToShow > 0)
		pages += 1;

	var pgdiv = $("#paginationdiv");
	$(".pages", pgdiv).remove();
	pgdiv.hide();
	if (pages > 1) {
		var item = $(".right", pgdiv);
		for (var i = 1; i <= pages; i++) {
			$('<li class="page-item pages" data-page="' + i + '"><a class="page-link" onClick="showPage(' + i + '); return false;" href="#">' + i + '</a></li>').insertBefore(item);
		}
		pgdiv.show();
		showPage(1);
	} else {
		$(".ponnoitems").show();
	}
}

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