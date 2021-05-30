var data = new Object();

data.attributeAdded = window.automation_attribute_added;
data.attributeRemoved = window.automation_attribute_removed;

return JSON.stringify(data);
