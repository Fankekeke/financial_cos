<template>
  <a-card :bordered="false" class="card-area">
    <a-form layout="horizontal">
      <a-row :gutter="15">
        <div>
          <a-col :md="6" :sm="24">
            <a-form-item
              label="查询月份"
              :labelCol="{span: 4}"
              :wrapperCol="{span: 18, offset: 2}">
              <a-month-picker v-model="month" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </div>
        <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
          <a-button type="primary" @click="export1">导出</a-button>
          </span>
      </a-row>
    </a-form>
    <a-row style="width: 100%;">
      <a-col :span="24">
        <a-tabs v-model="activeName" @change="callback">
          <a-tab-pane key="1" tab="支出记录">
            <div id="ss" v-if="activeName == 1 && !loading" style="
              width: 100%;
              height: calc(100vh - 215px);
              border: 1px solid gray;
            "></div>
          </a-tab-pane>
          <a-tab-pane key="2" tab="收入记录" force-render>
            <div id="ss"  v-if="activeName == 2 && !loading" style="
              width: 100%;
              height: calc(100vh - 215px);
              border: 1px solid gray;
            "></div>
          </a-tab-pane>
          <a-tab-pane key="3" tab="基金统计" force-render>
            <div id="ss"  v-if="activeName == 3 && !loading" style="
              width: 100%;
              height: calc(100vh - 215px);
              border: 1px solid gray;
            "></div>
          </a-tab-pane>
          <a-tab-pane key="4" tab="股票统计" force-render>
            <div id="ss"  v-if="activeName == 4 && !loading" style="
              width: 100%;
              height: calc(100vh - 215px);
              border: 1px solid gray;
            "></div>
          </a-tab-pane>
          <a-tab-pane key="5" tab="保险统计" force-render>
            <div id="ss"  v-if="activeName == 5 && !loading" style="
              width: 100%;
              height: calc(100vh - 215px);
              border: 1px solid gray;
            "></div>
          </a-tab-pane>
          <a-tab-pane key="6" tab="总报表" force-render>
            <div id="ss"  v-if="activeName == 6 && !loading" style="
              width: 100%;
              height: calc(100vh - 215px);
              border: 1px solid gray;
            "></div>
          </a-tab-pane>
        </a-tabs>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
