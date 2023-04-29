<template>
  <div class="aj-form-calendar">
    <div class="selectYearMonth">
      <a href="###" @click="getDate('preYear')" class="preYear" title="上一年">	&#171; <!-- &lt; --></a>
      <select @change="setMonth" v-model="month">
        <option value="1">一月</option>
        <option value="2">二月</option>
        <option value="3">三月</option>
        <option value="4">四月</option>
        <option value="5">五月</option>
        <option value="6">六月</option>
        <option value="7">七月</option>
        <option value="8">八月</option>
        <option value="9">九月</option>
        <option value="10">十月</option>
        <option value="11">十一月</option>
        <option value="12">十二月</option>
      </select>
      <a href="###" @click="getDate('nextYear')" class="nextYear" title="下一年"> &#187;<!-- &gt; --> </a>
    </div>
    <div class="showCurrentYearMonth">
      <span class="showYear">{{year}}</span>/<span class="showMonth">{{month}}</span>
    </div>
    <table>
      <thead>
        <tr>
          <td>日</td>
          <td>一</td>
          <td>二</td>
          <td>三</td>
          <td>四</td>
          <td>五</td>
          <td>六</td>
        </tr>
      </thead>
      <tbody @click="pickDay"></tbody>
    </table>
    <div v-if="showTime" class="showTime">
      时 <select class="hour aj-select">
        <option v-for="n in 24" :key="n">{{n}}</option>
      </select>
      分 <select class="minute aj-select">
        <option v-for="n in 61" :key="n">{{n - 1}}</option>
      </select>
      <a href="#" @click="pickupTime">选择时间</a>
    </div>
  </div>
</template>

<script lang="ts" src="./Calendar.ts"></script>

<style lang="less" scoped  src="./Calendar.less"></style>