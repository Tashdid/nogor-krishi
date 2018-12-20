
$("[data-page='suggestion']").on("init", function() {
	initSummerNote($('#comment'));
});

$("[data-page='newsuggestion']").on("init", function() {
	initSummerNote($('#content'));
});

$('#autocomplete').autocomplete({
	source: '/suggestions/search',
	select: function(event, ui) {
		location.href = "/suggestions/" + ui.item.key;
	}
});