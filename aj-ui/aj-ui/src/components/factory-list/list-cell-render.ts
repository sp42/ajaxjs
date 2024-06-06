/**
 * 常见的单元格渲染器
 */
export default function (rendererColDef: iViewTableColumn, item: TableColumn): void {
    if (item.isOneLine) {
        rendererColDef.ellipsis = true;
        rendererColDef.tooltip = true;
    }

    if (item.render && item.render != 'raw') {
        if (item.render == 'email')
            rendererColDef.render = email;

        if (item.render == 'link')
            rendererColDef.render = link;

        if (item.render == 'link_http') {
            rendererColDef.render = link_http;
            item.isOneLine = true;
            rendererColDef.ellipsis = true;
            rendererColDef.tooltip = true;
            rendererColDef.minWidth = item.minWidth = 150;// item.minWidth 不起作用
        }

        if (item.render == 'sex')
            rendererColDef.render = sex;

        if (item.render == 'date')
            rendererColDef.render = date;

        if (item.render == 'short_date')
            rendererColDef.render = short_date;

        if (item.render == 'long_date')
            rendererColDef.render = long_date;

        if (item.render == 'thumb')
            rendererColDef.render = thumb;

        if (item.render == 'stat')
            rendererColDef.render = state;

        if (item.render == 'clk_event' && item.clkEvent)
            try {
                rendererColDef.render = clk_event(eval(item.clkEvent));
            } catch (e) {
                alert('eval 代码错误，请检查配置 code: ' + item.clkEvent);
            }

        if (item.render == 'render') {
            if (item.isCode && item.customRender)
                rendererColDef.render = eval(item.customRender);

            if (!item.isCode && item.customRenderKV) {
                rendererColDef.render = customRender(<JsonParam[]><unknown>item.customRenderKV);
            }
        }

    }
}

function state(h: Function, params: any) {
    let value = params.row[params.column.key]; // 取出当前值
    const dot = '•';
    switch (value) {
        case 0:
            return h('div', {
                style: {
                    color: '#52c41a',
                    fontSize: '20px'
                },
                attrs: {
                    title: '正常'
                }
            }, dot);
            break;
        case -1:
            return h('div', {
                style: {
                    color: 'red',
                    fontSize: '20px'
                },
                attrs: {
                    title: '已删除'
                }
            }, dot);
            break;
        default:
            return h('div', {
                style: {
                    color: 'yellow',
                    fontSize: '20px'
                },
                attrs: {
                    title: '已禁用/已下线'
                }
            }, dot);
    }
}

function sex(h: Function, params: any) {
    let value: number = params.row[params.column.key]; // 取出当前值
    let str: string = '';

    console.log(value)

    switch (value) {
        case 1:
            str = '男';
            break;
        case 2:
            str = '女';
            break;
        case 0:
        default:
            str = '未知';
    }

    return h('span', str);
}

function email(h: Function, params: any) {
    let value: string = params.row[params.column.key]; // 取出当前值

    return value ? h('a', {
        attrs: {
            href: 'mailto://' + value
        }
    }, value) : '';
}

function link(h: Function, params: any) {
    let value: string = params.row[params.column.key]; // 取出当前值

    return value ? h('a', {
        attrs: {
            href: value, target: '_blank', title: value
        }
    }, '超链接') : '';
}

function link_http(h: Function, params: any) {
    let value: string = params.row[params.column.key]; // 取出当前值

    return value ? h('a', {
        attrs: {
            href: value, target: '_blank', title: value
        }
    }, value) : '';
}

function date(h: Function, params: any) {
    let value: string = params.row[params.column.key]; // 取出当前值

    if (value) {
        let arr: string[] = value.split(':');
        arr.pop();
        arr.pop();

        return value ? h('span', arr.join('').replace(/\s\d+$/, '')) : '';
    } else
        return '';
}

function long_date(h: Function, params: any) {
    let value: string = params.row[params.column.key]; // 取出当前值

    if (value && isoDateTimeRegex.test(value)) // 先判断是否 ISO 8601 格式的日期和时间 的字符串
        return h('span', convertDateLong(value));

    return value ? h('span', value) : '';
}

const isoDateTimeRegex = /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2}(?:.\d*)?)$/;

function convertDate(isoDate) {
    let date = new Date(isoDate);
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const hour = date.getHours();
    const minute = date.getMinutes() === 0 ? '00' : date.getMinutes();

    return `${year}-${month}-${day} ${hour}:${minute}`;
}

function convertDateLong(isoDate) {
    let date = new Date(isoDate);
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const hour = date.getHours();
    const minute = date.getMinutes() === 0 ? '00' : date.getMinutes();
    const seconds = date.getSeconds();

    return `${year}-${month}-${day} ${hour}:${minute}:${seconds}`;
}

function short_date(h: Function, params: any) {
    let value: string = params.row[params.column.key]; // 取出当前值

    if (value) {
        if (isoDateTimeRegex.test(value)) // 先判断是否 ISO 8601 格式的日期和时间 的字符串
            return h('span', convertDate(value));

        let arr: string[] = value.split(':');
        arr.pop();

        return value ? h('span', arr.join(':')) : '';
    } else
        return '';
}

function thumb(h: Function, params: any) {
    let value: string = params.row[params.column.key]; // 取出当前值

    return value ? h('a', {
        attrs: {
            href: value, target: '_blank', title: value
        }
    }, [
        h('img', {
            attrs: {
                src: value, style: 'max-width:100px;max-height:50px;'
            }
        })
    ]) : '';
}

function clk_event(clk: Function): Function {

    return (h: Function, params: any) => {
        let value: string = params.row[params.column.key]; // 取出当前值

        return value ? h('a', {
            on: {
                'click': () => {
                    clk(params.row);
                }
            }
        }, value) : '';
    };
}

function customRender(arr: JsonParam[]): Function {
    // arr to map
    let map = {};
    arr.forEach(item => {
        map[(item.value) + ""] = item.name;
    });

    return (h: Function, params: any) => {
        let value: string = params.row[params.column.key]; // 取出当前值
        return (value == '0' || value) ? h('span', map[value]) : '';
    };
}

