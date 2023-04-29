<template>
  <div class="home">
    <h2>表单</h2>
    <p>表单的问题主要是表单布局和表单验证两个。</p>
    <form class="aj-form">
      <table class="aj-form-table">
        <tr>
          <td>昵称</td>
          <td><input type="text" name="name" value="Jack" /></td>
          <td>真实姓名</td>
          <td><input type="text" name="realname" value="" /></td>
        </tr>
        <tr>
          <td>性别</td>
          <td>
            <label><input type="radio" name="gender" value="1" /> 男</label>
            <label><input type="radio" name="gender" value="2" /> 女</label>
            <label><input type="radio" name="gender" value="0" /> 未知</label>
          </td>
          <td>生日</td>
          <td><input type="text" name="phone" value="" /></td>
        </tr>
        <tr>
          <td>电话</td>
          <td><input type="text" name="phone" value="" /></td>
          <td>Email</td>
          <td><input type="text" name="email" value="" /></td>
        </tr>
        <tr>
          <td>地区</td>
          <td colspan="3">
            <input type="text" name="location" value="" />
          </td>
        </tr>
        <tr>
          <td>租户</td>
          <td>
            <select style="width: 180px; margin: 10px 0">
              <option value="${item.id}">${item.name}</option>
            </select>
          </td>
          <td>组织</td>
          <td></td>
        </tr>
      </table>
      <div class="aj-btnsHolder">
        <button>提交</button> <button>复位</button>
      </div>
    </form>
    <h2>文件上传</h2>
    <FileUploader action="ddfdsf" style="margin: 0 auto" />
  </div>
</template>
<script>
import FileUploader from "@/widget/FileUploader/FileUploader.vue";
import Calendar from "@/widget/calendar/Calendar.vue";

export default {
  components: { FileUploader, Calendar },
};
</script>

<style lang="less" scoped>
form {
  width: 600px;
  margin: 0 auto;
}
</style>