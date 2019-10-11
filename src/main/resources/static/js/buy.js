$(document).ready(function() {
	
	$(document).on("click", ".add-to-cart", function(){
	  
		let saleTypeId = $('#salesTypeOpt').val();
		let nurseryId = $(this).data('nurseryid');
		let unitPrice = $(this).data('unitprice');
		let productId = $("#product-id").val();

		let quantity = $(".quantity").val();
	  
		$.ajax({
	        type: 'POST',
	        url: 'http://localhost:8080/test/cart/add-to-cart/',
	        dataType: "json",
	        data: {
				sale_type_id : saleTypeId,
				nursery_id : nurseryId,
				product_id : productId,
				unit_price : unitPrice,
				quantity : quantity
				
	        },
	        success: function(data) {
				console.log(data);
				updateCart();
	            
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
	
	$(document).on("click", ".", function(){
		  
		
		  
	});

    
});