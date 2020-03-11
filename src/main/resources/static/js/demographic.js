$(document).ready(function() {
	loadDemographicData($("#locType"), true);
});

function loadDemographicData(selectElement, isEditing = false){
	
	var cValue = $(selectElement).val()
	switch(cValue){
		case "0": // bivag
			$("#bivag").hide();
			$("#zela").hide();
			$('#parentId').val('');
		break;
		case "1": // zilla
			$("#bivag").show();
			$("#zela").hide();
			if (isEditing) {
				$('#bivagselect').val($("#parentId").val());
			}
			$('#parentId').val($('#bivagselect').val());
		break;
		case "2": // upozilla
			$("#bivag").show();
			$("#zela").show();
			if (isEditing) {
				$('#bivagselect').val($("#parentBivagId").val());
				loadChildDemographicData($("#bivagselect"),"zelaselect", null, false, true);
				
			}else{
				$('#parentId').val($('#zelaselect').val());
			}
		break;
	}
}

function setParentId(_this) {
	if($('#locType').val() == 2) {
		$('#parentId').val($(_this).val());
	}
}

function loadChildDemographicData(selectElement, targetElementId, dependentElement, isPreselectionNeeded, isEditing = false){

	if($('#locType').val() == 1) {
		$('#parentId').val($(selectElement).val());
	}
	if($(selectElement).val()!=0){
		$.ajax({
			type: 'GET',
			url: "/get-demographic-data-by-parent/"+$(selectElement).val(),
			
			success: function(data) {
				$("#"+targetElementId).empty();
				var _html='';
				if(data && data.length && !isPreselectionNeeded){
					_html='<option value=0> Please Select</option>';
				}
				for(var i=0;i<data.length;i++){
					_html+='<option value="'+data[i].id+'" >'+data[i].name+'</option>';
				}
				$("#"+targetElementId).html(_html);

				if(dependentElement!=null){
					$("#"+dependentElement).empty();
				}
				if(isEditing){
					$('#zelaselect').val($("#parentId").val());
					$('#parentId').val($('#zelaselect').val());
				}
				// if(data.length && data[0].type==1){
				// 	loadChildDemographicData($("#"+targetElementId),dependentElement, null, isPreselectionNeeded);
				// }
			},
			error: function(data) {
				alert("error");
			}
		
		});
	}
}