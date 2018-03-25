//
// Example to get data with jQuery
//

locator = "div div div ul li";
$(locator).each(function(index) {
	data = $.data(this);
	console.log(data);
});

return data;