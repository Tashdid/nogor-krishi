var map;
var nurseries = {};

$("[data-page='home']").on("init", function(){
	$("body").on("change", "#nurseryareas", function() {
		var ar = $(this).val();
		$.each(nurseries, function(key, value) {
			var show = ar === "" || ar === key;
			$.each(value, function(ik, iv) {
				iv.setVisible(show);
			});
		});
	});
});

$("[data-page='ponno']").on("init", function(){
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
	}).on("change", ".pg", function() {
		var ob = $(this);
		$("." + ob.attr("id")).val(ob.val());
		loadPagination();
	}).on("click", ".arrows", function() {
		var cp = parseInt($(".pagination > .pages.disabled").data("page"));
		showPage((cp + ($(this).is(".leftarrow") ? -1 : 1)));
		return false;
	}).on("click", "#areaselection", function() {
		var ar = $(this);
		$(".nurseries").show();
		if (ar.val() !== "")
			$(".nurseries").not("." + ar.val()).hide();
		return false;
	});

	loadPagination();
});

$("[data-page='vendor']").on("init", function() {
	$("body").on("click", ".areas, .vendor", function() {
		var ob = $(this);
		var cont = $("#vendors-container");
		blockui(cont);
		$.get(ob.attr("href"), function(rs) {
			unblockui(cont);
			var prnt = cont.parent();
			prnt.find(".removeme").remove();
			prnt.append(rs);
		});
		return false;
	});
});

$("[data-page='exlayout']").on("init", function() {
	$("body").on("change", ".area", function() {
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
	}).on("change", ".tpchange", function() {
		var tp = $(this);
		tp.parents("tr").find(".price").text(tp.find("option:selected").data("priceshow"));
		var prnt = tp.parents("tbody");
		var totp = 0;
		$(".tpchange", prnt).each(function() {
			totp += parseFloat($(this).find("option:selected").data("pricecalc"));
		});
		$(".tpprice", prnt).each(function() {
			totp += parseFloat($(this).data("pricecalc"));
		});
		console.log(totp);
		$(".totprice", prnt).text("= ৳" + engToBen(totp.toFixed(2)));
	});
});

$("[data-page='exlayoutlist']").on("init", function() {
	setTimeout(function() {
		$("#layoutslisttable").find("th:first").click();
	}, 500);
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

function initMap() {
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

function initVendorMap() {
	var vloc = {
		lat : parseFloat($("#vlat").val()),
		lng : parseFloat($("#vlon").val())
	};
	// The map, centered at the Nursery
	var vmap = new google.maps.Map(document.getElementById('vendormap'), {
		zoom : 18,
		center : vloc
	});
	var marker = new google.maps.Marker({position: vloc, map: vmap});
}