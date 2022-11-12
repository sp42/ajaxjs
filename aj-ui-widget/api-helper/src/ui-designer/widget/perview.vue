<template>
  <!-- 预览 -->
  <Modal v-model="isShowPerview" fullscreen footer-hide title="预览">
    <div v-for="(item, index) in metaData" :key="item.type +'_'+ index">
      <Form v-if="item.type == 'form'" class="widget-form" :label-width="item.labelWidth" :label-position="item.labelPosition">
        <FormItem v-for="(formItem, formItem_index) in item.children" :key="formItem.type +'_'+ formItem_index" :label="formItem.label">
          <span v-for="(formEl, formEl_index) in formItem.children" :key="formEl.type +'_'+ formEl_index">
            <!-- <RenderCmp :item="formEl" :meta-data="metaData" :is-wysiwyg="false" /> -->
          </span>
        </FormItem>
      </Form>
    </div>
  </Modal>
</template>

<script>
// import RenderCmp from './render-cmp.vue';

export default {
  components: {},
  props: {
    metaData: { type: Array, required: true }
  },
  data() {
    return { isShowPerview: false }
  }
}
</script>