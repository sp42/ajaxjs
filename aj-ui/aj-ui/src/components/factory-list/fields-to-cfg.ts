/**
 * 智能识别
 * 针对不同的字段类型，决定不同的列配置
 * 都是一些常见的、约定成俗的配置
 * 
 * @param item 
 * @param tableColumnData 
 */
export default function (item: CheckableDataBaseColumnMeta, tableColumnData: TableColumn[]): void {
    let col: TableColumn = {
        isShow: true,
        key: item.name,
        title: item.name,
        align: 'center'
    };

    let type: string = item.type.toLowerCase();
    if (type.indexOf('text') != -1) {
        col.minWidth = 180;
        col.isOneLine = true;
    }

    if (type.indexOf('datetime') != -1) 
        col.render = 'short_date';
    
    let name: string = item.name.toLowerCase();
    if (name.indexOf('email') != -1) 
        col.render = 'email';
    
    if (name.indexOf('url') != -1) 
        col.render = 'link';
    
    if (name.indexOf('thumb') != -1 || name.indexOf('avatar') != -1) 
        col.render = 'thumb';
    
    // 名称
    switch (name) {
        case 'id':
            col.title = '#id';
            col.width = 60;
            break;
        case 'name':
            col.title = '名称';
            col.minWidth = 120;
            col.isOneLine = true;
            break;
        case 'content':
        case 'desc':
            col.title = '简介';
            col.minWidth = 180;
            col.isOneLine = true;
            break;
        case 'stat':
            col.title = '状态';
            col.width = 100;
            break;
        case 'createDate':
        case 'created_at':
            col.title = '创建日期';
            col.width = 160;
            break;
        case 'updateDate':
        case 'updated_at':
            col.title = '修改日期';
            col.width = 160;
            break;
        default:
            col.title = item.name;
    }

    tableColumnData.push(col);
}