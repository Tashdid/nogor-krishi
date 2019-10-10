$(document).ready(function() {
	
	$(document).on("click", ".add-to-cart", function(){
	  
		let saleTypeId = $('#salesTypeOpt').val();
		let nurseryId = $(this).data('nurseryid');
		debugger;
	  
		$.ajax({
	        type: 'POST',
	        url: '/add-to-cart',
	        dataType: "json",
	        async:false,
	        data: {
	            Country: "Japan"
	        },
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
			async:false,
			//		        data: {
			//		            sale: "Japan"
			//		        },
		    success: function(data) {
//		    	data = JSON.parse(data);
		    	body = '';
		        for(i=0; i < data.length; i++) {
		        	tr = `
		        		<tr>
		        			<td>${data[i].price}</td>
		        			<td>${data[i].nursery.name}</td>
		        			<td>${data[i].nursery.id}</td>
		        			<td><button data-nurseryid=${data[i].nursery.id} class='add-to-cart btn btn-primary'>Add to cart</button></td>
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