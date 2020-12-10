<style>
html,body {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  border: 0;
}
* {
  box-sizing: border-box;
}
.ivu-modal-content,
.ivu-btn-large,
.ivu-btn,
.ivu-tag,
.ivu-select-selection {
  border-radius: 0 !important;
}
.ivu-modal-body {
  max-height: 500px !important;
  overflow: scroll !important;
}
.ivu-select-selection:hover,
.ivu-checkbox-checked .ivu-checkbox-inner,
.ivu-btn-info:hover,
.ivu-btn-small:hover,
.ivu-btn-info,
.ivu-btn-primary,
.ivu-btn-success {
  border: 1px solid #28a745;
}
.ivu-btn-small:hover,
.ivu-btn-info,
.ivu-btn-text:hover {
  color: #28a745;
}
.ivu-checkbox-checked .ivu-checkbox-inner,
.ivu-btn-primary {
  background-color: #28a745;
}
</style>
<template>
  <div id="app">
    <router-view/>
  </div>
</template>
<script>
export default {
  name: 'App'
}
</script>
