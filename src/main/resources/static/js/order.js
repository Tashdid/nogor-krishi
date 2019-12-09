/**
 * 
 */

var sub_total=0;
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
	
	calculateColumn();
	$("#sub_total").text(sub_total);
	var total=0;
	var deliveryCharge=parseInt($("#deliveryCharge").text());
	if (!isNaN(deliveryCharge)) {
		total+=deliveryCharge;
	}

	total+=sub_total;
	$("#total").text(total);
	

});

function calculateColumn() {
    $('#orderDetailBody tr .price').each(function() {
        var value = parseInt($(this).text());
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
		},
		error:function(data, errorThrown){
			alert('something went wrong!');
		}
	});
}
