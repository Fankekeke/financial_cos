<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="基金名称"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.name"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="基金类型"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select
                  :allowClear="true"
                  v-model="queryParams.type"
                  style="width: 100%">
                  <a-select-option v-for="r in fundTypeData" :key="r.keyy">{{r.valuee}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="6" :sm="24">
                <a-form-item
                  label="基金风险"
                  :labelCol="{span: 4}"
                  :wrapperCol="{span: 18, offset: 2}">
                  <a-select
                    :allowClear="true"
                    v-model="queryParams.risk"
                    style="width: 100%">
                    <a-select-option v-for="r in fundRiskData" :key="r.keyy">{{r.valuee}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item
                  label="基金编号"
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
    <!-- 新增基金 -->
    <fund-add
      @close="handleFundAddClose"
      @success="handleFundAddSuccess"
      :fundAddVisiable="fundAdd.visiable"
      :fundTypeData="fundTypeData"
      :fundRiskData="fundRiskData">
    </fund-add>
    <!-- 修改基金 -->
    <fund-edit
      ref="fundEdit"
      @close="handleFundEditClose"
      @success="handleFundEditSuccess"
      :fundEditVisiable="fundEdit.visiable"
      :fundTypeData="fundTypeData"
      :fundRiskData="fundRiskData">
    </fund-edit>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import FundAdd from './FundAdd'
import FundEdit from './FundEdit'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'Fund',
  components: {FundAdd, FundEdit, RangeDate},
  data () {
    return {
      advanced: false,
      fundAdd: {
        visiable: false
      },
      fundEdit: {
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
      fundTypeData: state => state.dict.dicts.fund_TYPE,
      fundRiskData: state => state.dict.dicts.fund_RISK
    }),
    columns () {
      return [{
        title: '基金名称',
        dataIndex: 'name'
      }, {
        title: '基金编号',
        dataIndex: 'code'
      }, {
        title: '所属用户',
        dataIndex: 'username'
      }, {
        title: '净值',
        dataIndex: 'worth',
        customRender: (text, row, index) => {
          return text + '%'
        }
      }, {
        title: '增值',
        dataIndex: 'recent',
        customRender: (text, row, index) => {
          return text + '%'
        }
      }, {
        title: '份额',
        dataIndex: 'share',
        customRender: (text, row, index) => {
          return text + '（份）'
        }
      }, {
        title: '基金类型',
        dataIndex: 'type',
        customRender: (text, row, index) => {
          for (let index in this.fundTypeData) {
            if (text.toString() === this.fundTypeData[index].keyy) {
              return this.fundTypeData[index].valuee
            } else {
              continue
            }
          }
          return text
        }
      }, {
        title: '风险',
        dataIndex: 'risk',
        customRender: (text, row, index) => {
          for (let index in this.fundRiskData) {
            if (text.toString() === this.fundRiskData[index].keyy) {
              return this.fundRiskData[index].valuee
            } else {
              continue
            }
          }
          return text
        }
      }, {
        title: '标签',
        dataIndex: 'tag',
        scopedSlots: {customRender: 'tag'}
      }, {
        title: '基金收益',
        dataIndex: 'price',
        customRender: (text, row, index) => {
          return (row.recent * row.share).toFixed(2) + '元'
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
      this.fundAdd.visiable = true
    },
    handleFundAddClose () {
      this.fundAdd.visiable = false
    },
    handleFundAddSuccess () {
      this.fundAdd.visiable = false
      this.$message.success('新增基金成功')
      this.search()
    },
    edit (record) {
      this.$refs.fundEdit.setFormValues(record)
      this.fundEdit.visiable = true
    },
    handleFundEditClose () {
      this.fundEdit.visiable = false
    },
    handleFundEditSuccess () {
      this.fundEdit.visiable = false
      this.$message.success('修改基金成功')
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
          that.$delete('/cos/fund/' + ids).then(() => {
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
      this.$get('/cos/fund/page', {
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
