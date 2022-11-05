const DBType = { 1: 'MySQL', 2: 'Oracle', 3: 'SqlServer', 4: 'Spark', 5: 'SQLite' };

Vue.component('datasource', {
    template: '#DataSourceTpl',
    data() {
        return {
            datasources: [
                {
                    id: 1,
                    name: 'ff'
                }
            ],
            editing: {},
            form: {
                data: {}
            },
            DBType: DBType
        };
    },
    methods: {
        add() {

        }
    }
});