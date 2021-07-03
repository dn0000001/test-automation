var targetUrl = arguments[0];
var headers = JSON.parse(arguments[1]);
var result;

function bufferToBase64(buf) {
  var binstr = Array.prototype.map.call(buf, function(ch) {
    return String.fromCharCode(ch);
  }).join('');

  // Note:  This method is proprietary to the DOM
  return btoa(binstr);
}

function getPromise() {
  return new Promise(function(resolve, reject) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", targetUrl, true);
    xhr.responseType = "arraybuffer";
    xhr.onload = function() {
      result = new Object();
      result.status = this.status;
      result.file = bufferToBase64(new Uint8Array(this.response));
      resolve(result);
    };

    for (let header of headers) {
      xhr.setRequestHeader(header.name, header.value);
    }

    xhr.send(null);
  });
}

async function makeSynchronousRequest() {
  // wait to http request to finish
  await getPromise();

  // below code will be executed after http request is finished
  return JSON.stringify(result);
}

return makeSynchronousRequest();
