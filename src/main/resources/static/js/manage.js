$(document).ready(function() {
	var path = window.location.pathname;
	var parts = path.split("/");
	if (parts.length > 3) {
		path = "/" + parts[1] + "/" + parts[2];
	}
	$(".dropdown-item[href='" + path + "']").addClass("active");

	$(".uploadimg").on("change", function(){
		showImage(this);
	});

	loadDataTables($("body"));

	var rmmeco = $.cookie("clickme");
	if (rmmeco) {
		$.removeCookie("clickme");
		$("#" + rmmeco).trigger("click");
	} else if ($(".clickonload").length > 0) {
		$(".clickonload").trigger("click");
	}
});

function loadDataTables(target) {
	$('table.dttable', target).each(function() {
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

function initLocationMap() {
	var cus_mark;
	// The map, centered at the Nursery
	var nmap = new google.maps.Map(document.getElementById('locationmap'), {
		zoom : 16,
		center : dhaka
	});

	var nlat = $("#latitude").val();
	var nlon = $("#longitude").val();
	if (nlat !== "" && nlon !== "" && !isNaN(nlat) && !isNaN(nlon)) {
		console.log("Hello budy!!!");
		var nloc = {
			lat : parseFloat(nlat),
			lng : parseFloat(nlon)
		};
		cus_mark = new google.maps.Marker({
			position: nloc,
			draggable: true,
			map: nmap
		});
		setDragEndEvent(cus_mark);
		nmap.setCenter(nloc);
	}

	nmap.addListener('click', function(e) {
		if (cus_mark !== undefined) {
			cus_mark.setMap(null);
		}

		cus_mark = new google.maps.Marker({
			position: e.latLng,
			draggable: true,
			map: nmap
		});
		setLatLon(e);
		setDragEndEvent(cus_mark);
	});
}

function setLatLon(evt) {
	$("#latitude").val(evt.latLng.lat().toFixed(6));
	$("#longitude").val(evt.latLng.lng().toFixed(6));
}

function setDragEndEvent(marker) {
	google.maps.event.addListener(marker, 'dragend', function(evt) {
		setLatLon(evt);
		console.log('Marker dropped: Current Lat: ' + evt.latLng.lat().toFixed(6) + ' Current Lng: ' + evt.latLng.lng().toFixed(6));
	});
}

$("[data-page='nursery']").on("init", function(){
	bindNurseryPrice();
});

function bindNurseryPrice() {
	$("body").on("change", "#product", function() {
		var prs = $(this);
		var stypes = "" + prs.find("option:selected").data("stypes") + "";
		var st = stypes.indexOf(',') === -1 ? [stypes] : stypes.split(",");
		var sts = $("#saleType");
		sts.empty();
		$.each(st, function(i) {
			sts.append($('<option>').text($("#stp-" + st[i]).val()).val(st[i]));
		});
	});
	$("#product").trigger("change");
}