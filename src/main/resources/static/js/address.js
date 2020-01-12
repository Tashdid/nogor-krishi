$(document).ready(function () {
	$('input[type=radio][name=deliveryaddress-radio]:first').attr('checked', true);
	// type:0 for delivery and 1 for billing
	$(document).on('click', "#deliveryAddressBtn", function (e) {
		e.preventDefault();
		if ($("#delivery-address-id").val() != '')
			updateAdress('delivery', 0);
		else
			addAddressSubmit('delivery', 0);

	});

	$("#billingAddressBtn").click(function (e) {
		e.preventDefault();

		if ($("#billing-address-id").val() != '')
			updateAdress('billing', 1);
		else
			addAddressSubmit('billing', 1);
	});



	$("#newdeliveryAddress").click(function (e) {
		e.preventDefault();
		addNewAddressDiv('delivery');

	});

	$(document).on('click', ".editdeliveryAddress", function (e) {
		e.preventDefault();
		addNewAddressDiv('delivery');
		setAddressForm($(this), 'delivery');

	});

	$(document).on('click', ".editbillingAddress", function (e) {
		e.preventDefault();
		addNewAddressDiv('billing');
		setAddressForm($(this), 'billing');

	});

	$(document).on('click', ".deleteAddress", function (e) {
		e.preventDefault();
		var addressId = $(this).data("id");
		var prefix = $(this).data("prefix");

		Swal.fire({
			title: 'Are you sure?',
			text: "You won't be able to revert this!",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#345C05',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Yes, delete it!'
		}).then((result) => {
			if (result.value) {
				$.ajax({
					type: 'DELETE',
					url: "delete-user-address-preference/" + addressId,
					dataType: "json",
					contentType: "application/json; charset=utf-8",
					success: function () {
						$(`#${addressId}-${prefix}-address-list`).detach();
					},
					error: function () {
						Swal.fire({
							icon: 'error',
							title: 'Oops...',
							text: 'কিছু সমস্যা হয়েছে, কিছুক্ষণ পর আবার চেষ্টা করুন'
						})

					}

				});
			}

		})


	});

	function setAddressForm(_this, prefix) {
		parentDiv = _this.parents('.radio-container');
		addressDetail = parentDiv.find('.' + prefix + '-address-detail').text();
		district = parentDiv.find('.' + prefix + '-city').text();
		city = parentDiv.find('.' + prefix + '-district').text();

		$(".card-body" + prefix + " .card-text").css("color", "black");
		$("#" + prefix + "-city").val(city);
		$("#" + prefix + "-district").val(district);

		$("#" + prefix + "-address").val(addressDetail);
		$("#" + prefix + "-address-id").val(_this.data('id'));

	}

	$("#cancelnewdeliveryAddress").click(function (e) {
		e.preventDefault();
		cancelNewAddressDiv('delivery');

	});

	$("#newbillingAddress").click(function (e) {
		e.preventDefault();
		addNewAddressDiv('billing');

	});

	$("#cancelnewbillingAddress").click(function (e) {
		e.preventDefault();
		cancelNewAddressDiv('billing');

	});
});


function updateAdress(prefix, type) {
	var address = $("#" + prefix + "-address").val();
	var district = $("#" + prefix + "-district").val() == "" ? "" : $("#" + prefix + "-district option:selected").text();
	var city = $("#" + prefix + "-city").val();

	if (address != '' && district != '' && city != '') {
		var addressType = type;
		var addressId = $("#" + prefix + "-address-id").val();



		$.ajax({
			type: 'PUT',
			url: "update-user-address-preference/" + addressId,
			// url: "add-user-address-preference/",
			dataType: "json",
			contentType: "application/json; charset=utf-8",

			data: JSON.stringify({
				address,
				city,
				district,
				addressType
			}),
			success: function (cm) {
				
				var addressId = $("#" + prefix + "-address-id").val();
				var element = $(`#${addressId}-${prefix}-address-list`);
				// var _html="<div class=\"card-block\"><div class=\"card-text\" onclick=\"updateAddress(this,'"+prefix+"','"+data.address+"','"+data.city+"','"+data.district+"')\"><span>"+data.id+": "+data.address+", "+data.city+", "+data.district+"</span></div></div>"
				element.html(_addressBox(cm, prefix));
				cancelNewAddressDiv(prefix);
			}

		});
	} else {
		Swal.fire({
			icon: 'error',
			title: 'Oops...',
			text: 'সঠিক ঠিকানা দিন'
		})
	}

}

