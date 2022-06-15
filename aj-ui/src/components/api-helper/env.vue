<template>
  <div style="overflow: hidden">
    <div class="left">
      <ul>
        <li
          v-for="env in envs"
          :key="env.id"
          @click="active(env.id)"
          :class="{ actived: env.actived }"
        >
          <Icon
            type="md-trash"
            style="float: right; margin-top: 3px"
            @click="del(env.id, $event)"
          />
          {{ env.name }}
        </li>
      </ul>
      <a class="add" @click="add">+ 新建……</a>
    </div>
    <div class="right">
      <Input v-model="editing.name" ref="inputName">
        <span slot="prepend">环境名称</span>
      </Input>
      <br />
      <Input v-model="editing.urlPrefix">
        <span slot="prepend">URL前缀</span>
      </Input>

      <br />
      <br />
      <p>
        环境变量：在接口文档或测试的过程中，使用 ${变量名} 即可引用该环境变量
      </p>
    </div>
  </div>
</template>

<script lang="ts">
export default {
  props: {},
  data() {
    return {
      envs: [
        {
          id: 1,
          name: "本地测试",
          urlPrefix: "https://dd.com",
          actived: true,
        } as API_HELPER_ENV,
        {
          id: 2,
          name: "生产环境",
          urlPrefix: "http://foo.com",
          actived: false,
        } as API_HELPER_ENV,
      ],
      editing: {} as API_HELPER_ENV,
      isCreate: false,
    };
  },
  methods: {
    add(): void {
      this.isCreate = true;
      this.editing = {};
      this.$refs.inputName.focus();
    },
    /**
     * 点击某个环境，激活之
     */
    active(id: number): void {
      for (let i = 0; i < this.envs.length; i++) {
        let env: API_HELPER_ENV = this.envs[i];

        if (env.id == id) {
          this.editing = env;
          env.actived = true;
        } else env.actived = false;
      }
    },

    /**
     * 删除某个环境
     */
    del(id: number, e: Event): void {
      e.stopPropagation();

      let index: number;
      for (let i = 0; i < this.envs.length; i++) {
        let env: API_HELPER_ENV = this.envs[i];

        if (env.id == id) {
          index = i;
          break;
        }
      }

      if (index == 0 || index) {
        this.envs.splice(index, 1);
        this.editing = {};
      }
    },
  },
};
</script>

<style lang="less" scoped>
.left {
  float: left;
  width: 28%;
  height: 300px;
  border-right: 1px solid #f5f5f5;
}

.right {
  float: right;
  width: 70%;
  padding-left: 10px;
}

ul {
  margin: 0 2%;
}

li {
  border-bottom: 1px solid #f5f5f5;
  padding: 5px 8px;
  cursor: pointer;
  &.actived {
    font-weight: bold;
  }
}

.add {
  //   color: #2d8cf0;
  display: block;
  padding: 5px 10px;
}
</style>