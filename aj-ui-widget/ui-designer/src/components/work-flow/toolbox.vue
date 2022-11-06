<template>
  <ul>
    <li class="selectable selected pointer">
      <img src="@/assets/workflow/select16.gif" /> 选择 Select
    </li>

    <li class="selectable path">
      <img src="@/assets/workflow/flow_sequence.png" /> 流转 Transition
    </li>
    <li class="hr">
      <hr />
    </li>
    <li class="state" data-type="start">
      <img src="@/assets/workflow/start.png" /> 开始节点 Start
    </li>
    <li class="state" data-type="end">
      <img src="@/assets/workflow/end.png" /> 结束节点 End
    </li>
    <li class="state" data-type="task">
      <img src="@/assets/workflow/process.png" /> 任务节点 Task
    </li>
    <li class="state" data-type="custom">
      <img src="@/assets/workflow/process.png" /> 自定义节点 Custom
    </li>
    <li class="state" data-type="subprocess">
      <img src="@/assets/workflow/process.png" /> 子流程 Subprocess
    </li>
    <li class="state" data-type="decision">
      <img src="@/assets/workflow/fork.png" /> 抉择节点 Decision
    </li>
    <li class="state" data-type="fork">
      <img src="@/assets/workflow/fork.png" /> 分支节点 Fork
    </li>
    <li class="state" data-type="join">
      <img src="@/assets/workflow/join.png" /> 合并节点 Join
    </li>
  </ul>
</template>