window.chartColors = {
	red: 'rgb(255, 99, 132)',
	orange: 'rgb(255, 159, 64)',
	yellow: 'rgb(255, 205, 86)',
	green: 'rgb(75, 192, 192)',
	blue: 'rgb(54, 162, 235)',
	purple: 'rgb(153, 102, 255)',
	grey: 'rgb(201, 203, 207)',
	lime: 'rgb(0, 255, 0)',
	cyan: 'rgb(0, 255, 255)',
	maroon: 'rgb(128, 0, 0)',
	olive: 'rgb(128, 128, 0)',
	navy: 'rgb(0, 0, 128)'
};

window.chartColorsArray = Object.values(window.chartColors);

var color = Chart.helpers.color;

var genericLabel = function(tooltipItem, data) {
	var allData = data.datasets[tooltipItem.datasetIndex].data;
	var tooltipLabel = data.labels[tooltipItem.index];
	var tooltipData = allData[tooltipItem.index];
	var total = 0;
	for (var i in allData) {
		total += allData[i];
	}
	var tooltipPercentage = Math.round((tooltipData / total) * 100);
	return tooltipLabel + ' - ' + engToBen("" + tooltipPercentage) + '%';
}

var ponnoPieOptions = {
	responsive: true,
	title: {
		display: true,
		text: 'পণ্য তালিকা'
	},
	tooltips: {
		callbacks: {
			label: genericLabel
		}
	}
};

var dynamicColors = function(i) {
	if (i >= window.chartColorsArray)
		i = i % window.chartColorsArray;
	return color(window.chartColorsArray[i]).alpha(0.5).rgbString();
};

function loadPonnoPieChart() {
	$.get("/dashboard/ponno", function(rs) {
		var coloR = [];
		for (var i = 0; i < rs.data.length; i++) {
			coloR.push(dynamicColors(i));
		}
		ponnoPieOptions.title.text = "পণ্য তালিকা (" + rs.total + ")";
		var ctx = document.getElementById("ponnocanvas").getContext("2d");
		var myPieChart = new Chart(ctx, {
			type: 'pie',
			data: {
				datasets: [{
					backgroundColor: coloR,
					borderColor: 'rgba(200, 200, 200, 0.75)',
					hoverBorderColor: 'rgba(200, 200, 200, 1)',
					data: rs.data
				}],
				labels: rs.labels
			},
			options: ponnoPieOptions
		});
	});
}

$("[data-page='dashboard']").on("init", function() {
	loadPonnoPieChart();
	loadNurseryBarChart();
});

function loadNurseryBarChart() {
	$.get("/dashboard/nursery", function(rs) {
		ponnoPieOptions.title.text = "পণ্য তালিকা (" + rs.total + ")";
		var ctx = document.getElementById("nurserycanvas").getContext("2d");
		var nurseryBarChart = new Chart(ctx, {
			type: 'bar',
			data: {
				datasets: [{
					label: 'পণ্য',
					backgroundColor: color(window.chartColors.blue).alpha(0.5).rgbString(),
					data: rs.data1
				}, {
					label: 'সরঞ্জাম',
					backgroundColor: color(window.chartColors.red).alpha(0.5).rgbString(),
					data: rs.data2
				}],
				labels: rs.labels
			},
			options: {
				responsive: true,
				title: {
					display: true,
					text: 'নার্সারি ও নার্সারিতে প্রাপ্ত পণ্য ও সরঞ্জাম'
				},
				tooltips: {
					callbacks: {
						label: function(tooltipItem, data) {
							var tooltipLabel = data.datasets[tooltipItem.datasetIndex].label;
							var tooltipData = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
							return tooltipLabel + ': ' + engToBen("" + tooltipData);
						}
					}
				}
			}
		});
	});
}