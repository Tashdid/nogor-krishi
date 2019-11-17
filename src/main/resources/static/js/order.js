/**
 * 
 */

$(document).ready(function() {

	$("#statusUpdateForm").submit(function(e) {

		e.preventDefault();

		ajaxFormSubmit($(this));

	});
	
	$("#deliveryUpdateForm").submit(function(e) {

		e.preventDefault();

		ajaxFormSubmit($(this));

	});

});

function ajaxFormSubmit(form){
	var url = form.attr('action');
	$.ajax({
		type : "POST",
		url : url,
		data : form.serialize(),
		success : function(data) {
			console.log("success");
		},
		error:function(data, errorThrown){
			console.log(errorThrown);
		}
	});
}
