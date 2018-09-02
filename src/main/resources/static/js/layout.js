$("[data-page='layout']").on("init", function() {
	var conv = {'5': '৫', '10': '১০', '15': '১৫', '20': '২০', '25': '২৫', '30': '৩০', '35': '৩৫', '40': '৪০', '45': '৪৫', '50': '৫০', '55': '৫৫', '60': '৬০', '65': '৬৫', '70': '৭০', '75': '৭৫', '80': '৮০', '85': '৮৫', '90': '৯০', '95': '৯৫', '100': '১০০'}

	$("body").on("change", ".layoutgen", function() {
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
	}).on("change", "#types", function() {
		$("#home").load($(this).find("option:selected").data("remote"), function() {
			console.log("Should be loaded!");
			initDraggable();
		});
	}).on("click", "#savelayout", function() {
		var btn = $(this);
		var blocks = $("#tableov").find(".ui-draggable-handle");
		if (blocks.length === 0) {
			bootbox.alert("বাগান লে-আউট তৈরি করে সংরক্ষণ করুন");
			return false;
		}
		blockui();
		blocks.each(function(i, vl) {
			var ob = $(vl);
			ob.append('<input type="hidden" name="blocks[' + i + '].product" value="' + ob.data("id") + '"/>');
			ob.append('<input type="hidden" name="blocks[' + i + '].ptop" value="' + ob.position().top + '"/>');
			ob.append('<input type="hidden" name="blocks[' + i + '].pleft" value="' + ob.position().left + '"/>');
			ob.append('<input type="hidden" name="blocks[' + i + '].cheight" value="' + ob.height() + '"/>');
			ob.append('<input type="hidden" name="blocks[' + i + '].cwidth" value="' + ob.width() + '"/>');
		});
		html2canvas(document.getElementById("lyout")).then(function(canvas) {
			var imgbinary = canvas.toDataURL();
			imgbinary = imgbinary.substr(imgbinary.indexOf('base64,') + 7);
			$("#image").val(imgbinary);
			btn.parents("form").submit();
		});
		return false;
	});

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

	function initDraggable() {
		$('.draggable').draggable({
			helper: 'clone',
			start: function() { counts[0]++; }
		});
	}

	initDraggable();

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

				addedItem.addClass("gardenitem").attr("title", addedItem.data("name"));
				addedItem.find("a").remove();
				addedItem.find("span").remove();
				var cx = e.pageX;
				var dvx = $("#tableov").offset().left;
				addedItem.width("").height("");
				addedItem.css("left", e.pageX - dvx - (addedItem.width()/2)).css("top", e.pageY - $("#tableov").offset().top - (addedItem.height()/2));
				addedItem.removeClass("draggable ui-draggable ui-draggable-dragging tab-content-plant");

				addedItem.prepend('<a class="rmvme"><i class="glyphicon glyphicon-remove"/></a>');
				$(".rmvme", addedItem).click(function() {
					$(this).parent().remove();
					loadGardenDetails();
				});
				make_draggable(addedItem);
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
			gd += '<tr><td>' + checkAndGet(prd.data("name")) + '</td><td>' + checkAndGet(prd.data("prod")) + '</td><td>' + checkAndGet(prd.data("ben")) + '</td></tr>';
		}
	});
	$("#gdetails").html(gd);
}

function checkAndGet(text) {
	return text === undefined ? "" : text;
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