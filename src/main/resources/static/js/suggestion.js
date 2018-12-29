
$("[data-page='suggestion']").on("init", function() {
	initSummerNote($('#comment'));
});

$("[data-page='newsuggestion']").on("init", function() {
	initSummerNote($('#content'));
});