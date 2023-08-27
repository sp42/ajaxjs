const url = "http://localhost:8302/";

new Vue({
    el: '#vue',
    data: data,
    methods: {
        go(data) {
            let ifrmae = document.querySelector("iframe");
            let _url;

            if (data.type == 1)
                _url = url + `show-list.jsp?id=${data.id}&title=${data.name}&th=${data.th}`;

            if (data.type == 2)
                _url = url + `show-map.jsp?id=${data.id}&title=${data.name}`;

            if (data.note)
                _url += `&note=${data.note}`;

            ifrmae.src = _url;
        }
    }
});