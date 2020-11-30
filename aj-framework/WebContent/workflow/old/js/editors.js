var has_showModalDialog = !!window.showModalDialog; //定义一个全局变量判定是否有原生showModalDialog方法
if (!has_showModalDialog && !!(window.opener)) {
    window.οnbefοreunlοad = function() {
        window.opener.hasOpenWindow = false; //弹窗关闭时告诉opener：它子窗口已经关闭
    }
}
//定义window.showModalDialog如果它不存在
if (window.showModalDialog == undefined) {
    window.showModalDialog = function(url, mixedVar, features) {
        if (window.hasOpenWindow) {
            alert("您已经打开了一个窗口！请先处理它"); //避免多次点击会弹出多个窗口
            window.myNewWindow.focus();
        }
        window.hasOpenWindow = true;
        if (mixedVar) var mixedVar = mixedVar;
        //因window.showmodaldialog 与 window.open 参数不一样，所以封装的时候用正则去格式化一下参数
        if (features) var features = features.replace(/(dialog)|(px)/ig, "").replace(/;/g, ',').replace(/\:/g, "=");
        //window.open("Sample.htm",null,"height=200,width=400,status=yes,toolbar=no,menubar=no");
        //window.showModalDialog("modal.htm",obj,"dialogWidth=200px;dialogHeight=100px"); 
        var left = (window.screen.width - parseInt(features.match(/width[\s]*=[\s]*([\d]+)/i)[1])) / 2;
        var top = (window.screen.height - parseInt(features.match(/height[\s]*=[\s]*([\d]+)/i)[1])) / 2;
        window.myNewWindow = window.open(url, "_blank", features);
    }
}

function selectOrg(ctx, e1, e2) {
    var element1 = document.getElementById(e1);
    var element2 = document.getElementById(e2);
    var l = window.showModalDialog(ctx + "/dialogs/selectDialog.jsp?type=orgTree", " ", "dialogWidth:800px;dialogHeight:540px;center:yes;scrolling:yes");
    if (l == null) return;
    var result = splitUsersAndAccounts(l);
    element1.value = result[0];
    element1.title = result[0];
    element2.value = result[1];
}

function selectOrgUser(ctx, e1, e2) {
    var element1 = document.getElementById(e1);
    var element2 = document.getElementById(e2);
    var l = window.showModalDialog(ctx + "/dialogs/selectDialog.jsp?type=orgUserTree", " ", "dialogWidth:800px;dialogHeight:540px;center:yes;scrolling:yes");
    if (l == null) return;
    var result = splitUsersAndAccounts(l);
    element1.value = result[0];
    element1.title = result[0];
    element2.value = result[1];
}

function splitUsersAndAccounts(userNamesAndAccount) {
    var userNames = "";
    var accounts = "";

    var array = userNamesAndAccount.split(";");
    for (i = 0; i < array.length; i++) {
        var temp = splitUserNameAndAccount(array[i]);
        userNames += temp[0] + ",";
        accounts += temp[1] + ",";
    }
    userNames = userNames.substr(0, userNames.length - 1);
    accounts = accounts.substr(0, accounts.length - 1);
    var result = new Array(2);
    result[0] = userNames;
    result[1] = accounts;
    return result;
}

function splitUserNameAndAccount(userNameAndAccount) {
    var temp = new Array(2);
    if (userNameAndAccount.indexOf("(") != -1) {
        temp[0] = userNameAndAccount.substring(0, userNameAndAccount.indexOf("("));
        temp[1] = userNameAndAccount.substring(userNameAndAccount.indexOf("(") + 1, userNameAndAccount.indexOf(")"));
    } else {
        temp[0] = userNameAndAccount;
        temp[1] = userNameAndAccount;
    }
    return temp;
}

