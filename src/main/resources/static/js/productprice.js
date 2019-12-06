/**
 * 
 */

$(document).ready(function() {

//	$("#statusUpdateForm").submit(function(e) {
//
//		e.preventDefault();
//
//		ajaxFormSubmit($(this));
//
//	});
//	
	

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

function getProperty(){
	alert('test');
}

$(function(){
    // bind change event to select
    $('#product').on('change', function () {
        var url = '/nursery/productprice/'+$(this).val(); // get selected value
        if (url) { // require a URL
            window.location = url; // redirect
        }
        return false;
    });
  });
