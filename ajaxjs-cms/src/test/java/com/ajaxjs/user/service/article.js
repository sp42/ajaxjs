function alert(msg) {
    WScript.Echo(msg);
}


// Open the input dialog box using a function in the .wsf file.
var url = WSHInputBox('Pls input the url', title, "http://www.1000rrtt.com/yazhourenti/2017/1204/12496_4.html");


//WScript.Echo(t.match(/src="[^"]*"\s?\/><\/p><br\/>/gm).length);


function post(url) {

}

function get(url) {
    var srvXMLHTTP = new ActiveXObject("Msxml2.ServerXMLHTTP.6.0");
    srvXMLHTTP.onreadystatechange = function() {
        if (srvXMLHTTP.readyState === 4 && srvXMLHTTP.status === 200) {
            //var html = srvXMLHTTP.responseText;
            var html = gb2312toUtf8(srvXMLHTTP.responseBody);
            saveFolder += html.match(/<title>(.*)<\/title>/)[1].replace(/\(\d+\)/, '')
            createFolder(saveFolder);
            var arr = html.match(/src="[^"]*"\s?\/><\/p><br\/>/gm);
            if (arr && arr.length) {
                var last = arr.pop(),
                    lastImg = last.match('src="([^"]*)"').pop();
                lastImg.split('/').pop().match(/\d+/)

                var totalImg = Number(lastImg.split('/').pop().match(/\d+/)[0]);

                var imgTpl = lastImg.replace(new RegExp(totalImg + '\\.(jpg|png|gif)$'), '{0}.$1');

                var pics = new Array(totalImg);
                for (var i = 0; i < totalImg; i++) {
                    pics[i] = imgTpl.replace('{0}', i + 1);
                    getPic(pics[i]);
                }

                //getPic(pics[1]);
            } else
                WScript.Echo('no pic');
        }
    }

    srvXMLHTTP.open("GET", url, false);
    srvXMLHTTP.send();
}

function getPic(url) {
    var fileName = url.split('/').pop();
    var xhr = new ActiveXObject("Msxml2.ServerXMLHTTP.6.0");

    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            saveFileTo(xhr.responseBody, saveFolder + "\\" + fileName);
        }
    }

    xhr.open("GET", url, false);
    xhr.send();
}

function createFolder(saveFolder) {
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    if (!fso.folderExists(saveFolder)) {
        fso.createFolder(saveFolder);
    }
}


function saveFileTo(fFileContent, fFilePath) {
    var a = new ActiveXObject("Adodb.Stream");
    a.Type = 1
    a.Open();
    a.Write(fFileContent);
    a.SaveToFile(fFilePath, 2);
    a.Cancel();
    a.Close();
}

function gb2312toUtf8(s) {
    var output;

    var a = new ActiveXObject("Adodb.Stream");
    a.Type = 1
    a.Open();
    a.Write(s);
    a.Position = 0;
    a.Type = 2;
    a.Charset = 'gb2312';
    output = a.readText();
    a.Close();

    return output;
}