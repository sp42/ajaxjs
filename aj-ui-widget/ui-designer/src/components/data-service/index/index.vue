<template>
  <div class="container">
    <div class="left">
      <Tree ref="treeDataservice" :data="DataServiceTreeData" class="apiTree"></Tree>
    </div>

    <div class="right">
      <ApiSelector />
    </div>
  </div>
</template>

<script>
import ApiSelector from "../../api-selector/index.vue";
export default {
  components: { ApiSelector },
  data() {
    return {
      DataServiceTreeData: [
        {
          title: "parent 1",
          expand: true,

          children: [
            {
              title: "用户角色",
              expand: true,
              render: (h, { root, node, data }) => {
                return [
                  h("span", { class: "http-method get" }, "/cms/user_role"),
                  h("span", data.title),
                ];
              },
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
      ],
    };
  },
};
</script>

<style lang="less" src="../../common-css/left-right-layout.less"></style>