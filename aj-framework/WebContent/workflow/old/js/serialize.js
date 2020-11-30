// 转换为 json/xml
ajflow.serialize = {
	rect: {
		toJson(_o, _text, _rect) {
	        var data = "{type:'" + _o.type + "',text:{text:'" + _text.attr('text') + 
				"'}, attr:{ x:" + Math.round(_rect.attr('x')) + ", y:" + Math.round(_rect.attr('y')) + 
				", width:" + Math.round(_rect.attr('width')) + ", height:" + Math.round(_rect.attr('height')) + "}, props:{";
	        
			for (var k in _o.props) 
	            data += k + ":{value:'" + _o.props[k].value + "'},";
	        
	        if (data.substring(data.length - 1, data.length) == ',') 
				data = data.substring(0, data.length - 1);
				
	        data += "}}";
	
	        return data;
	    },
		toBeforeXml(_o, _text, _rect) {
	        var data = "<" + _o.type + " layout=\"" + 
				(Math.round(_rect.attr("x")) - 180) + "," + Math.round(_rect.attr("y")) + "," + 
				 Math.round(_rect.attr("width")) + "," + Math.round(_rect.attr("height")) + "\" ";
	       
			for (var k in _o.props) {
	            if (k == "name" && _o.props[k].value == "") {
	                alert(_o.type + " 名称 不能为空");
	                return "";
	            }
	
	            if (k == "layout") 
					continue;
					
	            if (_o.props[k].value != "") {
	                var vv = _o.props[k].value.replace(/>/g, "#5").replace(/</g, "#6").replace(/&/g, "#7");
	                data += k + "=\"" + vv + "\" ";
	            }
	        }
	
	        data += ">";
	        return data;
	    }
	},
	path: {
		toJson(_from, _to, _dotList, _text, _textPos) {
	        var r = "{from:'" + _from.getId() + "',to:'" + _to.getId() + "', dots:" + _dotList.toJson() + ",text:{text:'" + _text.attr("text") + 
				"'},textPos:{_dotList:" + Math.round(_textPos.x) + ",_ox:" + Math.round(_textPos.y) + "}, props:{";
	        
			for (var o in _o.props) 
	            r += o + ":{value:'" + _o.props[o].value + "'},"
	        
	        if (r.substring(r.length - 1, r.length) == ",") 
	            r = r.substring(0, r.length - 1)
	        
	        r += "}}";
	        return r;
	    },
	
	    toXml(_o, _to, _dotList, _textPos) {
	        var hx = Math.round(_textPos.x);
	        var r = "<transition offset=\"" + hx + "," + Math.round(_textPos.y) + "\" to=\"" + _to.getName() + "\" ";
	        var dots = _dotList.toXml();
	
	        if (dots != "") r += " g=\"" + _dotList.toXml() + "\" ";
	
	        for (var o in _o.props) {
	            if (o == "name" && _o.props[o].value == "") {
	                r += o + "=\"" + _id + "\" ";
	                continue;
	            }
	
	            if (_o.props[o].value != "") {
	                var vv = _o.props[o].value.replace(/>/g, "#5").replace(/</g, "#6").replace(/&/g, "#7");
	                r += o + "=\"" + vv + "\" ";
	            }
	        }
	
	        r += "/>";
	        return r
	    }
	},
	dotList: {
		 toJson(_fromDot) {
            var data = "[", d = _fromDot;

            while (d) {
                if (d.type() == "big") 
                    data += "{_dotList:" + Math.round(d.pos().x) + ",_ox:" + Math.round(d.pos().y) + "},";

                d = d.right();
            }

            if (data.substring(data.length - 1, data.length) == ",")
                data = data.substring(0, data.length - 1);
            
            data += "]";
            return data;
        },

        toXml(_fromDot) {
            var data = "", d = _fromDot;

            while (d) {
                if (d.type() == "big")
                    data += (Math.round(d.pos().x) - 180) + "," + Math.round(d.pos().y) + ";"
                
                d = d.right();
            }

            if (data.substring(data.length - 1, data.length) == ";") 
                data = data.substring(0, data.length - 1);
            
            return data;
        }
	}
};