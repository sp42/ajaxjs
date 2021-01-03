const { src, dest, watch, series } = require('gulp');
const concat = require('gulp-concat');
const uglify = require('gulp-uglify');
const rename = require('gulp-rename');
const sourcemaps = require('gulp-sourcemaps');
const ts = require('gulp-typescript');
const tsProject = ts.createProject('tsconfig.json');
const del = require('del');

const config = {
    isDev: false,
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

// 编译 ts
function compile() {
    return tsProject.src()
        .pipe(tsProject())
        .on('error', () => { /* Ignore compiler errors */ })
        .js.pipe(dest("dist"));
}

function js(cb) {
    let obj = src(config.jsFiles)
        .pipe(concat(config.allJsName))// 合并所有js到all.js
        .pipe(dest('./dist'));

    if (!config.isDev) {
        obj.pipe(rename({ suffix: '.min' }))            // rename压缩后的文件名
            .pipe(sourcemaps.init())                    // Source Map 
            .pipe(uglify())                             // 压缩 js
            .pipe(sourcemaps.write('../sourcemap/'))    // Source Map 
            .pipe(dest('./dist'));
    }

    cb();
}

const vueify = require('gulp-vueify2');


exports.default = () => {
    watch('src/**/*.ts', series(clean, compile, js));// /**/* 就是任意层级下的文件。
};