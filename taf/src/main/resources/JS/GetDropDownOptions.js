data = new Array();
dropdown = arguments[0];

try {
	for (i = 0; i < dropdown.options.length; i++) {
		var item = new Object();
		item.visible = dropdown.options[i].text;
		item.value = dropdown.options[i].value;
		item.index = i;
		item.enabled = !dropdown.options[i].disabled;
		data.push(item);
	}
} catch (err) {
}

return JSON.stringify(data);