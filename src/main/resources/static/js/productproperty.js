/**
 * 
 */

$(document).ready(function() {

	$("#saveproductproperty").submit(function(e) {
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
			appendrow(form);
		},
		error:function(data, errorThrown){
			alert('something went wrong!');
		}
	});
}

function loadProperty(id, name, chk){
	$("#id").attr("value",id);
	$("#name").attr("value",name);
	$("#isDisabled").prop('checked', chk);
}

function appendrow(bean) {
	var html_='<tr>'
				+'<td th:text="'+bean.id+'"></td>'
				+'<td th:text="'+bean.name+'"></td>'
				+'<td>'
					+'<input type="checkbox" readonly="readonly" th:field="'+bean.isDisabled+'"/>'
				+'</td>'
				+'<td>'
				+'	<a class="btn btn-info btn-sm pull-right"><span class="glyphicon glyphicon-pencil"></span></a>'
				+'	<button type="button" class="btn btn-danger btn-sm pull-right rmvbtn" th:attr="data-remote=@{\'/manage/productproperty/\' + ${bean.id}}"><span class="glyphicon glyphicon-trash"></span></button>'
				+'</td>'
			 +'</tr>';
	$("#productpropertybody").append(html_);
	
}