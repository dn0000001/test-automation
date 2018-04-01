var inner = '';
var all = arguments[0].childNodes;
for (var i = 0; i < all.length; i++) {
  if (all[i].nodeType == 3) {
    inner += all[i].textContent.trim() + '\n';
  }
}
return inner;
