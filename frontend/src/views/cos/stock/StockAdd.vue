<template>
  <a-drawer
    title="新增股票"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="stockAddVisiable"
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
import {mapState} from 'vuex'

const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'StockAdd',
  props: {
    stockAddVisiable: {
      default: false
    },
    stockTypeData: {
      type: Array
    }
  },
  data () {
    return {
      stock: {},
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this)
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
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
          this.stock = values
          this.stock.consumeFlag = 1
          this.stock.userId = this.currentUser.userId
          this.$post('/cos/stock', {
            ...this.stock
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
