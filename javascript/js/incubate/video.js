// 获取各个组件
var the_player = document.getElementById('kplayer');
var player = document.getElementById('kplayer_show');

var	proccess = document.getElementById('kplayer_proccess_current');

var bar = document.getElementById('kplayer_proccess_bar'),
	play = bar.childNodes[1],
	current_duration = bar.childNodes[3],
	duration = bar.childNodes[5],
	mute = bar.childNodes[11],
	full = bar.childNodes[7],
	volume = bar.childNodes[9],
	volume_btn = volume.childNodes[1];

var the_proccess = null; // 定义播放进度条 setInterval 的 ID
var the_duration = null; // 保存视频总时长

var player_long = 640; // 告知播放器的宽度
sessionStorage.player_long = player_long; // 临时保存这些变量值

// 时间格式转换
function toTime(time){
	var h, m, s, result = '';

	if( time > 3600 ){
		h = parseInt(time / 3600);
		if( h < 10 ) h = '0' + h;
		time = time % 3600;
		result = h + ':';
	} else {
		result = '00' + ':'
	}
	if( time > 60 ){
		m = parseInt(time / 60);
		if( m < 10 ) m = '0' + m;
		time = time % 60;
		result = result + m + ':';
	} else {
		result = result + '00' + ':'
	}
	s = parseInt(time);
	if( s < 10 ) s = '0' + s;
	return result = result + s;
}

// 获取鼠标 x 坐标
function mousePos(e){ 
    var e = e || window.event; 
    return e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
}

// 获取元素与窗口左侧的距离
function getElementLeft(element){
	var actualLeft = element.offsetLeft;
	var current = element.offsetParent;
	while (current !== null){
		actualLeft += current.offsetLeft;
		current = current.offsetParent;
	}
	
	return actualLeft;
}

var proccess_left = getElementLeft(proccess), volume_left = getElementLeft(volume);

sessionStorage.proccess_left = proccess_left; // 临时保存这些变量
sessionStorage.volume_left = volume_left;

// 控制进度条
function playing(){
	var time = player.currentTime;
	var vate = time / the_duration;
	proccess.style.width = vate * player_long;
	current_duration.innerHTML = toTime(time);
}

// 播放与暂停
play.onclick = function(){
	if ( player.paused ){
		player.play();
		if( duration.innerHTML == '00:00' ){
			the_duration = player.duration;
			duration.innerHTML = toTime(the_duration);
		}
		// 设置为暂停图标
		play.style.backgroundPosition = '-69px 0';
		// 控制播放条前进
		the_proccess = setInterval("playing()", 100);
		// 判断键盘动作
		document.addEventListener('keydown', moveLittle);
	} else {
		clearInterval(the_proccess);
		player.pause();
		// 设置为播放图标
		play.style.backgroundPosition = '-93px 0';
		// 取消键盘动作
		document.removeEventListener('keydown', moveLittle);
	}
};

// 静音
mute.onclick = function(){
	if(player.muted == false){
		mute.style.backgroundPosition = '-46px 0';
		player.muted = true;
	}else{
		mute.style.backgroundPosition = '-23px 0';
		player.muted = false;
	}
}

// 根据浏览器类型输出可用的全屏 API
function customRequestFullScreen(){
    if(typeof player['requestFullScreen'] != 'undefined')return player.requestFullScreen();
    else{
    	['webkit', 'moz', 'o', 'ms'].forEach(function(prefix){
            if( typeof player[ prefix + 'RequestFullScreen' ] != 'undefined' )
            	return player[ prefix + 'RequestFullScreen' ]();
		});
    }
}

// 全屏时相关
function hideControl(){
	bar.parentNode.style.display = 'none';
}

var hide = null;

// 全屏
full.onclick = function(){
	// customRequestFullScreen();
	if( the_player.className.indexOf('player_full') == -1 ){
		player_long = document.body.clientWidth;
		proccess_left = 0;
		volume_left = 0;
		the_player.className = 'player_full';
		hide = setTimeout('hideControl()', 2000);

		the_player.onmousemove = function(){
			bar.parentNode.style.display = 'block';
			clearTimeout(hide);
			hide = setTimeout('hideControl()', 2000);
		}
	} else {
		player_long = sessionStorage.player_long;
		proccess_left = sessionStorage.proccess_left;
		volume_left = sessionStorage.volume_left;
		the_player.className = 'player_normal';
		clearTimeout(hide);
		the_player.onmousemove = null;
	}
}

// ESC 取消全屏
document.onkeydown = function(e){
	if( e.keyCode == 27 ){
		player_long = sessionStorage.player_long;
		proccess_left = sessionStorage.proccess_left;
		volume_left = sessionStorage.volume_left;
		bar.parentNode.style.display = 'block';
		the_player.className = 'player_normal';
		clearTimeout(hide);
		the_player.onmousemove = null;
		return false;
	}
}

// Left Right
function moveLittle(e){
	if( e.keyCode == 39 ){
		player.currentTime = player.currentTime + 10;
		proccess.style.width = player.currentTime / the_duration * player_long;
	} else if( e.keyCode == 37 ){
		player.currentTime = player.currentTime - 10;
		proccess.style.width = player.currentTime / the_duration * player_long;
	}
}

// 视频播放结束后的处理
player.addEventListener('ended', function(e){
	clearInterval(the_proccess);
	play.style.backgroundPosition = '-93px 0';
	proccess.style.width = '100%';
});

// 移动进度条
function move(){
	var offset = mousePos() - proccess_left;
	player.currentTime = offset / player_long * the_duration;
	proccess.style.width = offset;
};

// 设置初始音量条
if( localStorage.kplayer_volume_data == undefined ) localStorage.kplayer_volume_data = 1;
volume_btn.style.left = localStorage.kplayer_volume_data * 70 > 60 ? 60 : localStorage.kplayer_volume_data * 70;

// 控制音量
volume.onclick = function(){
	var temp = volume_left != 0 ? volume_left : getElementLeft(volume);
	var offset = mousePos() - temp;
	if( offset > 60 ) offset = 60;
	volume_btn.style.left = offset;
	player.volume = offset / 70;
}

// 拖动控制音量
volume_btn.onmousedown = function(){
	document.onmousemove = function(){
		var temp = volume_left != 0 ? volume_left : getElementLeft(volume);
		var offset = mousePos() - temp;
		console.log(offset);
		if( 0 <= offset && offset <= 60 ){
			volume_btn.style.left = offset;
			player.volume = offset / 70;
		}
	};

	return false;
}

document.onmouseup = function(){
	document.onmousemove = null;
}

// 当音量改变时把音量值保存在本地
player.addEventListener('volumechange', function(argument) {
	localStorage.kplayer_volume_data = player.volume;
});