ajflow.editors = {
    textEditor: function() {
        var _props, _k, _div, _src, _r;

        this.init = function(props, _k, _div, _src, _r) {
            _props = props;
            _k = _k;
            _div = _div;
            _src = _src;
            _r = _r;

            $('<input style="width:98%;"/>').val(_src.text()).change(function() {
                props[_k].value = $(this).val();
                $(_r).trigger("textchange", [$(this).val(), _src])
            }).appendTo("#" + _div);

            $("#" + _div).data("editor", this)
        };

        this.destroy = function() {
            $("#" + _div + " input").each(function() {
                _props[_k].value = $(this).val();
                $(_r).trigger("textchange", [$(this).val(), _src])
            })
        }
    },

    inputEditor: function() {
        var _props, _k, _div, _src, _r;
        this.init = function(props, k, div, src, r) {
            _props = props;
            _k = k;
            _div = div;
            _src = src;
            _r = r;

            $('<input style="width:98%;"/>').val(props[_k].value).change(function() {
                props[_k].value = $(this).val();
            }).appendTo('#' + _div);

            $('#' + _div).data('editor', this);
        }
        this.destroy = function() {
            $('#' + _div + ' input').each(function() {
                _props[_k].value = $(this).val();
            });
        }
    },
    selectEditor: function(arg) {
        var _props, _k, _div, _src, _r;

        this.init = function(props, k, div, src, r) {
            _props = props;
            _k = k;
            _div = div;
            _src = src;
            _r = r;

            var sle = $('<select  style="width:99%;"/>').val(props[_k].value).change(function() {
                props[_k].value = $(this).val();
            }).appendTo('#' + _div);

            if (typeof arg === 'string') {
                $.ajax({
                    type: "GET",
                    url: arg,
                    success: function(data) {
                        var opts = eval(data);
                        if (opts && opts.length) {
                            for (var idx = 0; idx < opts.length; idx++) {
                                sle.append('<option value="' + opts[idx].value + '">' + opts[idx].name + '</option>');
                            }
                            sle.val(_props[_k].value);
                        }
                    }
                });
            } else {
                for (var idx = 0; idx < arg.length; idx++) {
                    sle.append('<option value="' + arg[idx].value + '">' + arg[idx].name + '</option>');
                }
                sle.val(_props[_k].value);
            }

            $('#' + _div).data('editor', this);
        };

        this.destroy = function() {
            $('#' + _div + ' input').each(function() {
                _props[_k].value = $(this).val();
            });
        };
    },
    assigneeEditor: function(arg) {
        var _props, _k, _div, _src, _r;
        this.init = function(props, k, div, src, r) {
            _props = props;
            _k = k;
            _div = div;
            _src = src;
            _r = r;

            $('<input style="width:88%;" readonly="true" id="dialogEditor"/>').val(props[_k].value).appendTo('#' + _div);
            $('<input style="width:10%;" type="button" value="选择"/>').click(function() {
                //alert("选择:" + snakerflow.config.ctxPath + arg);
                var element = document.getElementById("dialogEditor");
                var l = window.showModalDialog(snakerflow.config.ctxPath + arg, " ", "dialogWidth:800px;dialogHeight:540px;center:yes;scrolling:yes");
                if (l == null) return;
                var result = splitUsersAndAccounts(l);
                element.title = result[1];
                element.value = result[1];
                props[_k].value = result[1];
                props['assignee'].value = result[0];
            }).appendTo('#' + _div);

            $('#' + _div).data('editor', this);
        }
        this.destroy = function() {
            //
        }
    }
};

// 属性
ajflow.Props = function(o) {
    var _pdiv = $("#properties").hide().css(ajflow.config.props.attr).bind("click", () => {
        return false;
    }), _tb = _pdiv.find("table");

	// props
    $(ajflow.PAPER).bind("showprops", function(e, props, src) {
		console.log('显示属性 %s, %s', props.name.value, props.displayName.value);
  /*      if (_src && _src.getId() == src.getId())  // 连续点击不刷新
            return;*/
        
        $(_tb).find(".editor").each(function() {
            var k = $(this).data("editor");
            if (k) 
                k.destroy();
        });

        _tb.empty();
        _pdiv.show();

		ajflow.DESIGNER.property = props;
        for (var l in props) {
			var name = props[l].name;
			
            if (!name){
				continue;
			} 
			
            if ((name == "name" || name == "displayName") && name == "") 
                name = src.getId();
            
            name = name.replace(/#1/g, "'").replace(/#2/g, "\"").replace(/#3/g, "\r\n");
            name = name.replace(/#4/g, "\n").replace(/#5/g, ">").replace(/#6/g, "<").replace(/#7/g, "&");
			props[l].name = name;

            if (!props[l].label) 
                continue;
            
            _tb.append("<tr><td class='properties_name'>" + props[l].label + "</td><td class='properties_value'><div id='p" + l + "' class='editor'></div></td></tr>");
            
			if (props[l].editor) {
            	var obj =  props[l].editor();
				obj.init(props, l, "p" + l, src, ajflow.PAPER);
			}
        }
    });
};