// eslint-disable-next-line no-unused-vars
import { newSpread, fixedForm, floatForm, floatReset, saveExcel } from '@/utils/spreadJS'
import moment from 'moment'
import {mapState} from 'vuex'
export default {
  name: 'Report',
  data () {
    return {
      activeName: '1',
      spread: null,
      month: moment(),
      loading: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user,
      fundTypeData: state => state.dict.dicts.fund_TYPE,
      fundRiskData: state => state.dict.dicts.fund_RISK,
      stockTypeData: state => state.dict.dicts.stock_TYPE
    })
  },
  mounted () {
    setTimeout(() => {
      this.exportInventory()
    }, 1000)
  },
  methods: {
    search () {
      switch (this.activeName.toString()) {
        case '1':
          this.exportInventory()
          break
        case '2':
          this.exportInventory2()
          break
        case '3':
          this.exportInventory3()
          break
        case '4':
          this.exportInventory4()
          break
        case '5':
          this.exportInventory5()
          break
        case '6':
          this.exportInventory6()
          break
      }
    },
    callback (key) {
      switch (this.activeName.toString()) {
        case '1':
          this.exportInventory()
          break
        case '2':
          this.exportInventory2()
          break
        case '3':
          this.exportInventory3()
          break
        case '4':
          this.exportInventory4()
          break
        case '5':
          this.exportInventory5()
          break
        case '6':
          this.exportInventory6()
          break
      }
    },
    export1 () {
      if (this.activeName.toString() === '1') {
        this.$get('/cos/consume/export1', {
          flag: 0,
          month: moment(this.month).format('YYYY-MM-01')
        }).then((r) => {
          let newData = []
          r.data.data.forEach(item => {
            newData.push([item.RowNum.toFixed(0), item.username, item.reason, item.amount, item.type, item.consumeDate])
          })
          let spread = newSpread('income')
          spread = fixedForm(spread, 'income', { title: `${moment(this.month).format('YYYY年MM月')}支出统计表` })
          spread = floatForm(spread, 'income', newData)
          saveExcel(spread, '支出统计表.xlsx')
          floatReset(spread, 'income', newData.length)
          this.$message.destroy()
        })
      } else if (this.activeName.toString() === '2') {
        this.$get('/cos/consume/export1', {
          flag: 1,
          month: moment(this.month).format('YYYY-MM-01')
        }).then((r) => {
          let newData = []
          r.data.data.forEach(item => {
            newData.push([item.RowNum.toFixed(0), item.username, item.reason, item.amount, item.type, item.consumeDate])
          })
          let spread = newSpread('expenses')
          spread = fixedForm(spread, 'expenses', { title: `${moment(this.month).format('YYYY年MM月')}收入统计表` })
          spread = floatForm(spread, 'expenses', newData)
          saveExcel(spread, '收入统计表.xlsx')
          floatReset(spread, 'expenses', newData.length)
          this.$message.destroy()
        })
      } else if (this.activeName.toString() === '3') {
        this.$get('/cos/fund/page', {
          size: 9999
        }).then((r) => {
          let newData = []
          r.data.data.records.forEach((item, index) => {
            let typeLabel = ''
            for (let index in this.fundTypeData) {
              if (item.type.toString() === this.fundTypeData[index].keyy) {
                typeLabel = this.fundTypeData[index].valuee
              }
            }
            let riskLabel = ''
            for (let index in this.fundRiskData) {
              if (item.risk.toString() === this.fundRiskData[index].keyy) {
                riskLabel = this.fundRiskData[index].valuee
              }
            }
            newData.push(['No_' + (index + 1), item.username, item.code, item.name, item.worth, item.recent, item.share, typeLabel, riskLabel, (item.recent * item.share).toFixed(2) + '元'])
          })
          let spread = newSpread('fund')
          spread = fixedForm(spread, 'fund', { title: `基金统计表` })
          spread = floatForm(spread, 'fund', newData)
          saveExcel(spread, '基金统计表.xlsx')
          floatReset(spread, 'fund', newData.length)
          this.$message.destroy()
        })
      } else if (this.activeName.toString() === '4') {
        this.$get('/cos/stock/page', {
          size: 9999
        }).then((r) => {
          let newData = []
          this.spread = newSpread('stock')
          r.data.data.records.forEach((item, index) => {
            let typeLabel = ''
            for (let index in this.stockTypeData) {
              if (item.type.toString() === this.stockTypeData[index].keyy) {
                typeLabel = this.stockTypeData[index].valuee
              }
            }
            newData.push(['No_' + (index + 1), item.username, item.code, item.name, item.amount, item.unit, item.nowMoney, typeLabel, (item.amount * item.nowMoney) + '元'])
          })
          let spread = newSpread('stock')
          spread = fixedForm(spread, 'stock', { title: `股票统计表` })
          spread = floatForm(spread, 'stock', newData)
          saveExcel(spread, '股票统计表.xlsx')
          floatReset(spread, 'stock', newData.length)
          this.$message.destroy()
        })
      } else if (this.activeName.toString() === '5') {
        this.$get('/cos/insurance-info/page', {
          size: 9999
        }).then((r) => {
          let newData = []
          this.spread = newSpread('insurance')
          r.data.data.records.forEach((item, index) => {
            let typeLabel = ''
            if (item.type === 1) {
              typeLabel = '人身保险'
            } else if (item.type === 2) {
              typeLabel = '养老保险'
            } else {
              typeLabel = '车险'
            }
            newData.push(['No_' + (index + 1), item.username, item.code, item.name, item.content, item.price, item.insurancePeriod + '天', typeLabel])
          })
          let spread = newSpread('insurance')
          spread = fixedForm(spread, 'insurance', { title: `保险统计表` })
          spread = floatForm(spread, 'insurance', newData)
          saveExcel(spread, '保险统计表.xlsx')
          floatReset(spread, 'insurance', newData.length)
          this.$message.destroy()
        })
      } else if (this.activeName.toString() === '6') {
        this.$get('/cos/consume/allReport').then((r) => {
          let newData = []
          this.spread = newSpread('allReport')
          newData.push([r.data.total1 + '元', r.data.total2 + '元', r.data.total3 + '元', r.data.total4 + '元', r.data.total5 + '元'])
          let spread = newSpread('allReport')
          spread = fixedForm(spread, 'allReport', { title: `总统计表` })
          spread = floatForm(spread, 'allReport', newData)
          saveExcel(spread, '总统计表.xlsx')
          floatReset(spread, 'allReport', newData.length)
          this.$message.destroy()
        })
      }
    },
    exportInventory () {
      this.loading = true
      this.$get('/cos/consume/export1', {
        flag: 0,
        month: moment(this.month).format('YYYY-MM-01')
      }).then((r) => {
        this.loading = false
        setTimeout(() => {
          let newData = []
          if (r.data.data === undefined) {
            return false
          }
          this.spread = newSpread('income')
          r.data.data.forEach(item => {
            newData.push([item.RowNum.toFixed(0), item.username, item.reason, item.amount, item.type, item.consumeDate])
          })
          fixedForm(this.spread, 'income', { title: `${moment(this.month).format('YYYY年MM月')}支出统计表` })
          floatForm(this.spread, 'income', newData)
        }, 200)
      })
    },
    exportInventory2 () {
      this.loading = true
      this.$get('/cos/consume/export1', {
        flag: 1,
        month: moment(this.month).format('YYYY-MM-01')
      }).then((r) => {
        this.loading = false
        setTimeout(() => {
          let newData = []
          if (r.data.data === undefined) {
            return false
          }
          this.spread = newSpread('expenses')
          r.data.data.forEach(item => {
            newData.push([item.RowNum.toFixed(0), item.username, item.reason, item.amount, item.type, item.consumeDate])
          })
          fixedForm(this.spread, 'expenses', { title: `${moment(this.month).format('YYYY年MM月')}收入统计表` })
          floatForm(this.spread, 'expenses', newData)
        }, 200)
      })
    },
    exportInventory3 () {
      this.loading = true
      this.$get('/cos/fund/page', {
        size: 9999
      }).then((r) => {
        this.loading = false
        setTimeout(() => {
          let newData = []
          this.spread = newSpread('fund')
          r.data.data.records.forEach((item, index) => {
            let typeLabel = ''
            for (let index in this.fundTypeData) {
              if (item.type.toString() === this.fundTypeData[index].keyy) {
                typeLabel = this.fundTypeData[index].valuee
              }
            }
            let riskLabel = ''
            for (let index in this.fundRiskData) {
              if (item.risk.toString() === this.fundRiskData[index].keyy) {
                riskLabel = this.fundRiskData[index].valuee
              }
            }
            newData.push(['No_' + (index + 1), item.username, item.code, item.name, item.worth, item.recent, item.share, typeLabel, riskLabel, (item.recent * item.share).toFixed(2) + '元'])
          })
          fixedForm(this.spread, 'fund', { title: '基金统计表' })
          floatForm(this.spread, 'fund', newData)
        }, 200)
      })
    },
    exportInventory4 () {
      this.loading = true
      this.$get('/cos/stock/page', {
        size: 9999
      }).then((r) => {
        this.loading = false
        setTimeout(() => {
          let newData = []
          this.spread = newSpread('stock')
          r.data.data.records.forEach((item, index) => {
            let typeLabel = ''
            for (let index in this.stockTypeData) {
              if (item.type.toString() === this.stockTypeData[index].keyy) {
                typeLabel = this.stockTypeData[index].valuee
              }
            }
            newData.push(['No_' + (index + 1), item.username, item.code, item.name, item.amount, item.unit, item.nowMoney, typeLabel, (item.amount * item.nowMoney) + '元'])
          })
          fixedForm(this.spread, 'stock', { title: '股票统计表' })
          floatForm(this.spread, 'stock', newData)
        }, 200)
      })
    },
    exportInventory5 () {
      this.loading = true
      this.$get('/cos/insurance-info/page', {
        size: 9999
      }).then((r) => {
        this.loading = false
        setTimeout(() => {
          let newData = []
          this.spread = newSpread('insurance')
          r.data.data.records.forEach((item, index) => {
            let typeLabel = ''
            if (item.type === 1) {
              typeLabel = '人身保险'
            } else if (item.type === 2) {
              typeLabel = '养老保险'
            } else {
              typeLabel = '车险'
            }
            newData.push(['No_' + (index + 1), item.username, item.code, item.name, item.content, item.price, item.insurancePeriod + '天', typeLabel])
          })
          fixedForm(this.spread, 'insurance', { title: '保险统计表' })
          floatForm(this.spread, 'insurance', newData)
        }, 200)
      })
    },
    exportInventory6 () {
      this.loading = true
      this.$get('/cos/consume/allReport').then((r) => {
        this.loading = false
        setTimeout(() => {
          let newData = []
          this.spread = newSpread('allReport')
          newData.push([r.data.total1 + '元', r.data.total2 + '元', r.data.total3 + '元', r.data.total4 + '元', r.data.total5 + '元'])
          fixedForm(this.spread, 'allReport', { title: '总统计表' })
          floatForm(this.spread, 'allReport', newData)
        }, 200)
      })
    }
  }
}
</script>

<style scoped>

</style>
