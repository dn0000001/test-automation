var allAttributes = new Map();

var attributes = arguments[0].attributes;
for (let attr in attributes) {
    allAttributes.set(attributes[attr].name, attributes[attr].value);
}

return JSON.stringify(Object.fromEntries(allAttributes));
