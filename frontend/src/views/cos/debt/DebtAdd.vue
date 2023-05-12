<template>
  <a-modal v-model="show" title="新增债务债权" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='被授人' v-bind="formItemLayout">
            <a-input v-decorator="[
            'debtUser',
            { rules: [{ required: true, message: '请输入被授人!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='类型' v-bind="formItemLayout">
            <a-select v-decorator="[
            'debtType',
            { rules: [{ required: true, message: '请输入类型!' }] }
            ]"
                      style="width: 100%">
              <a-select-option key="1">债务</a-select-option>
              <a-select-option key="2">债权</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='债务类型' v-bind="formItemLayout">
            <a-select v-decorator="[
            'debtModel',
            { rules: [{ required: true, message: '请输入债务类型!' }] }
            ]"
                      style="width: 100%">
              <a-select-option key="1">信用还贷</a-select-option>
              <a-select-option key="2">个人借贷</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='金额' v-bind="formItemLayout">
            <a-input-number style="width: 100%" :min="1" :step="1" v-decorator="[
            'price',
            { rules: [{ required: true, message: '请输入金额!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='发生日期' v-bind="formItemLayout">
            <a-date-picker style="width: 100%" v-decorator="[
            'createDate',
            { rules: [{ required: true, message: '请输入发生日期!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='应付日期' v-bind="formItemLayout">
            <a-date-picker style="width: 100%" v-decorator="[
            'updateDate',
            { rules: [{ required: true, message: '请输入发生日期!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='备注' v-bind="formItemLayout">
            <a-textarea :rows="4" v-decorator="[
            'content'
            ]"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
import moment from 'moment'
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'debtAdd',
  props: {
    debtAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.debtAddVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false
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
          values.userId = this.currentUser.userId
          values.createDate = moment(values.createDate).format('YYYY-MM-DD')
          values.updateDate = moment(values.updateDate).format('YYYY-MM-DD')
          this.loading = true
          this.$post('/cos/debt-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
