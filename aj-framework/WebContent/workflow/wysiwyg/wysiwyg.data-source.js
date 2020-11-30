// 读取表结构
FB.dataSource = new Vue({
	el : '.dataSource',
	data : {
		tables: [''],
		fields:[]
	},
	mounted() {
		//aj.xhr.get('/test-shop/admin/DataBaseShowStru/showTables/', json => this.tables = json.result);
	},
	methods:{
		tableSelect(e) {
			if(e.target.selectedIndex === 0)
				return;
			
			var tableName = e.target.selectedOptions[0].innerHTML;
/*			aj.xhr.get('/test-shop/admin/DataBaseShowStru/showTableAllByTableName/', json => {
				this.fields = json.result;
				FB.statusBar.showMsg('表 ' + tableName +' 加载所有字段加载完成。');
			}, {tableName:tableName});*/
		}
	}
}); 