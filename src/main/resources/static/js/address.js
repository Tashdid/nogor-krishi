
// type:0 for delivery and 1 for billing
$("#deliveryAddressBtn").click(function(e) {
	e.preventDefault();
	ajaxFormSubmit('delivery', 0);
});

$("#billingAddressBtn").click(function(e) {
	e.preventDefault();
	ajaxFormSubmit('billing', 1);
});

function ajaxFormSubmit(prefix, type){
	var address = $("#"+prefix+"-address").val();
	if(address){
	var district = $("#"+prefix+"-district").val()==""?"": $("#"+prefix+"-district option:selected").text();
	var city = $("#"+prefix+"-city").val();
	var addressType=type;

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
		success: function(data) {
			// @todo handle exepction
			alert("Successfully Added!");
			
			var element=$(".card-body"+prefix+" .bg-faded");
			var _html="<div class=\"card-block\"><div class=\"card-text\" onclick=\"updateAddress(this,'"+prefix+"','"+data.address+"','"+data.city+"','"+data.district+"')\"><span>"+data.id+": "+data.address+", "+data.city+", "+data.district+"</span></div></div>"
			element.append(_html);
			cancelNewAddressDiv(prefix);
		}

	});
	}else{
		alert("Insert Valid Address");
	}

}

function updateAddress(element, prefix, address, city, district){
	$(".card-body"+prefix+" .card-text").css("color","black");
	$(element).css("color","blue");
	if(prefix=='delivery'){
		new_delivery_address=false;
	}
	else{
		new_billing_address=false;
	}
	$("#"+prefix+"-address").val(address);
	if(district){
		$("#"+prefix+"-district option:contains('"+district+"')").prop('selected', true);
		loadThana(prefix, city);
	}
}

$("#newdeliveryAddress").click(function(e) {
	e.preventDefault();
	addNewAddressDiv('delivery');

});

$("#cancelnewdeliveryAddress").click(function(e) {
	e.preventDefault();
	cancelNewAddressDiv('delivery');

});

$("#newbillingAddress").click(function(e) {
	e.preventDefault();
	addNewAddressDiv('billing');

});

$("#cancelnewbillingAddress").click(function(e) {
	e.preventDefault();
	cancelNewAddressDiv('billing');

});

function addNewAddressDiv(prefix){
	clearInput(prefix);
	$(".card-body"+prefix+"").hide();
	$("#cancelnew"+prefix+"Address").show();
	$("#new"+prefix+"Address").hide();
	$("."+prefix+"-addressDiv").show();
	if(prefix=='delivery'){
		new_delivery_address=true;
	}else{
		new_billing_address=true;
	}
}

function cancelNewAddressDiv(prefix){
	$(".card-body"+prefix+"").show();
	$("#cancelnew"+prefix+"Address").hide();
	$("#new"+prefix+"Address").show();
	$("."+prefix+"-addressDiv").hide();
	if(prefix=='delivery'){
		new_delivery_address=false;
	}else{
		new_billing_address=false;
	}
}

function clearInput(prefix){
	$(".card-body"+prefix+" .card-text").css("color","black");
	$("."+prefix+"-addressDiv").find('select').val('');  
	$("."+prefix+"-addressDiv").find('textarea').val('');  
}

function loadThana(prefix, selectedCityValue){
	console.log(prefix);
	var selectedValue=$("#"+prefix+"-district").val();
	$.ajax({
		type: 'GET',
		url: "/get-demographic-data-by-parent/"+selectedValue,
		
		success: function(data) {
			thanaList=data;

			$("#"+prefix+"-city").empty();
			var _html='<option value="">শহর/থানা/উপজেলা বাছাই করুন…</option>';
			for(var i=0;i<thanaList.length;i++){
				if(selectedCityValue==thanaList[i].name){
					_html+='<option value="'+thanaList[i].name+'" selected >'+thanaList[i].name+'</option>';
				}else{
				_html+='<option value="'+thanaList[i].name+'" >'+thanaList[i].name+'</option>';
				}
			}
			$("#"+prefix+"-city").html(_html);
		},
		error: function(data) {
			alert("error");
		}
	
	});
}