function _addressBox(cm, prefix) {
	return `
	<input class="form-check-input ${prefix}address-radio" type="radio" name="${prefix}address-radio" id="${cm.id + '-radio'}" 
	value="${cm.id}" checked="true" />
	<label class="form-check-label ${prefix}address-radio" for="${cm.id + '-radio'}" 
	>${cm.address}
	  
	</label>
	<span class="${prefix}-address-detail d-none" >${cm.address}</span>
	<p class="small ml-4">
		<span class="${prefix}-city" >${cm.city}</span>
		, <span class="${prefix}-district" >${cm.district}</span>
		<a class="link edit${prefix}Address" data-id="${cm.id}" href="javascript:void(0);">edit</a> .
		<a class="link deleteAddress" data-prefix='${prefix}' data-id="${cm.id}" href="javascript:void(0);">Delete</a>
	</p>
	`;
}


function addAddressSubmit(prefix, type) {

	var address = $("#" + prefix + "-address").val();
	var district = $("#" + prefix + "-district").val() == "" ? "" : $("#" + prefix + "-district option:selected").text();
	var city = $("#" + prefix + "-city").val();

	if (address != '' && district != '' && city != '') {

		var addressType = type;

		$.ajax({
			type: 'POST',
			url: "add-user-address-preference",
			dataType: "json",
			contentType: "application/json; charset=utf-8",

			data: JSON.stringify({
				address,
				city,
				district,
				addressType
			}),
			success: function (data) {
				// @todo handle exepction
				// alert("Successfully Added!");

				var element = $(".card-body" + prefix);
				var _html = `
			<div class="form-check radio-container" id="${data.id}-${prefix}-address-list">
				${_addressBox(data, prefix)}
			</div>
			`
				element.append(_html);
				cancelNewAddressDiv(prefix);
			},
			error: function () {
				Swal.fire({
					icon: 'error',
					title: 'Oops...',
					text: 'কিছু সমস্যা হয়েছে, কিছুক্ষণ পর আবার চেষ্টা করুন'
				})

			}

		});
	} else {
		Swal.fire({
			icon: 'error',
			title: 'Oops...',			
			alerttext: 'সঠিক ঠিকানা দিন'

		})
	}

}

// function updateAddress(element, prefix, address, city, district){
// 	$(".card-body"+prefix+" .card-text").css("color","black");
// 	$(element).css("color","blue");
// 	if(prefix=='delivery'){
// 		new_delivery_address=false;
// 	}
// 	else{
// 		new_billing_address=false;
// 	}
// 	$("#"+prefix+"-address").val(address);
// 	if(district){
// 		$("#"+prefix+"-district option:contains('"+district+"')").prop('selected', true);
// 		loadThana(prefix, city);
// 	}
// }

function addNewAddressDiv(prefix) {
	clearInput(prefix);
	$(".card-body" + prefix + "").hide();
	$("#cancelnew" + prefix + "Address").show();
	$("#new" + prefix + "Address").hide();
	$("." + prefix + "-addressDiv").show();
	if (prefix == 'delivery') {
		new_delivery_address = true;
	} else {
		new_billing_address = true;
	}
}

function cancelNewAddressDiv(prefix) {
	$(".card-body" + prefix + "").show();
	$("#cancelnew" + prefix + "Address").hide();
	$("#new" + prefix + "Address").show();
	$("." + prefix + "-addressDiv").hide();
	if (prefix == 'delivery') {
		new_delivery_address = false;
	} else {
		new_billing_address = false;
	}
}

function clearInput(prefix) {
	$(".card-body" + prefix + " .card-text").css("color", "black");
	$("." + prefix + "-addressDiv").find('select').val('');
	$("." + prefix + "-addressDiv").find('textarea').val('');
	$("#" + prefix + "-address-id").val('');

}

function loadThana(prefix, selectedCityValue) {
	console.log(prefix);
	var selectedValue = $("#" + prefix + "-district").val();
	$.ajax({
		type: 'GET',
		url: "/get-demographic-data-by-parent/" + selectedValue,

		success: function (data) {
			thanaList = data;

			$("#" + prefix + "-city").empty();
			var _html = '<option value="">শহর/থানা/উপজেলা বাছাই করুন…</option>';
			for (var i = 0; i < thanaList.length; i++) {
				if (selectedCityValue == thanaList[i].name) {
					_html += '<option value="' + thanaList[i].name + '" selected >' + thanaList[i].name + '</option>';
				} else {
					_html += '<option value="' + thanaList[i].name + '" >' + thanaList[i].name + '</option>';
				}
			}
			$("#" + prefix + "-city").html(_html);
		},
		error: function () {
			Swal.fire({
				icon: 'error',
				title: 'Oops...',
				text: 'কিছু সমস্যা হয়েছে, কিছুক্ষণ পর আবার চেষ্টা করুন'
			})

		}

	});
}