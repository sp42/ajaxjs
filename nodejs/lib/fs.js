var p = require('path'),
    fs = require('fs');
// 同步删除文件夹
var rmTreeSync = exports.rmTreeSync = function (path) {
    // 如果文件路径不存在或文件路径不是文件夹则直接返回
    if (!p.existsSync(path) || !fs.statSync(path).isDirectory()) return;
    var files = fs.readdirSync(path);
    // 如果文件夹为空则直接删除
    if (!files.length) {
        fs.rmdirSync(path);
        return;
    } else {
        // 文件夹不为空，依次删除文件夹下的文件
        files.forEach(function (file) {
            var fullName = p.join(path, file);
            if (fs.statSync(fullName).isDirectory()) {
                rmTreeSync(fullName);
            } else {
                fs.unlinkSync(fullName);
            }
        });
    }
    // 最后删除根文件夹
    fs.rmdirSync(path);
    console.log("删除文件夹: ", path, "完毕");
};

// 异步删除文件夹
var rmTree = exports.rmTree = function (path, callback) {
    p.exists(path, function (exists) {
        //如果文件路径不存在或文件路径不是文件夹则直接返回
        if (!exists || !fs.statSync(path).isDirectory()) return callback();
        fs.readdir(path, function (err, files) {
            if (err) return callback(err);
            //根据文件夹下的每个文件的完全路径创建一个数组
            var fullNames = files.map(function (file) { return p.join(path, file); });
            //获取文件夹下的文件的属性
            getFilesStats(fullNames, fs.stat, function (err, stats) {
                var files = [];
                var dirs = [];
                //不要使用 for in 来遍历，如果有空值，会不一一对应
                for (var i = 0; i < fullNames.length; i++) {
                    if (stats[i].isDirectory()) {
                        dirs.push(fullNames[i]);
                    } else {
                        files.push(fullNames[i]);
                    }
                }
                //先删文件，后删文件夹
                serial(files, fs.unlink, function (err) {
                    if (err) return callback(err);
                    serial(dirs, rmTree, function (err) {
                        if (err) return callback(err);
                        //最后删除根文件夹
                        fs.rmdir(path, callback);
                    });
                });
            });
        });
    });
};
//获取文件的属性
var getFilesStats = function (list, stat, callback) {
    if (!list.length) return callback(null, []);
    //concat 中无参数，相当于拷贝一份数组，而不是引用数组
    var copy = list.concat();
    var statArray = [];
    //handle 函数亮了。如果我写的话，我会把 handler 单独写出去，这样的话，还得传一个 copy 参数，麻烦
    stat(copy.shift(), function handler(err, stats) {
        statArray.push(stats);
        if (copy.length) {
            stat(copy.shift(), handler);
        } else {
            callback(null, statArray);
        }
    });
};
//删除文件
var serial = function (list, rmfile, callback) {
    if (!list.length) return callback(null, []);
    var copy = list.concat();
    rmfile(copy.shift(), function handler(err) {
        if (err) return callback(err);
        if (copy.length) {
            rmfile(copy.shift(), handler);
        } else {
            callback(null);
        }
    });
};