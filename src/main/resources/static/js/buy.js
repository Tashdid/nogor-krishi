$(document).ready(function() {
	
	$(document).on("click", ".add-to-cart", function(){
	  
		let saleTypeId = $('#select-type').val();
		let nurseryId = $(this).data('nurseryid');
		let unitPrice = $(this).data('unitprice');
		let productId = $("#product-id").val();

		let quantity = $(this).parents('tr').find('.quantity').val();

		$.ajax({
	        type: 'POST',
	        url: 'http://localhost:8080/test/cart/add-to-cart/',
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
	$(document).on("click", "#confirm-order", function(){
	  
		let phone = $('#phone').val();
		let deliveryAddress = $("deliveryAddress").data('nurseryid');
		let deliveryArea = $("#deliveryArea").data('unitprice');
		let productId = $("#product-id").val();

		
		$.ajax({
	        type: 'POST',
	        url: 'http://localhost:8080/test/confirm-order/',
			dataType: "json",
			contentType: "application/json; charset=utf-8",
	        data: JSON.stringify({
				phone : phone,
				deliveryAddress : deliveryAddress,
				deliveryArea : deliveryArea,
				productId : productId
				
	        }),
	        success: function(data) {
				console.log(data);
	        
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
			url: `http://localhost:8080/test/price-list/${productId}/saleType/${saleTypeId}`,
			dataType: "json",
			//		        data: {
			//		            sale: "Japan"
			//		        },
		    success: function(data) {
//		    	data = JSON.parse(data);
		    	body = '';
		        for(i=0; i < data.length; i++) {
		        	tr = `
		        		<tr>
		        			<td>${data[i].price} Taka</td>
							<td> 
								<p>${data[i].nursery.name}</p>
								<p class="small">${data[i].nursery.area.name}, ${data[i].nursery.area.city.name}</p>
							</td>
							<td>
								
								<div class="qty mt-5">
									<span class="minus bg-dark">-</span>
									<input type="number" class="count quantity" name="qty" value="1"/>
									<span class="plus bg-dark">+</span>
								</div>
							</td>
							<td>
								<button data-unitprice=${data[i].price}
									data-nurseryid=${data[i].nursery.id} 
									class='add-to-cart btn btn-primary'>
										Add to cart
								</button>
							</td>
		        		</tr>
		        	
		        	`;
		        	
		        	body += tr;
		        }
		        $('#price-list > tbody').html(body);
		        
		    }
		
		});
		  
		  
	});
	
	$(document).on("click", "#confirm-order", function(){
		  
		let phone = $("#phone").val();
		let address = $("#address").val();
		$.ajax({
			type: 'POST',
			url: `http://localhost:8080/test/confirm-order/${phone}/${address}`,
			dataType: "json",
			
			//		        data: {
			//		            sale: "Japan"
			//		        },
			success: function(data) {
				location.replace("http://localhost:8080/order-confirmation/" + data.id);
	
			}
		
		});
	
	});

    
});