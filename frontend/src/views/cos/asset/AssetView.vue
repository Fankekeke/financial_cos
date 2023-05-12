<template>
  <a-modal v-model="show" title="固定资产详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px" v-if="assetData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>资产编号：</b>
          {{ assetData.name !== null ? assetData.name : '- -' }}
        </a-col>
        <a-col :span="8"><b>资产名称：</b>
          {{ assetData.name !== null ? assetData.name : '- -' }}
        </a-col>
        <a-col :span="8"><b>总价格：</b>
          {{ assetData.price }} 元
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>还款总月：</b>
          {{ assetData.monthNum }} 月
        </a-col>
        <a-col :span="8"><b>开始日期：</b>
          {{ assetData.startDate !== null ? assetData.startDate : '- -' }}
        </a-col>
        <a-col :span="8"><b>月供金额：</b>
          {{ assetData.monthPrice }} 元
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>当前已还：</b>
          {{ assetData.amount }} 元
        </a-col>
        <a-col :span="8"><b>所属用户：</b>
          {{ assetData.username !== null ? assetData.username : '- -' }}
        </a-col>
        <a-col :span="8"><b>剩余金额：</b>
          {{ assetData.price - assetData.amount }} 元
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" :gutter="15">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">还款记录</span></a-col>
        <a-col :span="24">
          <a-table :columns="columns" :data-source="assetList">
          </a-table>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'
moment.locale('zh-cn')
export default {
  name: 'assetView',
  props: {
    assetShow: {
      type: Boolean,
      default: false
    },
    assetData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.assetShow
      },
      set: function () {
      }
    },
    columns () {
      return [{
        title: '事由',
        dataIndex: 'reason'
      }, {
        title: '金额',
        dataIndex: 'amount',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text + '元'
          } else {
            return '- -'
          }
        }
      }, {
        title: '还款时间',
        dataIndex: 'consumeDate'
      }]
    }
  },
  data () {
    return {
      loading: false,
      assetList: []
    }
  },
  watch: {
    assetShow: function (value) {
      if (value) {
        this.housesInfoByassetId(this.assetData.code)
      }
    }
  },
  methods: {
    housesInfoByassetId (code) {
      this.$get('/cos/asset-info/getConsumeByCode', { code }).then((r) => {
        this.assetList = r.data.data
      })
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
