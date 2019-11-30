$(document).ready(function() {
	
	$(document).on("click", ".add-to-cart", function(){
	  
		let saleTypeId = $('#select-type').val();
		let nurseryId = $(this).data('nurseryid');
		let unitPrice = $(this).data('unitprice');
		let productId = $("#product-id").val();

		let quantity = $(this).parents('tr').find('.quantity').val();

		$.ajax({
	        type: 'POST',
	        url: `${api_origin}/test/cart/add-to-cart/`,
			dataType: "json",
			contentType: "application/json; charset=utf-8",
	        data: JSON.stringify({
				sale_type_id : saleTypeId,
				nursery_id : nurseryId,
				product_id : productId,
				unit_price : unitPrice,
				quantity : quantity
				
	        }),
	        success: function(data) {
				updateCart();
				$('body').addClass('show-cart');
	            
	        }

	    });
	  
	  
	});
	

	
	$(document).on("click", ".kk-btn-group", function(){ 
		$(this).siblings().removeClass('active');
		$(this).addClass('active');
		var parId = $(this).parent().attr('id');
		var selectId = parId.replace('btn-grp-', '');

		$("#select-" + selectId).val($(this).data('vlu'));
	});

	$(document).on("click", "#get-price", function(){
		  
		let productId = $("#product-id").val();
		let saleTypeId = $('#select-type').val();//@todo make it dynamic
  
		$.ajax({
			type: 'GET',
			url: `${api_origin}/test/price-list/${productId}/saleType/${saleTypeId}`,
			dataType: "json",
			//		        data: {
			//		            sale: "Japan"
			//		        },
		    success: function(data) {
//		    	data = JSON.parse(data);
		    	body = data.length ? '' : '<tr><td class="msg-buy">দুঃখিত, কিছু পাওয়া যায়নি। দয়া করে অন্য প্রপার্টি দিয়ে চেষ্টা করুন.</td></tr>';
		        for(i=0; i < data.length; i++) {
		        	tr = `
		        		<tr>
		        			<td> 
								<a target="_blank" href="/vendor/${data[i].nursery.id}">${data[i].nursery.name}</a>
								<p class="small">${data[i].nursery.area.name}, ${data[i].nursery.area.city.name}</p>
							</td>
							<td>${data[i].price} টাকা</td>
							
							<td>
								<div class="quantity-cart">
									<div class="qty mt-2">
										<span class="minus">-</span>
										<input type="number" class="count quantity" name="qty" value="1"/>
										<span class="plus">+</span>
									</div>
									<div>
										<button data-unitprice=${data[i].price}
											data-nurseryid=${data[i].nursery.id} 
											class='add-to-cart btn btn-default'>
												কিনুন
										</button>
									</div>

									
								</div>
								
							</td>
							
		        		</tr>
		        	
		        	`;
		        	
		        	body += tr;
		        }
		        $('#price-list > tbody').html(body);
		        
		    }
		
		});
		  
		  
	});

	$(document).on("click", "#same-bill", function(){
		if($(this).is(':checked'))
			$('.billing-addrss').attr('disabled', 'disabled');
		else 
			$('.billing-addrss').removeAttr('disabled');
			
	
	});
	
	$(document).on("click", "#confirm-order", function(){
		  
		let phone = $("#phone").val();
		let billing_address = $("#billing-address").val();
		let billing_district = $("#billing-district").val();
		let billing_city = $("#billing-city").val();
		let delivery_notes = $("#delivery-notes").val();

		if($('#same-bill').is(':checked')) {
			var delivery_address = $("#billing-address").val();
			var delivery_district = $("#billing-district").val();
			var delivery_city = $("#billing-city").val();
		} else {
			var delivery_address = $("#delivery-address").val();
			var delivery_district = $("#delivery-district").val();
			var delivery_city = $("#delivery-city").val();
		}

		
		$.ajax({
			type: 'POST',
			url: `${api_origin}/test/confirm-order/`,
			dataType: "json",
			contentType: "application/json; charset=utf-8",
	        
			data: JSON.stringify({
				phoneNo:phone,
				billing_address,
				billing_district,
				billing_city,
				delivery_address,
				delivery_district,
				delivery_city,
				delivery_notes
			}),
			success: function(data) {
				// @todo handle exepction
				location.replace(`${api_origin}/order-confirmation/` + data.id);
	
			}
		
		});
	
	});

    
});