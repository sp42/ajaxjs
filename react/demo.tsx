class MyClass extends React.Component<any, any> {
    render() {
        return <div>
            <div className="msgbox">
                <h1>title</h1>
                <div className="topCloseBtn closeAction">×</div>
                <div className="inner">{this.props.name}
                    人们普遍认为，随着浦东新区和苏州工业园在上世纪90年代的崛起，长三角已超越珠三角成为中国经济增长的第一级，但是本文通过大量的数据分析与调研认为：
                    近几年来，珠三角悄然实现了弯道超车，无论是自主创新能力、民间创富能力，还是可持续发展能力，都比长三角的表现更优。
                    为什么?长三角不是国家钦定的老大哥吗，无论是政策优势、人才优势，还是地缘优势，都比之珠三角更优。珠三角反超的秘密是什么?
                </div>
                <div className="btn">
                    <div className="yesAction">是</div>
                    <div className="noAction">否</div>
                    <div className="closeAction">关闭</div>
                </div>
            </div>
            <div className="msgbox_mask"></div>
        </div>;
    }
}

document.addEventListener('DOMContentLoaded', function () {
    ReactDOM.render(<MyClass name="Tom" />, document.body);

    var msgbox = document.querySelector('.msgbox');
    // 计算居中
    document.querySelector('.msgbox_mask').style.height = document.body.scrollHeight + 'px';
    msgbox.style.left = (window.innerWidth / 2 - msgbox.clientWidth / 2) + 'px';
    msgbox.style.top = (window.innerHeight / 2 - msgbox.clientHeight / 2) + 'px';

    // 平均分按钮宽度
    var btn = msgbox.querySelector('.btn'), btns = msgbox.querySelectorAll('.btn>div');
    var j = btns.length, width = (btn.clientWidth / j - 12) + 'px'; // 要减去间隙

    for (var i = 0; i < j; i++) {
        btns[i].style.marginRight = '10px'; // 添加间隙
        btns[i].style.width = width;
    }
    // 登记关闭事件
    [].forEach.call(msgbox.querySelectorAll('.closeAction'), function (closeBtn) {
        closeBtn.onclick = function () {
            var msgbox = document.querySelector('.msgbox');
            msgbox.parentNode.removeChild(msgbox);
            var msgbox_mask = document.querySelector('.msgbox_mask');
            if (msgbox_mask)
                msgbox_mask.parentNode.removeChild(msgbox_mask);
        }
    });
});
