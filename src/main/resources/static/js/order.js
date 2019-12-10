/**
 * 
 */

var sub_total=parseFloat('0.0');
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

function calculateColumn() {
    $('#orderDetailBody tr .price').each(function() {
        var value = parseFloat($(this).text());
        if (!isNaN(value)) {
        	sub_total += value;
        }
    });
}

function ajaxFormSubmit(form){
	var url = form.attr('action');
	$.ajax({
		type : "POST",
		url : url,
		data : form.serialize(),
		success : function(data) {
			alert("successfully changed!");
			location.reload();
		},
		error:function(data, errorThrown){
			alert('something went wrong!');
		}
	});
}
