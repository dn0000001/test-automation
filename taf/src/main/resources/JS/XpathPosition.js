//
// Gets the previous sibling taking into account whitespace nodes
//
function prevSiblingNode(node)
{
    var prev = node.previousSibling;
    while (prev != null && prev.nodeType != 1)
    {
      prev = prev.previousSibling;
    }
    
    return prev;
}

//
// Gets the node position (in relation to the parent node)
//
function nodePosition(node)
{
    // For position start count at 1
    var i = 1;
    while((node = prevSiblingNode(node)) != null)
    {
      i++;
    }
    
    return i;
}

//
//Returns the xpath position (in relation to the parent node)
//
//Note:  This method may not return the correct position if the xpath used has a wild card
//
//Example:  //*/div => OK but //div/* => if there is a mix of node names, then the position may not be correct only the same node names are considered
//
function nodeXPathPosition(node)
{
 // For position start count at 1
 var i = 1;
	
	// Save node name to find the xpath position 
	var matchNodeName = node.nodeName;
	
 while((node = prevSiblingNode(node)) != null)
 {
		// Only count if same node name
		if(matchNodeName == node.nodeName)
			i++;
 }
 
 return i;
}

// Returns the node position of the given WebElement
if(arguments[1])
	return nodePosition(arguments[0]);
else
	return nodeXPathPosition(arguments[0]);
