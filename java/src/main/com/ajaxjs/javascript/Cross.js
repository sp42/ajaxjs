View = {
	init : function(){
		View.setObj('request',  pageContext.getRequest());
		View.setObj('response', pageContext.getResponse());
		View.setObj('out', pageContext.getOut()());
	}
};
self = this;

View.setObj = function (globalVarName, obj){
	self[globalVarName] = obj;
};

View.write = function (str){
	pageContext.getOut().println(str);
};