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

function cssBuild() {
    return src('src/less/**/*.less')
        .pipe(cssSourcemaps.init())
        .pipe(less())
        .pipe(cssSourcemaps.write('../../sourcemap/css/'))
        .pipe(dest('dist/css'));
}

dev = () => {
    watch('src/**/*.ts', series(jsCompile));// /**/* 就是任意层级下的文件。
    watch('src/less/**/*.less', series(cssCompile));
};

exports.default = dev;