<template>
  <!-- 图标选择器 -->
  <div class="icons">
    <Input v-model="value" :placeholder="placeholder" @on-change="onChange">
    <div slot="prepend" @click="handleIcons">
      <!-- <Icon :type="value" /> -->
      <Icon :type="value" />
    </div>
    </Input>
    <Modal class="modal-icons" v-model="modal" title="选择图标" width="800" @on-cancel="cancel">
      <div>
        <Input v-model="iconName" suffix="ios-search" placeholder="请输入图标名称" style="width: 100%" @on-change="filterIcons" />
        <ul class="icon-list">
          <!-- <li class="list-items" v-for="(item,index) in iconList" :key="index" @click="selectedIcon(item)">
                        <Icon :type="item" />
                        <span>{{item}}</span>
                    </li> -->
          <Row :gutter="24">
            <Col span="12" class="list-items" v-for="(item,index) in iconList" :key="index">
            <div @click="selectedIcon(item)">
              <Icon style="padding-bottom: 5px;font-size: 18px;margin-right: 5px;" :type="item" />
              <span style="font-size: 18px;">{{item}}</span>
            </div>
            </Col>
          </Row>
        </ul>
      </div>
      <div slot="footer">
        <Button @click="cancel">取消</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
// import font from '@/plugins/icon-name.js';
// import fontName from '@bingo/iview-pro-style/style/fonts/iconname.js'

export default {
  name: 'compIcons',
  props: {
    data: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      value: '',
      // iconList: fontName,
      iconList: [],
      modal: false,
      iconName: ''
    }
  },
  watch: {
    data(val, old) {
      this.value = this.data;
    }
  },
  methods: {
    filterIcons(e) {
      this.iconList = fontName;
      if (e.target.value) {
        this.iconList = this.iconList.filter(item => item.includes(e.target.value))
      }

    },
    selectedIcon(name) {
      this.modal = false;
      this.value = name;
      this.iconName = '';
      this.$emit('onChange', name);
    },
    onChange(event) {
      this.$emit('onChange', event.target.value);
    },
    handleIcons() {
      this.iconList = fontName;
      this.modal = true;
    },
    cancel() {
      this.modal = false;
      this.iconName = '';
    }
  }
}
</script>
<style lang="less" scoped>
.icons {
    /deep/.ivu-input-group-prepend {
        width: 40px;
        cursor: pointer;
    }
}
.modal-icons {
    .icon-list {
        height: 450px;
        margin-top: 16px;
        overflow-y: scroll;
    }
    .list-items {
        cursor: pointer;
    }
    /deep/.ivu-modal-wrap {
        display: flex;
        align-items: center;
        justify-content: center;
        .ivu-modal {
            top: 0;
        }
    }
}
</style>