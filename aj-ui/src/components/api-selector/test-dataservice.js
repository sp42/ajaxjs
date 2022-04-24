export default [
  {
    title: "parent 1",
    expand: true,

    children: [
      {
        title: "parent 1-1",
        expand: true,
        children: [
          {
            title: "leaf 1-1-2",
            render: (h, { root, node, data }) => {
              return [
                h("span", { class: "http-method get" }, "GET"),
                h("span", data.title),
              ];
            },
          },
          {
            title: "leaf 1-1-2",
            render: (h, { root, node, data }) => {
              return [
                h("span", { class: "http-method get" }, "GET"),
                h("span", data.title),
              ];
            },
          },
          {
            title: "leaf 1-1-1",
            render: (h, { root, node, data }) => {
              return [
                h("span", { class: "http-method post" }, "POST"),
                h("span", data.title),
              ];
            },
          },
          {
            title: "leaf 1-1-1",
            render: (h, { root, node, data }) => {
              return [
                h("span", { class: "http-method put" }, "PUT"),
                h("span", data.title),
              ];
            },
          },
          {
            title: "leaf 1-1-1",
            render: (h, { root, node, data }) => {
              return [
                h("span", { class: "http-method delete" }, "DELETE"),
                h("span", data.title),
              ];
            },
          },
        ],
      },
      {
        title: "parent 1-2",
        expand: true,
        children: [
          {
            title: "leaf 1-2-1",
          },
          {
            title: "leaf 1-2-1",
          },
        ],
      },
    ],
  },
];
