<template>
  <a-drawer
    title="修改支出"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="expensesEditVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form">
      <a-form-item label="用户名" v-bind="formItemLayout">
        <a-input readOnly v-decorator="['username',{rules: [{ required: true, message: '请填写用户名' }]}]"/>
      </a-form-item>
      <a-form-item label="交易日期" v-bind="formItemLayout">
        <a-input readOnly v-decorator="['consumeDate',{rules: [{ required: true, message: '请填写交易日期' }]}]"/>
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
import {mapState, mapMutations} from 'vuex'

const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'ExpensesEdit',
  props: {
    expensesEditVisiable: {
      default: false
    },
    consumeTypeData: {
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
    setFormValues ({...expenses}) {
      this.rowId = expenses.id
      let fields = ['username', 'reason', 'amount', 'consumeDate', 'consumeType']
      let obj = {}
      Object.keys(expenses).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = expenses[key]
        }
      })
      obj['consumeType'] = obj['consumeType'].toString()
      this.form.setFieldsValue(obj)
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          let expenses = this.form.getFieldsValue()
          this.$put('/cos/consume', {
            id: this.rowId,
            reason: expenses.reason,
            consumeType: expenses.consumeType,
            amount: expenses.amount
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
