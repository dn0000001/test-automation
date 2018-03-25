//
// Get entire table using javascript
// Note: Trims cell values.
//
var data = new Object();
var maxColumnLength = -1;
var rows = new Array();

try {
	var table = arguments[0];
	var rowLength = table.rows.length;

	for (i = 0; i < rowLength; i++) {
		var cols = new Array();

		// Gets cells of current row
		var cells = table.rows.item(i).cells;
		var cellLength = cells.length;
		for (var j = 0; j < cellLength; j++) {
			var value;
			element = cells.item(j);
			try {
				if (element.textContent == undefined)
					value = element.innerText;
				else
					value = element.textContent;
			} catch (e) {
				value = "";
			}

			// Workaround for IE8 that does not support trim()
			try {
				value = value.trim();
			} catch (e) {
				value = value.replace(/^\s+|\s+$/gm,'');
			}
			
			var colObj = new Object();
			colObj.value = value;

			cols.push(colObj);
		}

		if (cellLength > maxColumnLength)
			maxColumnLength = cellLength;

		var rowObj = new Object();
		rowObj.columns = cols;

		rows.push(rowObj);
	}
} catch (err) {
}

data.rows = rows;
data.maxColumnLength = maxColumnLength;

return JSON.stringify(data);