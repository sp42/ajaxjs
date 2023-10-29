<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta charset="UTF-8" />
<title>admin</title>
<!--
    <link rel="stylesheet" type="text/css" href="http://www.ajaxjs.com/public/common.css" />
    <link rel="stylesheet" type="text/css" href="http://www.ajaxjs.com/public/admin.css" />
    <script src="http://www.ajaxjs.com/public/common.js"></script>
-->

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/common/common.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/common/admin.css" />
<script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.14/vue.min.js"></script>
<script src="${pageContext.request.contextPath}/common/common.js"></script>

<script>
    const accessToken = localStorage.getItem("accessToken");

    if(location.href.indexOf('jsp') != -1 && !accessToken) {
      //  alert('你未登录！');
       //  location.assign('index.jsp');
        //location.assign('${pageContext.request.contextPath}');
    }

    // 数据字典
    TASK_TYPE = {
        "1": "全局侦察任务",
        "2": "局部侦察任务",
        "3": "建模侦察任务",
        "4": "精准打击任务",
        "5": "区域瞰制任务",
        "6": "前送物资任务",
        "7": "后撤伤员任务",
        "8": "前进区域侦察任务",
        "9": "登陆突击任务",
        "10": "扫残破障任务",
        "11": "穿插迂回任务",
        "101": "任务规划席位",
        "102": "前进指挥席位",
        "103": "侦察操控席位",
        "104": "情报分析席位",
        "105": "打击瞰制席位",
        "106": "后勤保障席位",
        "107": "标绘席位"
    };

    OBJECT_GROUP = {
        "11": "红方 基础标绘",
        "12": "红方 态势标绘",
        "13": "红方 有人装备",
        "14": "红方 无人装备",
        "15": "红方 班组",
        "16": "红方 障碍物",
        "21": "蓝方 基础标绘",
        "22": "蓝方 态势标绘",
        "23": "蓝方 有人装备",
        "24": "蓝方 无人装备",
        "25": "蓝方 班组",
        "26": "蓝方 障碍物",
        "97": "子区域",
        "98": "全域多边形",
        "99": "其他",
        "100": "瞰制区域（圆形）"
    };

    OBJECT_TYPE = {
        "1": "红方指挥所",
        "2": "红方阵地",
        "3": "红方标绘点",
        "4": "红方物资点",
        "5": "红方伤员点",
        "6": "红方直/折线",
        "7": "红方曲线",
        "8": "红方圆形",
        "9": "红方矩形",
        "10": "红方多边形",
        "11": "红方正面防御线",
        "12": "红方反面防御线",
        "13": "红方行军线",
        "14": "红方进攻线",
        "17": "红方可见光照片",
        "18": "红方红外照片",
        "19": "红方固定翼巡飞弹",
        "20": "红方固定翼侦察机",
        "21": "红方固定翼察打一体",
        "22": "红方旋翼侦察机",
        "23": "红方无人运输直升机",
        "24": "红方班组",
        "26": "红方穿插点",
        "50": "蓝方指挥所",
        "51": "蓝方阵地",
        "52": "蓝方标绘点",
        "53": "蓝方物资点",
        "54": "蓝方伤员点",
        "55": "蓝方直/折线",
        "56": "蓝方曲线",
        "57": "蓝方圆形",
        "58": "蓝方矩形",
        "59": "蓝方多边形",
        "60": "蓝方正面防御线",
        "61": "蓝方反面防御线",
        "62": "蓝方行军线",
        "63": "蓝方进攻线",
        "66": "蓝方可见光照片",
        "67": "蓝方红外照片",
        "68": "蓝方固定翼巡飞弹",
        "69": "蓝方固定翼侦察机",
        "70": "蓝方固定翼察打一体",
        "71": "蓝方旋翼侦察机",
        "72": "蓝方无人运输直升机",
        "73": "蓝方班组",
        "74": "漂雷",
        "75": "蛇腹型铁丝网",
        "76": "一字型铁丝网",
        "77": "暗堡",
        "78": "地堡",
        "79": "机枪掩体",
        "80": "红方轨条砦",
        "81": "红方三角锥",
        "82": "蓝方穿插点"
    };
</script>