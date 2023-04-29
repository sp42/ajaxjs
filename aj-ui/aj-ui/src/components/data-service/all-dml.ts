/**
 * 标准 DML 配置
 */
export default {
    info: {
        enable: true,
        dir: '',
        isReadAllField: true,
        sql: 'SELECT * FROM ${tableName} WHERE id = #{id}',
        fields: [],
        isQuerySearch: false
    } as DataService_Cfg_Info,
    list: {
        enable: true,
        dir: 'list',
        isReadAllField: true,
        sql: 'SELECT * FROM ${tableName} WHERE 1=1 ORDER BY createDate DESC',
        fields: [],
        pageMode: 1,
        isQuerySearch: true
    } as DataService_Cfg_List,
    create: {
        enable: true,
        dir: '',
        sql: `INSERT INTO \${tableName}
<foreach collection="params" index="key" open="(" close=")" separator=",">\${key}</foreach> 
VALUES <foreach collection="params" index="key" open="(" close=")" separator=",">#{params[\${key}]}</foreach>`,
        isDynamicParse: true,
        fields: [],
        autoDate: true, // 奇怪 不能用下级对象
        addUuid: false,
        createOrSave: false
        // uuid: {
        //     enable: true
        // },
    } as DataService_Cfg_Create,
    update: {
        enable: true,
        dir: '',
        sql: `UPDATE \${tableName} SET 
<foreach collection="params" index="key" item="value" separator=",">
    <if test="key != 'id'">\${key} = #{value}</if>
</foreach> 
WHERE id = #{params[id]};`,
        isDynamicParse: true,
        fields: [],
        isUpdateDate: true
    } as DataService_Cfg_Update,
    delete: {
        enable: true,
        dir: '',
        sql: "DELETE FROM ${tableName} WHERE id IN (${id})",
        isPhysicallyDelete: true
    } as DataService_Cfg_Delete,
    others: [
        // {
        //     type: 'insert',
        //     enable: true,
        //     dir: '',
        //     comment: '先判断记录是否存在，不存在则将记录插入表',
        //     sql: 'INSERT INTO registe_statistics (date) SELECT #{value} WHERE NOT EXISTS (SELECT date FROM registe_statistics WHERE date = #{value})'
        // }
    ],
    "fieldsMapping": {
        "id": "id",
        "createDate": "created_at",
        "createUser": "created_by",
        "updateDate": "updated_at",
        "updateUser": "updated_by",
        "delStatus": "is_del"
    } as DataServiceFieldsMapping
};