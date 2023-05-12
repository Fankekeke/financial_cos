<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="股票名称"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.name"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="股票类型"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select
                  :allowClear="true"
                  v-model="queryParams.type"
                  style="width: 100%">
                  <a-select-option v-for="r in stockTypeData" :key="r.keyy">{{r.valuee}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="6" :sm="24">
                <a-form-item
                  label="股票编号"
                  :labelCol="{span: 4}"
                  :wrapperCol="{span: 18, offset: 2}">
                  <a-input v-model="queryParams.code"/>
                </a-form-item>
              </a-col>
            </template>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
             <a @click="toggleAdvanced" style="margin-left: 8px">
              {{ advanced ? '收起' : '展开' }}
              <a-icon :type="advanced ? 'up' : 'down'"/>
            </a>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <a-button type="primary" ghost @click="add">新增</a-button>
        <a-button @click="batchDelete">删除</a-button>
      </div>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
        <template slot="tag" slot-scope="text, record">
          <a-popover placement="topLeft">
            <template slot="content">
              <div>{{ text }}</div>
            </template>
            <p style="width: 150px;margin-bottom: 0">{{ text }}</p>
          </a-popover>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改"></a-icon>
        </template>
      </a-table>
    </div>
    <!-- 新增股票 -->
    <stock-add
      @close="handleStockAddClose"
      @success="handleStockAddSuccess"
      :stockAddVisiable="stockAdd.visiable"
      :stockTypeData="stockTypeData">
    </stock-add>
    <!-- 修改股票 -->
    <stock-edit
      ref="stockEdit"
      @close="handleStockEditClose"
      @success="handleStockEditSuccess"
      :stockEditVisiable="stockEdit.visiable"
      :stockTypeData="stockTypeData">
    </stock-edit>
<!--    <div id="chartMin" style="width:540px;height:300px"></div>-->
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import StockAdd from './StockAdd'
import StockEdit from './StockEdit'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'Stock',
  components: {StockAdd, StockEdit, RangeDate},
  data () {
    return {
      visible: false,
      myChart: null,
      advanced: false,
      stockAdd: {
        visiable: false
      },
      stockEdit: {
        visiable: false
      },
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      userList: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user,
      stockTypeData: state => state.dict.dicts.stock_TYPE
    }),
    columns () {
      return [{
        title: '所属用户',
        dataIndex: 'username'
      }, {
        title: '股票名称',
        dataIndex: 'name'
      }, {
        title: '股票编号',
        dataIndex: 'code'
      }, {
        title: '金额',
        dataIndex: 'money',
        customRender: (text, row, index) => {
          return text + '￥'
        }
      }, {
        title: '股数',
        dataIndex: 'amount',
        customRender: (text, row, index) => {
          return text + '股'
        }
      }, {
        title: '买入价格',
        dataIndex: 'unit',
        customRender: (text, row, index) => {
          return text + '￥'
        }
      }, {
        title: '当前价格',
        dataIndex: 'nowMoney',
        customRender: (text, row, index) => {
          return text + '￥'
        }
      }, {
        title: '收益',
        dataIndex: 'price',
        customRender: (text, row, index) => {
          return (row.amount * row.nowMoney) + '￥'
        }
      }, {
        title: '类型',
        dataIndex: 'type',
        customRender: (text, row, index) => {
          for (let index in this.stockTypeData) {
            if (text.toString() === this.stockTypeData[index].keyy) {
              return this.stockTypeData[index].valuee
            } else {
              continue
            }
          }
          return text
        }
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    getChart () {
      this.code = (this.a).slice(2)
      if (this.code[0] === 6) {
        this.type = 'sse'
      } else {
        this.type = 'szse'
      }
      // 第三方接口，需要实时刷新用的定时器，并未做websocket的处理，有需要可以自己加
      // this.timer = setInterval(() => {
      // vue项目中为了规范，跨域请求封装了jsonp的方法
      this.$get('http://webstock.quote.hermes.hexun.com/a/minute?code=' +
        this.type + this.code + '&start=20181026000000&number=500&callback=chart2', {fn: 'chart2'}).then((res) => {
        const total = res.Data[0]
        for (const j of Object.keys(total)) {
          const time = total[j][0] + ''
          const year = time.slice(0, 4)
          const month = time.slice(4, 6)
          const day = time.slice(6, 8)
          const hour = time.slice(8, 10)
          const minute = time.slice(10, 12)
          const second = time.slice(12, 14)
          // eslint-disable-next-line no-unused-vars
          const yeartwo = year + '-' + month + '-' + day
          const timetwo = hour + ':' + minute + ':' + second
          this.data1.push(timetwo)
          const chartprice = total[j][1] / 100
          this.data2.push(chartprice)
          const chg = parseFloat(Number((chartprice - this.yes) / this.yes * 100)).toFixed(2)
          this.data3.push(chg)
        }

        this.myChart.setOption({
          // 图相对于容器的位置
          grid: {
            left: '10%',
            right: '10%',
            bottom: '10%'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              animation: false,
              label: {
                backgroundColor: '#505765'
              }
            }
          },
          // 下方滑块
          // dataZoom: [
          //     {
          //         show: true,
          //         realtime: true,
          //     },
          //     {
          //         type: 'inside',
          //         realtime: true,
          //     },
          // ],
          xAxis: [
            {
              type: 'category',
              boundaryGap: false,
              axisLine: {onZero: false},
              data: this.data1
                .map((str) => {
                  return str.replace(' ', '\n')
                })
            }
          ],
          yAxis: [
            {
              name: '价格',
              type: 'value',
              scale: true,
              splitNumber: 4
            },
            {
              name: '涨跌幅(%)',
              type: 'value',
              scale: true,
              splitNumber: 4,
              splitLine: false
            }
          ],
          series: [
            {
              name: '价格',
              type: 'line',
              animation: false,
              symbol: 'none',
              lineStyle: {
                width: 1
              },
              data: this.data2
            },
            {
              name: '涨跌幅',
              type: 'line',
              yAxisIndex: 1,
              animation: false,
              symbol: 'none',
              lineStyle: {
                width: 1,
                color: 'transparent'
              },
              markLine: {
                silent: true,
                // 去掉箭头
                symbol: 'none',
                data: [{
                  yAxis: 0
                }],
                lineStyle: {
                  normal: {
                    type: 'dashed',
                    color: 'red'
                  }
                },
                label: {
                  formatter: ''
                }
              },
              data: this.data3
            }
          ]
        })
      })
      // },3000);
    },
    initchart () {
      this.visible = true
      // setTimeout(() => {
      //   this.myChart = this.$echarts.init(document.getElementById('chartMin'))
      //   this.getChart()
      // }, 500)
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
      if (!this.advanced) {
        this.queryParams.createTimeFrom = ''
        this.queryParams.createTimeTo = ''
      }
    },
    add () {
      this.stockAdd.visiable = true
    },
    handleStockAddClose () {
      this.stockAdd.visiable = false
    },
    handleStockAddSuccess () {
      this.stockAdd.visiable = false
      this.$message.success('新增股票成功')
      this.search()
    },
    edit (record) {
      this.$refs.stockEdit.setFormValues(record)
      this.stockEdit.visiable = true
    },
    handleStockEditClose () {
      this.stockEdit.visiable = false
    },
    handleStockEditSuccess () {
      this.stockEdit.visiable = false
      this.$message.success('修改股票成功')
      this.search()
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    handleDateChange (value) {
      if (value) {
        this.queryParams.createTimeFrom = value[0]
        this.queryParams.createTimeTo = value[1]
      }
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/stock/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      // 清空部门树选择
      this.$refs.deptTree.reset()
      // 清空时间选择
      if (this.advanced) {
        this.$refs.createTime.reset()
      }
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.size = this.paginationInfo.pageSize
        params.current = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.size = this.pagination.defaultPageSize
        params.current = this.pagination.defaultCurrent
      }
      if (params.type === undefined) {
        delete params.type
      }
      if (params.risk === undefined) {
        delete params.risk
      }
      this.$get('/cos/stock/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  },
  watch: {
  }
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
