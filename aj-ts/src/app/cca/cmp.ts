namespace CCA {
    /**
     * SQL 编辑器
     */
    class SqlEditor extends aj.VueComponent {
        name: string = "sql-editor";
        template = html`<div>
    <textarea :value="parentValue" @input="$emit('update:parentValue', $event.target.value)" :disabled="disabled" cols="60" rows="5"></textarea>
    <br />
    <label><input type="checkbox" /> 自定义 SQL</label> <a href="javascript:void">弹出编辑</a>
</div>`;

        props = {
            disabled: { type: Boolean},
            parentValue: String
        };
    }

    new SqlEditor().register();

    /**
     * 接口地址
     */
    class ApiUrl extends aj.VueComponent {
        name: string = "api-url";
        template = html`<span class="api-url">
    <span v-html="getMethod()"></span>
    <a :href="getUrl">{{getUrl()}}</a>
</span>`;

        props = {
            method: { type: String, default: 'get' },
            root: { type: String, required: true },
            dir: { type: String, required: true },
            subDir: { type: String, required: false },
        };

        /**
         * HTTP 方法
         */
        method: string = "";

        getMethod(): string {
            switch (this.method) {
                case 'delete':
                    return '<span style="color: red;">DELETE</span>';
                case 'put':
                    return '<span style="color: blue;">PUT</span>';
                case 'post':
                    return '<span style="color: rgb(224, 60, 254);">POST</span>';
                case 'get':
                default:
                    return '<span style="color: rgb(20, 215, 20);">GET</span>';
            }
        }

        getUrl(): string {
            // @ts-ignore
            let url: string = this.root + '/' + this.dir + '/';
            // @ts-ignore
            if (this.subDir) url += this.subDir;

            return url;
        }
    }

    new ApiUrl().register();

    /**
     * 自定义操作
     */
    class CustomAction extends aj.VueComponent {
        name: string = "custom-action";

        template = html`<tr>
    <td class="first" style="padding-top: 30px;padding-bottom: 30px;">
        <input type="checkbox" />
        <input style="width:180px;" type="text" v-model="name" placeholder="操作名称，也作为URL" />
        <br />
        <select style="margin:10px 0 0 15px; width:180px;" v-model="method">
            <option value="get">查询操作</option>
            <option value="post">新增操作</option>
            <option value="put">修改操作</option>
            <option value="delete">删除操作</option>
            <option value="get">存储过程</option>
            <option value="get">批处理</option>
        </select>
    </td>
    <td>
        <textarea placeholder="操作说明，描述这个接口的作用。可选的" cols="30" rows="4" style="min-height: 20px;"></textarea>
    </td>
    <td>
        <sql-editor :disabled="!true"></sql-editor>
    </td>
    <td>
        <api-url :root="root" :dir="dir" :sub-dir="name" :method="method"></api-url>
        <br />
        <button title="添加后不是真正的保存，只是添加到缓冲区。\n按上方的【保存】真正保存。\n多次添加后可以一次【保存】。">
            添加
        </button>
    </td>
</tr>`;

        props = {
            root: { type: String, required: true },
            dir: { type: String, required: true },
        };

        data() {
            return {
                name: '',
                method: 'get'
            };
        }
    }

    new CustomAction().register();
}
