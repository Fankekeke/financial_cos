<template>
  <a-drawer
    title="新增收入"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="incomeAddVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form">
      <a-form-item label="用户名" v-bind="formItemLayout">
        <a-select
          :allowClear="true"
          style="width: 100%"
          v-decorator="['belonging',{rules: [{ required: true, message: '请选择用户名' }]}]">
          <a-select-option v-for="r in userList" :key="r.userId">{{r.username}}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="交易日期" v-bind="formItemLayout">
        <a-date-picker v-decorator="['consumeDate',{rules: [{ required: true, message: '请填写交易日期' }]}]" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="事由" v-bind="formItemLayout">
        <a-input v-decorator="['reason']"/>
      </a-form-item>
      <a-form-item label="金额" v-bind="formItemLayout">
        <a-input-number v-decorator="['amount']" :min="1" :step="0.1" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="类别" v-bind="formItemLayout">
        <a-select
          :allowClear="true"
          style="width: 100%"
          v-decorator="['consumeType',{rules: [{ required: true, message: '请选择类别' }]}]">
          <a-select-option v-for="r in consumeTypeData" :key="r.keyy">{{r.valuee}}</a-select-option>
        </a-select>
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
const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'IncomeAdd',
  props: {
    incomeAddVisiable: {
      default: false
    },
    consumeTypeData: {
      type: Array
    },
    userList: {
      type: Array
    }
  },
  data () {
    return {
      income: {},
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this)
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          this.income = values
          this.income.consumeFlag = 1
          this.$post('/cos/consume', {
            ...this.income
          }).then((r) => {
            this.reset()
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
