<template>
  <a-modal v-model="show" title="新增保险" @cancel="onClose" :width="800">
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
          <a-form-item label='保险名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入保险名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='保险类型' v-bind="formItemLayout">
            <a-select v-decorator="[
            'type',
            { rules: [{ required: true, message: '请输入保险名称!' }] }
            ]"
              style="width: 100%">
              <a-select-option key="1">人身保险</a-select-option>
              <a-select-option key="2">养老保险</a-select-option>
              <a-select-option key="3">车险</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='投保金额' v-bind="formItemLayout">
            <a-input-number style="width: 100%" :min="1" :step="1" v-decorator="[
            'price',
            { rules: [{ required: true, message: '请输入投保金额!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='投保期限(天)' v-bind="formItemLayout">
            <a-input-number style="width: 100%" :min="1" :step="1" v-decorator="[
            'insurancePeriod',
            { rules: [{ required: true, message: '请输入投保期限!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='缴费方式' v-bind="formItemLayout">
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
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'insuranceAdd',
  props: {
    insuranceAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.insuranceAddVisiable
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
          values.policyholder = this.currentUser.userId
          this.loading = true
          this.$post('/cos/insurance-info', {
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
