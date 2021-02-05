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
    del.sync(['./dist/css/all.css', './dist/css/all.min.css']);

    src(["./dist/css/**/*.css", "!./dist/css/reset", "!./dist/css/app/**/*.css", "!./dist/css/admin/**/*.css", "!./dist/css/website/**/*.css"])
        .pipe(concat("all.css"))// 合并所有js到all.js
        .pipe(dest('./dist/css'))
        .pipe(cssSourcemaps.init())
        .pipe(rename({ suffix: '.min' }))         //修改文件名
        .pipe(minifyCss())                    //压缩文件
        .pipe(cssSourcemaps.write('../../sourcemap/css/'))
        .pipe(dest('./dist/css'));

    cb();

    console.log("打包 CSS 完成");
}

exports.css = packCss;

const replace = require('gulp-replace');

const _EXTENDS_ = `var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();`,
__MMakeTemplateObject__ = `var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};`;

/**
 * 
 * @param {*} arr 
 * @param {*} allJsName 
 * @param {*} isRemoveHelperFn 移除重复的 ts 辅助函数 TypeScript removing extends duplicates
 */
function mod(arr, allJsName, isRemoveHelperFn) {
    let obj = src(arr)
        .pipe(concat(allJsName));// 合并所有js到all.js

    if (isRemoveHelperFn) {
        obj = obj.pipe(replace(_EXTENDS_, "")).pipe(replace(__MMakeTemplateObject__, ""));
    }

    obj = obj.pipe(dest('./dist'));

    obj.pipe(rename({ suffix: '.min' }))            // rename压缩后的文件名
        .pipe(sourcemaps.init())                    // Source Map 
        .pipe(uglify())                             // 压缩 js
        .pipe(sourcemaps.write('../sourcemap/'))    // Source Map 
        .pipe(dest('./dist'));
}

// 打包所有的 css 到一个文件
packJs = (cb) => {
    del.sync(['./dist/base.js', './dist/widget.js', './dist/form.js', './dist/list.js', './dist/all.js']);

    mod(['./dist/base/prototype.js', './dist/base/aj.js', './dist/base/xhr.js', './src/base/ts-helper.js'], 'base.js');
    mod(['./dist/widget/**/*.js', '!./dist/widget/page/ChineseChars.js'], 'widget.js', true);
    mod(['./dist/form/**/*.js', '!./dist/widget/form/China_AREA.js', '!./dist/widget/form/China_AREA_full.js'], 'form.js', true);
    mod(['./dist/list/**/*.js', '!./dist/widget/form/China_AREA.js', '!./dist/widget/form/China_AREA_full.js'], 'list.js', true);
    // mod(['./dist/user/**/*.js'], 'misc.js');

    cb();
}

packJsConcat = (cb) => {
    setTimeout(() => {
        src(['./dist/base.js', './dist/widget.js', './dist/form.js', './dist/list.js', './dist/misc.js']).pipe(concat('all.js')).pipe(dest('./dist'));
        src(['./dist/base.min.js', './dist/widget.min.js', './dist/form.min.js', './dist/list.min.js', './dist/misc.min.js']).pipe(concat('all.min.js')).pipe(dest('./dist'));
        console.log('打包 js: all.js 完成');
        cb();
    }, 2000)
}
exports.js = series(packJs, packJsConcat);
