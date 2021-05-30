//
// Attach MutationObserver to the specified element.
// Note:  Any previous MutationObserver added by automation should be disconnected
// as the variable used to store it will be overridden.
//
var element = arguments[0];

window.automation_last_attribute_value = null;
window.automation_attribute_added = false;
window.automation_attribute_removed = false;

window.automation_observer = new MutationObserver(function(mutations) {
    mutations.forEach(function(mutation) {
    if (mutation.type == "attributes") {
        let current_attribute_exists = mutation.target.attributes[mutation.attributeName] !== undefined;
        let current_attribute_removed = !current_attribute_exists && mutation.oldValue !== null;
        if (current_attribute_removed) {
            window.automation_attribute_removed = true;
        }

        if (!current_attribute_removed
            && current_attribute_exists
            && mutation.oldValue === null
            && window.automation_last_attribute_value === null
        ) {
            window.automation_attribute_added = true;
        }

        window.automation_last_attribute_value = current_attribute_exists ? true : null;
    }
  });
});

window.automation_observer.observe(element, {attributes: true, attributeOldValue: true});
