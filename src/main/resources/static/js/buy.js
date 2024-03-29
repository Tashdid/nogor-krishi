var myMap = {};
var new_delivery_address = false;
var new_billing_address = false;

$(document).ready(function () {

	$(document).on("click", ".add-to-cart", function () {

		// let saleTypeId = $('#select-type').val();
		let productPrice_id = $(this).data('id');
		let unitPrice = $(this).data('unitprice');
		// let productId = $("#product-id").val();

		let quantity = $(this).parents('tr').find('.quantity').val();

		$.ajax({
			type: 'POST',
			url: `${api_origin}/test/cart/add-to-cart/`,
			dataType: "json",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify({
				// sale_type_id : saleTypeId,
				// nursery_id : nurseryId,
				productPrice_id: productPrice_id,
				unit_price: unitPrice,
				quantity: quantity

			}),
			success: function (data) {
				updateCart();
				$('body').addClass('show-cart');

			}

		});


	});



	$(document).on("click", ".kk-btn-group", function () {

		if ($(this).hasClass('active')) { // unslelct this button group

			$(this).removeClass('active');

			var parId = $(this).parent().attr('id');
			var selectId = parId.replace('btn-grp-', '');

			$("#select-" + selectId).val(0);
			delete myMap[selectId];

		} else { // change or select a button group

			$(this).siblings().removeClass('active');
			$(this).addClass('active');

			var parId = $(this).parent().attr('id');
			var selectId = parId.replace('btn-grp-', '');

			$("#select-" + selectId).val($(this).data('vlu'));
			myMap[selectId] = $(this).data('vlu');
		}

	});

	$(document).on("click", "#get-price", function () {

		loadPrice(
			$("#bivag").val(),
			$("#zela").val(),
			$("#upozela").val()
		);
	});

	$(document).on("click", "#same-bill", function () {
		if ($(this).is(':checked'))
			$('.billing-addrss').attr('disabled', 'disabled');
		else
			$('.billing-addrss').removeAttr('disabled');


	});

	$(document).on("click", "#confirm-order", function () {

		let phone = $("#phone").val();

		selectedDeliveryAddressDiv = $('input[type=radio][name=deliveryaddress-radio]:checked').parents('.radio-container');

		var delivery_address = $(selectedDeliveryAddressDiv).children(".delivery-address-detail").text(); //$("#delivery-address").val();
		var delivery_district = $(selectedDeliveryAddressDiv).find(".delivery-district").text(); // $("#delivery-district").val()==""?"": $("#delivery-district option:selected").text();
		var delivery_city = $(selectedDeliveryAddressDiv).find(".delivery-city").text(); //$("#delivery-city").val();
		let delivery_notes = $("#delivery-notes").val();

		if ($('#same-bill').is(':checked')) {
			new_billing_address = false
			var billing_address = delivery_address;// $("#delivery-address").val();
			var billing_district = delivery_district;//$("#delivery-district").val()==""?"": $("#delivery-district option:selected").text();
			var billing_city = delivery_city;//$("#delivery-city").val();
		} else {

			var billing_address = $("#billing-address").val();
			var billing_district = $("#billing-district").val() == "" ? "" : $("#billing-district option:selected").text();
			var billing_city = $("#billing-city").val();
		}
		if (delivery_address && billing_address) {

			$(this).text('অর্ডারটি নিশ্চিত হচ্ছে ...');
			$(this).addClass('disabled');
			
			$.ajax({
				type: 'POST',
				url: `${api_origin}/test/confirm-order/`,
				dataType: "json",
				contentType: "application/json; charset=utf-8",

				data: JSON.stringify({
					phoneNo: phone,
					billing_address,
					billing_district,
					billing_city,
					delivery_address,
					delivery_district,
					delivery_city,
					new_delivery_address,
					new_billing_address,
					delivery_notes
				}),
				success: function (data) {
					// @todo handle exepction
					location.replace(`${api_origin}/order-confirmation/` + data.id);

				}

			});
		} else {
			Swal.fire({
				icon: 'error',
				title: 'Oops...',
				text: 'সঠিক ঠিকানা দিন'
			});
		}

	});


});

function loadPrice(bivag , zela, upozela ) {
	let productId = $("#product-id").val();
	let valueSt = "";
	$.each(myMap, function (index, value) {
		valueSt += "-" + value;
	});
	if (valueSt) {
		valueSt = valueSt.substring(1);
	}
	let demographicDataSt = "";
	//2 : upozela type
	//1 : zela type
	//0 : bivag type


	localStorage.setItem('loc', JSON.stringify(
		{
			division: bivag,
			district: zela,
			upozila: upozela
		}
	));

	if (upozela && upozela != "0") {
		demographicDataSt = "2-" + upozela;
	} else if (zela && zela != "0") {
		demographicDataSt = "1-" + zela;
	} else if (upozila && upozila != "0") {
		demographicDataSt = "0-" + upozila;
	}

	var urlSt = `${api_origin}/api/productpricesearch/${productId}/${valueSt}`;
	if (demographicDataSt) {
		urlSt = `${api_origin}/api/productpricesearch/${productId}/demographicdata/${demographicDataSt}/${valueSt}`;
	}
	$("#loading").show();
	$.ajax({
		type: 'GET',
		// url:
		// `${api_origin}/test/price-list/${productId}/saleType/${saleTypeId}`,
		url: urlSt,

		success: function (data) {
			$("#loading").hide();

			body = data.length ? '' : '<tr><td class="msg-buy text-danger">দুঃখিত, কিছু পাওয়া যায়নি। দয়া করে অন্য প্রপার্টি দিয়ে চেষ্টা করুন.</td></tr>';

			for (i = 0; i < data.length; i++) {

				var properties = '';
				for (j = 0; j < data[i].productPriceOnPropertyValueList.length; j++) {
					properties += `<p class="small">
						${data[i].productPriceOnPropertyValueList[j].productPropertyValue.productProperty.name}:
						${data[i].productPriceOnPropertyValueList[j].productPropertyValue.name}
					</p>`;
				}
				var button = data[i].quantity != undefined && data[i].quantity > 0 ?
					`<button data-unitprice=${data[i].price}
					data-id=${data[i].id} 
					class='add-to-cart btn btn-success'>
						কিনুন
				</button>` : `<button class="btn btn-default disabled">Stock out</button> `;
				tr = `
					<tr>
						<td> 
							<a target="_blank" href="/vendor/${data[i].nursery.id}">${data[i].nursery.name}</a>
							<p class="small">${data[i].nursery.area.name}, ${data[i].nursery.area.city.name}</p>
						</td>
						<td>${properties}</td>
						
						<td><h5>${data[i].price}</h5> টাকা</td>
						
						<td>
							<div class="quantity-cart">
								<div class="d-flex flex-column">
									<div class="qty mt-2">
										<span class="minus">-</span>
										<input type="number" class="count quantity" name="qty" value="1"/>
										<span class="plus">+</span>
									</div>
									<p class="small">${data[i].quantity == undefined ? 0 : data[i].quantity} Available</p>
								</div>
								
								<div>
									${button}
								</div>

								
							</div>
							
						</td>
						
					</tr>
				
				`;

				body += tr;
			}
			$('#price-list > tbody').html(body);

		},
		error: function (data) {
			$("#loading").hide();
			Swal.fire({
				icon: 'error',
				title: 'Oops...',
				text: 'কিছু সমস্যা হয়েছে, কিছুক্ষণ পর আবার চেষ্টা করুন'
			})
		}

	});
}