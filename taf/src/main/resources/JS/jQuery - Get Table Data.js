//
// Get table data using jQuery
//
var data = new Array();

var table = $("#tableID");
table.find('tr').each(
		function(i, el) {
			var $tds = $(this).find('td');

			var item = new Object();
			item.index = i;
			item.field1 = $tds.filter('[attribute=value]').text().trim();

			data.push(item);
		});

return JSON.stringify(data);