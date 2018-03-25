//
// Finds the children of the node (WebElement)
//

findChildrenOf = arguments[0];
var data = new Array();
index = 0;

elements = findChildrenOf.childNodes;
for (i = 0; i < elements.length; i++) {
	if (elements[i].nodeType == 1){
		data[index] = elements[i];
		index++;
	}
}

return data;