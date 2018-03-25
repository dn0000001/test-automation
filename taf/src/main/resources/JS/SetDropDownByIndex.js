dropdown = arguments[0];
index = arguments[1];

try {
	dropdown.selectedIndex = index;
	if (dropdown.selectedIndex == -1)
		return false; // If index is out of bounds
	else if (dropdown.selectedIndex != index)
		return false; // If index cannot be cast to an integer
	else
		return true;
} catch (err) {
	return false;
}