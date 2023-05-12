<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="用户名"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.username"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="类别"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select
                  :allowClear="true"
                  v-model="queryParams.consumeType"
                  style="width: 100%">
                  <a-select-option v-for="r in consumeTypeData" :key="r.keyy">{{r.valuee}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="6" :sm="24">
                <a-form-item
                  label="交易时间"
                  :labelCol="{span: 4}"
                  :wrapperCol="{span: 18, offset: 2}">
                  <a-date-picker v-model="queryParams.consumeDateStr" style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item
                  label="事由"
                  :labelCol="{span: 4}"
                  :wrapperCol="{span: 18, offset: 2}">
                  <a-input v-model="queryParams.reason"/>
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
        <template slot="reason" slot-scope="text, record">
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
    <!-- 新增收入 -->
    <income-add
      @close="handleIncomeAddClose"
      @success="handleIncomeAddSuccess"
      :incomeAddVisiable="IncomeAdd.visiable"
      :consumeTypeData="consumeTypeData"
      :userList="userList">
    </income-add>
    <!-- 修改收入 -->
    <income-edit
      ref="incomeEdit"
      @close="handleIncomeEditClose"
      @success="handleIncomeEditSuccess"
      :incomeEditVisiable="IncomeEdit.visiable"
      :consumeTypeData="consumeTypeData">
    </income-edit>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import IncomeAdd from './IncomeAdd'
import IncomeEdit from './IncomeEdit'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'Income',
  components: {IncomeAdd, IncomeEdit, RangeDate},
  data () {
    return {
      advanced: false,
      IncomeAdd: {
        visiable: false
      },
      IncomeEdit: {
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
      consumeTypeData: state => state.dict.dicts.consume_CONSUME_TYPE
    }),
    columns () {
      return [{
        title: '用户名',
        dataIndex: 'username'
      }, {
        title: '事由',
        dataIndex: 'reason',
        scopedSlots: {customRender: 'reason'}
      }, {
        title: '收入金额',
        dataIndex: 'amount',
        customRender: (text, row, index) => {
          return '￥' + text
        }
      }, {
        title: '交易日期',
        dataIndex: 'consumeDate'
      }, {
        title: '类别',
        dataIndex: 'consumeTypeLabel'
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted () {
    this.fetch()
    this.userListData()
  },
  methods: {
    userListData () {
      this.$get('/user/userListAll').then((r) => {
        this.userList = r.data.data
      })
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
      this.IncomeAdd.visiable = true
    },
    handleIncomeAddClose () {
      this.IncomeAdd.visiable = false
    },
    handleIncomeAddSuccess () {
      this.IncomeAdd.visiable = false
      this.$message.success('新增收入成功')
      this.search()
    },
    edit (record) {
      this.$refs.incomeEdit.setFormValues(record)
      this.IncomeEdit.visiable = true
    },
    handleIncomeEditClose () {
      this.IncomeEdit.visiable = false
    },
    handleIncomeEditSuccess () {
      this.IncomeEdit.visiable = false
      this.$message.success('修改收入成功')
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
          that.$delete('/cos/consume/' + ids).then(() => {
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
      params.consumeFlag = 1
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
      if (params.consumeType === undefined) {
        delete params.consumeType
      }
      if (params.consumeDateStr !== null && params.consumeDateStr !== undefined) {
        params.consumeDateStr = moment(params.consumeDateStr).format('YYYY-MM-DD HH:mm:ss')
      } else {
        delete params.consumeDateStr
      }
      this.$get('/cos/consume/income/page', {
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
  watch: {}
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
