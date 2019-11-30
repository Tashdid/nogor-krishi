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
	
	$("#notesAddForm").submit(function(e) {

		e.preventDefault();

		ajaxFormSubmit($(this));

	});
	
	$("#feedbackUpdateForm").submit(function(e) {

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
			alert("successfully changed!");
		},
		error:function(data, errorThrown){
			alert('something went wrong!');
		}
	});
}
