<template>
  <div>
    <RadioGroup v-model="requestType" type="button" size="small">
      <Radio label="Form"></Radio>
      <Radio label="FormData"></Radio>
      <Radio label="JSON"></Radio>
      <Radio label="Raw"></Radio>
    </RadioGroup>

    <span v-if="requestType == 'JSON'" class="jsonRootType">JSON 根类型
      <RadioGroup v-model="json.rootType" size="small">
        <Radio label="Object" size="small"></Radio>
        <Radio label="Array"></Radio>
      </RadioGroup>
    </span>

    <div style="margin-top: 20px">
      <section v-if="requestType == 'Form'">
        <Table mode="SIMPLE" />

      </section>
      <section v-if="requestType == 'JSON'">
        <Table mode="SCHEME" />

      </section>

      <section v-if="requestType == 'Raw'">
        <div style="width:500px;margin:10px 0;">
          ContentType
          <Select v-model="raw.contentType" style="width:200px;" size="small" transfer>
            <Option value="application/json">application/json</Option>
            <Option value="application/x-www-form-urlencoded">application/x-www-form-urlencoded</Option>
            <Option value="multipart/form-data">multipart/form-data</Option>
            <Option value="text/plain">text/plain</Option>
            <Option value="text/plain">text/xml</Option>
          </Select>
        </div>
        <Input type="textarea" :rows="4" />
      </section>
    </div>
  </div>
</template>

<script lang="ts">
import Table from "./table.vue";
export default {
  components: { Table },
  data() {
    return {
      requestType: "JSON",
      json: {
        rootType: "Object",
      },
      raw: {
        contentType: "application/json",
      },
    };
  },
};
</script>

<style lang="less" scoped>
.jsonRootType {
  font-size: 9pt;
  padding-left: 20px;
}
</style>