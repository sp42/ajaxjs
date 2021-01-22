const { src, dest, watch, series } = require('gulp');
const concat = require('gulp-concat');
const rename = require('gulp-rename');

const del = require('del');

const config = {
    isCompress: false,      // 是否压缩 js
    serverPort: 8888,
    srcFolder: 'dist/svg/',
    jsFiles: ['Utils.js', 'BaseComponent.js', 'wire/Dot.js'],
    allJsName: 'workflow.js'
};

let arr = config.jsFiles;
arr.forEach((item, i) => arr[i] = config.srcFolder + item);

// 执行前先删除以前的文件
function clean() {
    return del(['./dist', './sourcemap']);
}

const ts = require('gulp-typescript');
const uglify = require('gulp-uglify');
const sourcemaps = require('gulp-sourcemaps');
const tsProject = ts.createProject('tsconfig.json');

// 编译 ts
function jsCompile() {
    return tsProject.src()
        .pipe(tsProject())
        .on('error', () => { /* Ignore compiler errors */ })
        .js.pipe(dest("dist"));
}

// 打包 js
function jsBuild(cb) {
    let obj = src(config.jsFiles)
        .pipe(concat(config.allJsName))// 合并所有js到all.js
        .pipe(dest('./dist'));

    if (!config.isCompress) {
        obj.pipe(rename({ suffix: '.min' }))            // rename压缩后的文件名
            .pipe(sourcemaps.init())                    // Source Map 
            .pipe(uglify())                             // 压缩 js
            .pipe(sourcemaps.write('../sourcemap/'))    // Source Map 
            .pipe(dest('./dist'));
    }

    cb();
}

const less = require('gulp-less'), cssSourcemaps = require('gulp-sourcemaps');

// css when developing
function cssCompile() {
    return src('src/less/**/*.less')
        .pipe(less())
        .pipe(dest('dist/css'));
}


const nodemon = require('gulp-nodemon');

// 开发期间用的
dev = (cb) => {
    nodemon({
        script: './src/nodejs/static-server/server.js'
    });
    watch('src/**/*.ts', series(jsCompile));// /**/* 就是任意层级下的文件。
    watch('src/less/**/*.less', series(cssCompile));

    cb();
};

exports.default = dev;



const minifyCss = require('gulp-minify-css');

// 打包所有的 css 到一个文件
packCss = (cb) => {
    del.sync(['./dist/css/all.css']);

    src(["./dist/css/**/*.css", "!./dist/css/reset'"])
        .pipe(concat("all.css"))// 合并所有js到all.js
        .pipe(dest('./dist/css'))
        .pipe(cssSourcemaps.init())
        .pipe(rename({ suffix: '.min' }))         //修改文件名
        .pipe(minifyCss())                    //压缩文件
        .pipe(cssSourcemaps.write('../../sourcemap/css/'))
        .pipe(dest('./dist/css'));

    cb();
}

exports.packCss = packCss;

function mod(arr, allJsName) {
    let obj = src(arr)
        .pipe(concat(allJsName))// 合并所有js到all.js
        .pipe(dest('./dist'));

    obj.pipe(rename({ suffix: '.min' }))            // rename压缩后的文件名
        .pipe(sourcemaps.init())                    // Source Map 
        .pipe(uglify())                             // 压缩 js
        .pipe(sourcemaps.write('../sourcemap/'))    // Source Map 
        .pipe(dest('./dist'));
}

// 打包所有的 css 到一个文件
packJs = (cb) => {
    del.sync(['./dist/base.js', './dist/widget.js', './dist/form.js', './dist/list.js']);

    mod(['./dist/base/prototype.js', './dist/base/aj.js', './dist/base/xhr.js'], 'base.js');
    mod(['./dist/widget/**/*.js', '!./dist/widget/page/ChineseChars.js'], 'widget.js');
    mod(['./dist/form/**/*.js', '!./dist/widget/form/China_AREA.js', '!./dist/widget/form/China_AREA_full.js'], 'form.js');
    mod(['./dist/list/**/*.js', '!./dist/widget/form/China_AREA.js', '!./dist/widget/form/China_AREA_full.js'], 'list.js');

    console.log('打包 js: all.js 完成');
    cb();
}

exports.packJs = packJs;