<template>
  <a-drawer
    title="修改基金"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="fundEditVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form">
      <a-form-item label="基金名称" v-bind="formItemLayout">
        <a-input v-decorator="['name',{rules: [{ required: true, message: '请填写基金名称' }]}]"/>
      </a-form-item>
      <a-form-item label="基金编号" v-bind="formItemLayout">
        <a-input v-decorator="['code',{rules: [{ required: true, message: '请填写基金编号' }]}]"/>
      </a-form-item>
      <a-form-item label="净额" v-bind="formItemLayout">
        <a-input-number v-decorator="['worth']" :min="1" :step="0.01" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="增值" v-bind="formItemLayout">
        <a-input-number v-decorator="['recent']" :min="1" :step="0.01" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="份额" v-bind="formItemLayout">
        <a-input-number v-decorator="['share']" :min="1" :step="0.01" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="基金类型" v-bind="formItemLayout">
        <a-select
          :allowClear="true"
          style="width: 100%"
          v-decorator="['type',{rules: [{ required: true, message: '请选择基金类型' }]}]">
          <a-select-option v-for="r in fundTypeData" :key="r.keyy">{{r.valuee}}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="风险" v-bind="formItemLayout">
        <a-select
          :allowClear="true"
          style="width: 100%"
          v-decorator="['risk',{rules: [{ required: true, message: '请选择风险' }]}]">
          <a-select-option v-for="r in fundRiskData" :key="r.keyy">{{r.valuee}}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="标签" v-bind="formItemLayout">
        <a-textarea v-decorator="['tag']"/>
      </a-form-item>
    </a-form>
    <div class="drawer-bootom-button">
      <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="loading">提交</a-button>
    </div>
  </a-drawer>
</template>
<script>
import {mapState, mapMutations} from 'vuex'

const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'FundEdit',
  props: {
    fundEditVisiable: {
      default: false
    },
    fundTypeData: {
      type: Array
    },
    fundRiskData: {
      type: Array
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      rowId: '',
      loading: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  methods: {
    ...mapMutations({
      setUser: 'account/setUser'
    }),
    onClose () {
      this.loading = false
      this.form.resetFields()
      this.$emit('close')
    },
    setFormValues ({...fund}) {
      this.rowId = fund.id
      let fields = ['name', 'code', 'worth', 'recent', 'type', 'risk', 'tag', 'share']
      let obj = {}
      Object.keys(fund).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = fund[key]
        }
      })
      obj['type'] = obj['type'].toString()
      obj['risk'] = obj['risk'].toString()
      this.form.setFieldsValue(obj)
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          let fund = this.form.getFieldsValue()
          fund.id = this.rowId
          this.$put('/cos/fund', {
            ...fund
          }).then((r) => {
            this.loading = false
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  },
  watch: {}
}
</script>
