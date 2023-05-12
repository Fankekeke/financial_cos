<template>
  <a-modal v-model="show" title="还款" @cancel="onClose" :width="450">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        还款
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="24">
          <a-form-item label='固定资产名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入固定资产名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='购入价格' v-bind="formItemLayout">
            <a-input-number style="width: 100%" :min="1" :step="1" v-decorator="[
            'price',
            { rules: [{ required: true, message: '请输入购入价格!' }] }
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
  name: 'assetEdit',
  props: {
    assetEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.assetEditVisiable
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
      previewImage: '',
      asset: null
    }
  },
  methods: {
    setFormValues ({...asset}) {
      this.rowId = asset.id
      this.asset = asset
      let fields = ['name', 'price']
      let obj = {}
      Object.keys(asset).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = asset[key]
        }
      })
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
        values.code = this.asset.code
        values.amount = this.asset.monthPrice
        values.consumeType = 5
        values.consumeFlag = 0
        values.deleteFlag = 0
        values.belonging = this.currentUser.userId
        values.consumeDate = new Date()
        values.reason = this.asset.name + '-' + values.content
        if (!err) {
          this.loading = true
          this.$put('/cos/asset-info', {
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
