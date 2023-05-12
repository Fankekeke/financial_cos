<template>
  <a-drawer
    title="修改股票"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="stockEditVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form">
      <a-form-item label="股票名称" v-bind="formItemLayout">
        <a-input v-decorator="['name',{rules: [{ required: true, message: '请填写股票名称' }]}]"/>
      </a-form-item>
      <a-form-item label="股票编号" v-bind="formItemLayout">
        <a-input v-decorator="['code',{rules: [{ required: true, message: '请填写股票编号' }]}]"/>
      </a-form-item>
      <a-form-item label="金额" v-bind="formItemLayout">
        <a-input-number v-decorator="['money']" :min="1" :step="0.01" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="买入价格" v-bind="formItemLayout">
        <a-input-number v-decorator="['unit']" :min="1" :step="0.01" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="当前价格" v-bind="formItemLayout">
        <a-input-number v-decorator="['nowMoney']" :min="1" :step="0.01" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="股数" v-bind="formItemLayout">
        <a-input-number v-decorator="['amount']" :min="1" :step="1" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="股票类型" v-bind="formItemLayout">
        <a-select
          :allowClear="true"
          style="width: 100%"
          v-decorator="['type',{rules: [{ required: true, message: '请选择股票类型' }]}]">
          <a-select-option v-for="r in stockTypeData" :key="r.keyy">{{r.valuee}}</a-select-option>
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
  name: 'StockEdit',
  props: {
    stockEditVisiable: {
      default: false
    },
    stockTypeData: {
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
    setFormValues ({...stock}) {
      this.rowId = stock.id
      let fields = ['name', 'code', 'amount', 'unit', 'type', 'money', 'nowMoney']
      let obj = {}
      Object.keys(stock).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = stock[key]
        }
      })
      obj['type'] = obj['type'].toString()
      this.form.setFieldsValue(obj)
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          let stock = this.form.getFieldsValue()
          stock.id = this.rowId
          this.$put('/cos/stock', {
            ...stock
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
  watch: {
    stockEditVisiable: function (val) {
      if (val) {

      }
    }
  }
}
</script>
