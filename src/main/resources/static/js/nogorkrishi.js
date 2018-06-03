var conv = {'5': '৫', '10': '১০', '15': '১৫', '20': '২০', '25': '২৫', '30': '৩০', '35': '৩৫', '40': '৪০', '45': '৪৫', '50': '৫০', '55': '৫৫', '60': '৬০', '65': '৬৫', '70': '৭০', '75': '৭৫', '80': '৮০', '85': '৮৫', '90': '৯০', '95': '৯৫', '100': '১০০'}
var map;
var nurseries = {};

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
		var cont = $("#pclp-container");
		blockui(cont);
		cont.load($(this).attr("href"), function(){
			unblockui(cont);
			loadPagination();
		});
		$("#alltype").text($(this).text()).show();
		$(".alltype").hide();
		$("#paginationdiv").show();
		$(".pagestoshow").show();
		return false;
	}).on("click", ".products", function() {
		var ob = $(this);
		var cont = $("#pclp-container");
		blockui(cont);
		cont.load(ob.attr("href"), function() {
			unblockui(cont);
		});
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
	}).on("change", ".layoutgen", function() {
		if ($("#layoutlength").val().length > 0 && $("#layoutwidth").val().length > 0) {
			var lng = parseInt($("#layoutlength").val()) + 1;
			var wdt = parseInt($("#layoutwidth").val()) + 1;
			var tbl = "";
			for(var i = 0; i < lng; i++) {
				tbl += '<div class="tr">';
				for(var j = 0; j < wdt; j++) {
					tbl += '<div class="td"><div class="inner">';
					if (i === 0 && j > 1 && j%5 === 0) {
						tbl += '<span>' + conv[(j/5) * 5] + '`</span>';
					} else if (j === 0 && i > 1 && i%5 === 0) {
						tbl += '<span>' + conv[(i/5) * 5] + '`</span>';
					}
					tbl += '</div></div>';
				}
				tbl += '</div>';
			}
			$("div.table").html(tbl);
		} else {
			$("div.table").html("");
		}
		setLayoutCellHeight();
	}).on("change", "#areaselection", function() {
		var ar = $(this).val();
		$.each(nurseries, function(key, value) {
			var show = ar === "" || ar === key;
			$.each(value, function(ik, iv) {
				iv.setVisible(show);
			});
		});
	}).on("click", "#savelayout", function() {
		var btn = $(this);
		btn.prop("disabled", true);
		$("#tableov").find(".ui-draggable-handle").each(function(i, vl) {
			var ob = $(vl);
			ob.append('<input type="hidden" name="blocks[' + i + '].product" value="' + ob.data("id") + '"/>');
			ob.append('<input type="hidden" name="blocks[' + i + '].ptop" value="' + ob.position().top + '"/>');
			ob.append('<input type="hidden" name="blocks[' + i + '].pleft" value="' + ob.position().left + '"/>');
			ob.append('<input type="hidden" name="blocks[' + i + '].cheight" value="' + ob.height() + '"/>');
			ob.append('<input type="hidden" name="blocks[' + i + '].cwidth" value="' + ob.width() + '"/>');
		});
		btn.parents("form").submit();
		return false;
	}).on("change", ".area", function() {
		var ar = $(this);
		var arid = ar.val();
		var prnt = ar.parents(".col-12");
		prnt.find(".nurseries").attr("style", "display: none !important");
		prnt.find(".nurseries.ar" + arid).show();
		prnt.find(".nurseries.ar" + arid).find(".nursery").trigger("change");
	}).on("change", ".nursery", function() {
		var nr = $(this);
		var nrid = nr.val();
		var prnt = nr.parents(".col-lg-8");
		var arid = prnt.find(".area").val();
		prnt.find(".nd").hide();
		prnt.find(".nd.ar" + arid + ".nr" + nrid).show();
		prnt.find(".table").hide();
		prnt.find(".table.ar" + arid + ".nr" + nrid).show();
	});

	loadPagination();
	
	var lo = $("div.table");
	if (lo.length === 1) {
		setLayoutCellHeight();
	}
	
	$( window ).resize(function() {
		if (lo.length === 1) {
			setLayoutCellHeight();
		}
	});

	var counts = [ 0 ];
	var resizeOpts = {
		handles : "all",
		autoHide : true
	};
	$('.draggable').draggable({
		helper: 'clone',
		start: function() { counts[0]++; }
	});
	$(".droppable").droppable({
		drop : function(e, ui) {
			if (ui.draggable.hasClass("draggable")) {
				var orit = $(ui.helper);
				var orimg = orit.find(".img");
				orit.clone().appendTo($(this));
				$(".droppable .draggable").addClass("item-" + counts[0]);
				var addedItem = $(".droppable .item-" + counts[0]);
				var addedImg = addedItem.find(".img");
				addedImg.addClass("imgSize-" + counts[0])
						.css("border", "2px solid black")
						.width(orimg.width())
						.height(orimg.height());

				addedItem.attr("title", addedItem.data("name"));
				addedItem.find("a").remove();
				addedItem.find("span").remove();
				var cx = e.pageX;
				var dvx = $("#tableov").offset().left;
				addedItem.width("").height("");
				addedItem.css("left", e.pageX - dvx - (addedItem.width()/2)).css("top", e.pageY - $("#tableov").offset().top - (addedItem.height()/2));
				addedItem.removeClass("draggable ui-draggable ui-draggable-dragging tab-content-plant");

				$(".item-" + counts[0]).dblclick(function() {
					$(this).remove();
					loadGardenDetails();
				});
				make_draggable($(".item-" + counts[0]));
				addedImg.resizable(resizeOpts);
				loadGardenDetails();
			}
		}
	});

	var zIndex = 0;
	function make_draggable(elements) {	
		elements.draggable({
			containment: '.droppable',
			start:function(e,ui){ ui.helper.css('z-index',++zIndex); },
			stop:function(e,ui){
			}
		});
	}
});

