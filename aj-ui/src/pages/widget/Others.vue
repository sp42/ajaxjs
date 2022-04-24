<template>
  <div>
    <h2>Resize</h2>
    <Resize />

    <br />
    <br />
    <br />
    <h2>文件上传</h2>
    <FileUploader action="ddfdsf" style="margin: 0 auto" />

  </div>
</template>

<script>
import Resize from '../../widget/Resize.vue';
import FileUploader from "@/widget/FileUploader/FileUploader.vue";

export default {
  components: { Resize, FileUploader },
};
</script>