<template>
  <a-modal v-model="show" title="修改推荐保险" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
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
          <a-form-item label='担保金额' v-bind="formItemLayout">
            <a-input-number style="width: 100%" :min="1" :step="1" v-decorator="[
            'guaranteePrice',
            { rules: [{ required: true, message: '请输入担保金额!' }] }
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
  name: 'insuranceEdit',
  props: {
    insuranceEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.insuranceEditVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  methods: {
    setFormValues ({...insurance}) {
      this.rowId = insurance.id
      let fields = ['name', 'price', 'type', 'insurancePeriod', 'guaranteePrice']
      let obj = {}
      Object.keys(insurance).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = insurance[key]
        }
      })
      obj['type'] = obj['type'].toString()
      this.form.setFieldsValue(obj)
    },
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
        values.id = this.rowId
        if (!err) {
          this.loading = true
          this.$put('/cos/recommend-info', {
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