function loadGardenDetails() {
	var prods = [];
	var gd = "";
	$("#tableov").find(".ui-draggable-handle").each(function(){
		var prd = $(this);
		if ($.inArray(prd.data("id"), prods) === -1) {
			prods.push(prd.data("id"));
			gd += '<tr><td>' + prd.data("name") + '</td><td>' + prd.data("prod") + '</td><td>' + prd.data("ben") + '</td></tr>';
		}
	});
	$("#gdetails").html(gd);
}

function setLayoutCellHeight() {
	var lo = $("div.table");
	if (lo.width() === 0) {
		$("#tableov").width(0).height(0);
		return;
	}
	var wdth = lo.width() / lo.find("div.tr:first > div.td").length;
	var hght = wdth > 50 ? 50 : wdth;
	lo.find("div.tr > div.td").height(hght);
	$("#tableov").width(lo.width() - wdth).height(lo.height() - hght).css("margin-left", wdth).css("margin-top", hght);
}

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

function initMap() {
	// The location of Dhaka
	var dhaka = {
		lat : 23.810332,
		lng : 90.412518
	};
	// The map, centered in Dhaka
	map = new google.maps.Map(document.getElementById('nurseriesmap'), {
		zoom : 10,
		center : dhaka
	});
	var infowindow = new google.maps.InfoWindow();
	$.get("/nurseries", function(rs) {
		$.each(rs, function(fk, fv) {
			var area = [];
			$.each(fv, function(sk, it) {
				var marker = new google.maps.Marker({position: {lat: it.latitude, lng: it.longitude}, map: map});
				google.maps.event.addListener(marker, 'click', function() {
					infowindow.setContent('<div><p class="color-green lead">' + it.name
							+ '</p>' + it.address + '<br>' + it.area.name + ', ' + it.area.city.name
							+ '<br>ফোন : ' + it.phone + '</div>');
					infowindow.open(map, this);
				});
				area.push(marker);
			});
			nurseries[fk] = area;
		});
	});
}

function blockui(target) {
	target.block({
		message : '<h1>Loading.......</h1>'
	});
}

function unblockui(target) {
	target.unblock();
}