//
// Finds the siblings of the node (WebElement)
//

findSiblingsOf = arguments[0];
var data = new Array();
index = 0;

elements = findSiblingsOf.parentNode.childNodes;
for (i = 0; i < elements.length; i++) {
	if (elements[i].nodeType == 1 && findSiblingsOf != elements[i]){
		data[index] = elements[i];
		index++;
	}
}

return data;