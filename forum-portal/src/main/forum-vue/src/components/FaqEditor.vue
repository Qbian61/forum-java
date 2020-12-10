<style scoped>
.ivu-btn-success,
.ivu-btn-info {
  padding-left: 25px;
  padding-right: 25px;
  font-weight: 400;
  padding: .375rem 1.37rem;
  font-size: 1rem;
  line-height: 1.5;
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
.ivu-btn-info:hover {
  color: #fff;
}
.ivu-btn-small:hover,
.ivu-btn-info {
  background-color: #fff;
}
.ivu-btn-primary,
.ivu-btn-success,
.ivu-checkbox-checked .ivu-checkbox-inner ,
.ivu-btn-info:hover {
  background-color: #28a745;
}
.article-box-title {
  width: 100%;
  height: 80px;
}
.article-box--title-home,
.article-box-title-input,
.article-box-foot-publish,
.article-box-foot-tag {
  display: block;
  height: 100%;
  border: 0;
  float: left;
}
.article-box-title-input {
  width: calc(100% - 150px);
  padding-left: 10px;
  font-size: 30px;
}
.article-box-title-input:focus {
  outline: none;
}
.article-box--title-home {
  width: 150px;
  padding-top: 22px;
  text-align: center;
}
.article-box-content {
  width: 100%;
  height: calc(100% - 140px);
}
.article-box-foot {
  width: 100%;
  height: 60px;
}
.article-box-foot-tag {
  width: calc(100% - 350px);
  padding-top: 18px;
  padding-left: 15px;
}
.article-box-foot-publish {
  width: 350px;
  padding-top: 10px;
  text-align: center;
}
.tag-group {
  margin-bottom: 10px;
  line-height: 32px;
  font-weight: bold;
  font-size: 14px;
  color: #333;
  border-bottom: 1px solid #EEE;
}
@media (max-width: 768px) {
  .article-box-title-input,
  .article-box-foot-tag,
  .article-box-foot-type,
  .article-box-foot-publish,
  .article-box--title-home {
    width: 100%;
    text-align: left;
    padding-left: 10px;
  }
}
</style>
<template>
  <div class="article-editor article-box" :style="{height: containerHeight}">
    <Modal
        title="选择标签"
        v-model="isShowTagSelectModel"
        :mask-closable="false">
      <div v-for="(tagGroup, index) in showTags" :key="index">
        <h3 class="tag-group">{{tagGroup.groupName}}</h3>
        <CheckboxGroup v-model="selectedTags" @on-change="tagChange">
          <Checkbox border v-for="tag in tagGroup.tags" :key="tag.id" :label="tag.name"></Checkbox>
        </CheckboxGroup>
      </div>
    </Modal>
    <div class="article-box-title">
      <div class="article-box--title-home">
        <Button @click="goHome" type="info">返回首页</Button>
      </div>
      <input v-model="article.title" class="article-box-title-input" placeholder="这里输入问题描述"/>
    </div>
    <mavon-editor
      class="article-box-content"
      placeholder="这里输入问题内容，markdown格式。"
      :style="{zIndex:0}"
      :toolbars="editorSetting"
      v-model="article.markdownContent"
      ref=md
      @save="updateArticle"
      @imgAdd="addImg"/>
    <div class="article-box-foot">
      <div class="article-box-foot-tag">
        <Tag v-for="tag in article.tags"
          :key="tag.id"
          :name="tag.name"
          @on-close="delTag"
          closable
          type="border"
          color="success">{{ tag.name }}</Tag>
        <Button type="dashed" size="small" @click="showTagSelectModel">添加标签</Button>
      </div>
      <div class="article-box-foot-publish">
        <Button @click="updateArticle" type="info">保存并继续编辑</Button>&nbsp;&nbsp;&nbsp;&nbsp;
        <Button @click="saveArticle" type="success">发布</Button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'FaqEditor',
  data () {
    return {
      isShowTagSelectModel: false,
      containerHeight: '500px',
      showTags: [],
      tags: [],
      selectedTags: [],
      article: {
        id: null,
        title: '',
        markdownContent: '',
        tags: []
      },
      editorSetting: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: true, // 上角标
        subscript: true, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: true, // code
        table: true, // 表格
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        // htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        // trash: true, // 清空
        save: true, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        // subfield: true, // 单双栏模式
        preview: true // 预览
      }
    }
  },
  methods: {
    goHome () {
      location.href = '/faq'
    },
    showTagSelectModel () {
      this.isShowTagSelectModel = true
    },
    loadArticle (id) {
      this.$Loading.start()
      this.$http.post(`/faq-rest/${id}`).then(res => {
        if (res.code !== 200) {
          this.$Loading.error()
          return
        }
        this.$Loading.finish()
        this.article = res.data
        document.title = this.article.title
        this.tagChecked()
        this.typeChecked()
      })
    },
    tagChange (checkedNameList) {
      if (checkedNameList.length > 5) {
        this.$Message.error('标签最多选择5个')
        const selectedTagNames = []
        for (let i = 0; i < this.article.tags.length; i++) {
          selectedTagNames.push(this.article.tags[i].name)
        }
        this.selectedTags = selectedTagNames
        return
      }
      const selectedTags = []
      for (let i = 0; i < this.tags.length; i++) {
        for (let j = 0; j < checkedNameList.length; j++) {
          if (this.tags[i].name === checkedNameList[j]) {
            selectedTags.push(this.tags[i])
          }
        }
      }
      this.article.tags = selectedTags
    },
    updateArticle () {
      this.postArticle('更新成功', () => {})
    },
    saveArticle () {
      this.postArticle('保存成功', () => {
        location.href = '/faq'
      })
    },
    postArticle (message, callback) {
      if (!this.preCheck()) {
        return
      }
      const headImgs = this.getHeadImgs()
      this.$Loading.start()
      this.$http.post(`/faq-rest/save`, {
        id: this.article.id,
        typeId: this.article.typeId,
        title: this.article.title,
        contentType: 'MARKDOWN',
        markdownContent: this.article.markdownContent,
        htmlContent: this.$refs.md.d_render,
        tagIds: this.getTagIds(),
        headImg: headImgs.length > 0 ? JSON.stringify(headImgs) : ''
      }).then(res => {
        if (res.code !== 200) {
          this.$Loading.error()
          this.$Message.error(res.message)
          return
        }
        this.$Loading.finish()
        this.article.id = res.data
        this.$Message.success(message)
        callback()
      })
    },
    preCheck () {
      if (!this.article.title) {
        this.$Message.error('标题不能为空')
        return false
      }
      if (this.article.title.length > 40) {
        this.$Message.error('标题不能超过40个字符')
        return false
      }
      if (!this.article.markdownContent) {
        this.$Message.error('内容不能为空')
        return false
      }
      if (!this.article.tags || this.article.tags.length === 0) {
        this.$Message.error('标签不能为空')
        return false
      }
      return true
    },
    tagChecked () {
      if (this.tags.length === 0) {
        return
      }
      for (let i = 0; i < this.tags.length; i++) {
        for (let j = 0; j < this.article.tags.length; j++) {
          if (this.article.tags[j].id === this.tags[i].id) {
            this.selectedTags.push(this.tags[i].name)
          }
        }
      }
    },
    getTagIds () {
      const tagIds = []
      for (let i = 0; i < this.article.tags.length; i++) {
        tagIds.push(this.article.tags[i].id)
      }
      return tagIds
    },
    getHeadImgs () {
      const pattern = /!\[(.*?)\]\((.*?)\)/mg
      const result = []
      let matcher
      while ((matcher = pattern.exec(this.article.markdownContent)) !== null) {
        result.push({
          alt: matcher[1],
          url: matcher[2]
        })
      }
      return result
    },
    addImg (pos, $file) {
      var formdata = new FormData()
      formdata.append('image', $file)
      this.$Loading.start()
      axios({
        url: '/file-rest/upload',
        method: 'post',
        data: formdata,
        headers: {
          'Content-Type': 'multipart/form-data',
          'token': localStorage.getItem('token')
        }
      }).then((res) => {
        if (res.code !== 200) {
          this.$Loading.error()
          return
        }
        this.$Loading.finish()
        this.$refs.md.$img2Url(pos, res.data)
      })
    },
    delTag (event, name) {
      for (let i = 0; i < this.article.tags.length; i++) {
        if (this.article.tags[i].name === name) {
          this.article.tags.splice(i, 1)
        }
      }
    },
    loadTags () {
      this.$Loading.start()
      this.$http.post(`/tag-rest/all`).then(res => {
        if (res.code !== 200) {
          this.$Loading.error()
          return
        }
        this.$Loading.finish()
        const allTags = res.data
        this.tags = res.data
        const showTags = []
        for (let i = 0; i < allTags.length; i++) {
          let hasGroup = false
          for (let j = 0; j < showTags.length; j++) {
            if (allTags[i].groupName === showTags[j].groupName) {
              showTags[j].tags.push(allTags[i])
              hasGroup = true
            }
          }
          if (!hasGroup) {
            showTags.push({
              groupName: allTags[i].groupName,
              tags: [allTags[i]]
            })
          }
        }
        this.showTags = showTags
        this.tagChecked()
      })
    }
  },
  created () {
    let allHeight = window.innerHeight
    this.containerHeight = allHeight + 'px'
    this.loadTags()
    if (this.$route.params.id) {
      this.loadArticle(this.$route.params.id)
    } else {
      document.title = '提问题'
      this.article.markdownContent = `# 一 一级标题

## 1.1 二级标题

- xxxx;
- xxxx;

## 1.2 二级标题

1. xxxx;
2. xxxx;`
    }
  }
}
</script>
