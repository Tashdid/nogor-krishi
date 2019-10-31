$(document).ready(function() {
	
	$(document).on("click", ".add-to-cart", function(){
	  
		let saleTypeId = $('#salesTypeOpt').val();
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
				console.log(data);
				updateCart();
	            
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
	
	$(document).on("click", "#get-price", function(){
		  
		let productId = $("#product-id").val();
		let saleTypeId = $('#salesTypeOpt').val();
  
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
								Quantity : <input type="number" class="quantity" value="1" />
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