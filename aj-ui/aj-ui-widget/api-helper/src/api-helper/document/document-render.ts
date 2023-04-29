export default {
    data() {
        return {
            documnetObj: {
                name: "解锁任务中所有的飞机",
                description: "把 空闲、解锁失败的进行解锁",
                httpMethod: "GET",
                url: "/uav/{uav_id}/check_status",
                demoUrl: "http://test.ajaxjs.com:8081/drone/uav/10001/check_status",
                isShow: true
            } as API_HELPER_DOCUMENT,
            args: [
                {
                    desc: "任务 id",
                    name: "task_id",
                    type: "FORM",
                    dataType: "string",
                    comment: "执行任务 execute | 立即返航 return | 结束任务 close 任选一",
                    isRequired: true,
                    values: [
                        { value: "execute", comment: "执行任务" },
                        { value: "return", comment: "立即返航" },
                        { value: "close", comment: "结束任务" },
                    ],
                },
            ] as API_HELPER_ARGUMENT[],
        };
    },
    methods: {
        enlagrn(e: Event): void {
            let img: HTMLElement = <HTMLElement>e.target;
            img.classList.toggle("small");
        },
    },
};