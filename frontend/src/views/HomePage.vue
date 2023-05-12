<template>
  <div :class="[multipage === true ? 'multi-page':'single-page', 'not-menu-page', 'home-page']">
    <a-row :gutter="8" class="head-info">
      <a-card class="head-info-card">
        <a-col :span="12">
          <div class="head-info-avatar">
            <img alt="头像" :src="avatar">
          </div>
          <div class="head-info-count">
            <div class="head-info-welcome">
              {{welcomeMessage}}
            </div>
<!--            <div class="head-info-desc">-->
<!--              <p>{{user.deptName ? user.deptName : '暂无部门'}} | {{user.roleName ? user.roleName : '暂无角色'}}</p>-->
<!--            </div>-->
            <div class="head-info-time">上次登录时间：{{user.lastLoginTime ? user.lastLoginTime : '第一次访问系统'}}</div>
          </div>
        </a-col>
        <a-col :span="12">
          <div>
            <a-row class="more-info">
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4">
                <head-info title="总支出" :content="price1" :center="false" :bordered="false"/>
              </a-col>
              <a-col :span="4">
                <head-info title="总收入" :content="price2" :center="false" :bordered="false"/>
              </a-col>
              <a-col :span="4">
              </a-col>
            </a-row>
          </div>
        </a-col>
      </a-card>
    </a-row>
    <a-row :gutter="8" class="count-info">
      <a-col :span="12" class="visit-count-wrapper">
        <div style="background: #ECECEC; padding: 30px">
          <a-card hoverable class="visit-count">
            <div id="countChart" style="height: 262px;" ></div>
          </a-card>
        </div>
      </a-col>
      <a-col :span="12">
        <div style="background: #ECECEC; padding: 30px">
          <a-row :gutter="16">
            <a-col :span="12">
              <a-card hoverable @click="exportInventory">
                <a-statistic
                  title="本月支出"
                  :value="toMonth.income"
                  :precision="2"
                  suffix="￥"
                  :value-style="{ color: '#cf1322' }"
                  style="margin-right: 50px"
                >
                  <template #prefix>
                    <a-icon type="arrow-down" />
                  </template>
                </a-statistic>
              </a-card>
            </a-col>
            <a-col :span="12">
              <a-card hoverable @click="exportInventory2">
                <a-statistic
                  title="本月收入"
                  :value="toMonth.expenses"
                  :precision="2"
                  suffix="￥"
                  class="demo-class"
                  :value-style="{ color: '#3f8600' }"
                >
                  <template #prefix>
                    <a-icon type="arrow-up" />
                  </template>
                </a-statistic>
              </a-card>
            </a-col>
          </a-row>
          <a-card hoverable class="visit-count" style="margin-top: 30px">
            <a-tooltip title="对比日期为今年的1月1日至来年的1月1日">
              <a-icon type="info-circle-o" style="float: right"/>
            </a-tooltip>
            <a-statistic title="今年已过" :value="yearProgress + '%'" style="margin-right: 50px">
              <template #suffix>
                <a-icon type="smile" />
              </template>
            </a-statistic>
            <a-progress
              :stroke-color="{
              '0%': '#108ee9',
              '100%': '#E7310A',
            }"
              :percent="yearProgress"
            />
            <template>已过天数 <b><i>{{dayPassed}}</i></b >天</template>
          </a-card>
        </div>
      </a-col>
    </a-row>
    <a-row :gutter="8" style="margin-top: 30px">
      <a-row :gutter="16">
        <a-col :span="12">
          <div style="background: #ECECEC; padding: 30px">
            <a-card hoverable>
              <a-month-picker placeholder="选择月份" style="position: absolute;right: 30px;z-index: 10" @change="dateChange"/>
              <statistion-type style="height: 260px" :statistionTypeData="statistionTypeData"/>
            </a-card>
          </div>
        </a-col>
        <a-col :span="12">
          <div style="background: #ECECEC; padding: 30px">
            <a-card hoverable>
              <statistion-day style="height: 260px"/>
            </a-card>
          </div>
        </a-col>
      </a-row>
    </a-row>
  </div>
</template>
<script>
import HeadInfo from '@/views/common/HeadInfo'
import StatistionDay from '@/components/echart/statistion_day/index'
import StatistionType from '@/components/echart/statistion_type/index'
import {mapState} from 'vuex'
import moment from 'moment'
import RunningTask from './common/RunningTask'
import { newSpread, fixedForm, floatForm, floatReset, saveExcel } from '@/utils/spreadJS'
moment.locale('zh-cn')
export default {
  name: 'HomePage',
  components: {RunningTask, HeadInfo, StatistionDay, StatistionType},
  data () {
    return {
      series: [],
      projects: [],
      todayIp: '',
      todayVisitCount: '',
      totalVisitCount: '',
      userRole: '',
      userDept: '',
      lastLoginTime: '',
      welcomeMessage: '',
      loadRepo: 0,
      htmlspan: '<span style="display:inline-block;margin-right: 5px;border-radius: 10px;width: 10px;height: 10px;background-color: ',
      legends: ['总数', '您'],
      myChart: {},
      yearProgress: 0,
      dayPassed: 0,
      statistionTypeData: [],
      statistionMonthData: [],
      price1: 0,
      price2: 0,
      toMonth: {
        income: 0,
        expenses: 0
      }
    }
  },
  computed: {
    ...mapState({
      multipage: state => state.setting.multipage,
      user: state => state.account.user
    }),
    avatar () {
      return `static/avatar/${this.user.avatar}`
    }
  },
  methods: {
    home () {
      this.$get('/cos/consume/home').then((r) => {
        console.log(r.data)
        this.price1 = r.data.price1
        this.price2 = r.data.price2
      })
    },
    exportInventory () {
      this.$message.loading('正在生成', 0)
      this.$get('/cos/consume/export', {
        flag: 0
      }).then((r) => {
        let newData = []
        r.data.data.forEach(item => {
          newData.push([item.RowNum.toFixed(0), item.username, item.reason, item.amount, item.type, item.consumeDate])
        })
        let spread = newSpread('income')
        spread = fixedForm(spread, 'income', { title: `${moment().format('YYYY年MM月')}支出统计表` })
        spread = floatForm(spread, 'income', newData)
        saveExcel(spread, '支出统计表.xlsx')
        floatReset(spread, 'income', newData.length)
        this.$message.destroy()
      })
    },
    exportInventory2 () {
      this.$message.loading('正在生成', 0)
      this.$get('/cos/consume/export', {
        flag: 1
      }).then((r) => {
        let newData = []
        r.data.data.forEach(item => {
          newData.push([item.RowNum.toFixed(0), item.username, item.reason, item.amount, item.type, item.consumeDate])
        })
        let spread = newSpread('expenses')
        spread = fixedForm(spread, 'expenses', { title: `${moment().format('YYYY年MM月')}收入统计表` })
        spread = floatForm(spread, 'expenses', newData)
        saveExcel(spread, '收入统计表.xlsx')
        floatReset(spread, 'expenses', newData.length)
        this.$message.destroy()
      })
    },
    dateChange (date, dateString) {
      this.$get('/cos/consume/statistionType', {
        date: dateString + '-01'
      }).then((r) => {
        this.statistionTypeData = r.data.data
      })
    },
    statistionMonth () {
      this.$get('/cos/consume/statistionMonth').then((r) => {
        this.statistionMonthData = r.data
        if (r.data.toMonth !== undefined) {
          if (r.data.toMonth.expenses !== undefined) {
            this.toMonth.expenses = r.data.toMonth.expenses
          }
          if (r.data.toMonth.income !== undefined) {
            this.toMonth.income = r.data.toMonth.income
          }
        }
      })
    },
    welcome () {
      const date = new Date()
      const hour = date.getHours()
      let time = hour < 6 ? '早上好' : (hour <= 11 ? '上午好' : (hour <= 13 ? '中午好' : (hour <= 18 ? '下午好' : '晚上好')))
      // let welcomeArr = [
      //   '喝杯咖啡休息下吧☕',
      //   '要不要和朋友打局LOL',
      //   '要不要和朋友打局王者荣耀',
      //   '几天没见又更好看了呢😍',
      //   '今天又写了几个Bug🐞呢',
      //   '今天在群里吹水了吗',
      //   '今天吃了什么好吃的呢',
      //   '今天您微笑了吗😊',
      //   '今天帮助别人解决问题了吗',
      //   '准备吃些什么呢',
      //   '周末要不要去看电影？'
      // ]
      // let index = Math.floor((Math.random() * welcomeArr.length))
      return `${time}，${this.user.username}`
    },
    getRepos () {
      this.$originalGet('https://api.github.com/users/wuyouzhuguli/repos').then((r) => {
        r.data.forEach(repo => {
          let project = {}
          project.id = repo.id
          project.name = repo.name
          project.description = repo.description
          project.avatar = repo.name.substring(0, 1).toUpperCase()
          this.projects.push(project)
        })
      }).catch(error => {
        console.log(error)
      })
    },
    runningProject () {
      let that = this
      this.$get(`index/${this.user.username}`).then((r) => {
        let data = r.data.data
        this.todayIp = data.todayIp
        this.todayVisitCount = data.todayVisitCount
        this.totalVisitCount = data.totalVisitCount
        let dateArr = []
        let totalSeries = {name: '总数', data: [], type: 'bar'}
        let yourSeries = {name: '您', data: [], type: 'bar'}
        for (let i = 6; i >= 0; i--) {
          let time = moment().subtract(i, 'days').format('MM-DD')
          let contain = false
          for (let o of data.lastSevenVisitCount) {
            if (o.days === time) {
              contain = true
              totalSeries.data.push(o.count)
            }
          }
          if (!contain) {
            totalSeries.data.push(0)
          }
          dateArr.push(time)
        }
        this.series.push(totalSeries)
        for (let i = 6; i >= 0; i--) {
          let time = moment().subtract(i, 'days').format('MM-DD')
          let contain = false
          for (let o of data.lastSevenUserVisitCount) {
            if (o.days === time) {
              contain = true
              yourSeries.data.push(o.count)
            }
          }
          if (!contain) {
            yourSeries.data.push(0)
          }
        }
        this.series.push(yourSeries)
        this.myChart.setOption({
          title: {
            text: '近7日系统访问记录',
            show: true,
            left: 10,
            top: 10
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'line'
            },
            formatter: function name (params) {
              let htmlTip = ''
              for (let i = 0; i < params.length; i++) {
                if (i === 0) {
                  htmlTip += params[i].axisValue + '<br />'
                }
                if (i === (params.length - 1)) {
                  htmlTip += (that.htmlspan + params[i].color + ';"></span>' + params[i].seriesName + ' : ' + params[i].value)
                } else {
                  htmlTip += (that.htmlspan + params[i].color + ';"></span>' + params[i].seriesName + ' : ' + params[i].value + '<br />')
                }
              }
              return htmlTip
            }
          },
          legend: {
            type: 'scroll',
            x: 'center',
            y: 'bottom',
            textStyle: {
              fontSize: '12'
            },
            data: this.legends
          },
          toolbox: {
            show: true,
            right: 20,
            top: 10,
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: true,
            data: dateArr,
            axisLabel: {
              textStyle: {
                fontSize: '12'
              }
            }
          },
          yAxis: {
            type: 'value',
            axisLabel: {
              formatter: '{value}',
              textStyle: {
                fontSize: '12'
              }
            }
          },
          grid: {
            left: '4%'
          },
          series: this.series
        }, true)
      }).catch((r) => {
        console.error(r)
        that.$message.error('获取首页信息失败')
      })
    }
  },
  created () {
    this.getRepos()
    this.loadRepo = 1
  },
  mounted () {
    this.home()
    this.dateChange(null, moment().format('YYYY-MM'))
    this.statistionMonth()
    this.welcomeMessage = this.welcome()
    this.myChart = this.$echarts.init(document.getElementById('countChart'))
    this.runningProject()
    // 设置年进度
    let day = moment().diff(moment().startOf('year'), 'day')
    let dayCount = 0
    for (let i = 1; i < 13; i++) {
      dayCount += moment().month(i).daysInMonth()
    }
    this.dayPassed = day // 已过天数
    this.yearProgress = Math.round(day / dayCount * 100) // 年进度
  }
}
</script>
<style lang="less">
  .home-page {
    .head-info {
      margin-bottom: .5rem;
      .head-info-card {
        padding: .5rem;
        border-color: #f1f1f1;
        .head-info-avatar {
          display: inline-block;
          float: left;
          margin-right: 1rem;
          img {
            width: 5rem;
            border-radius: 2px;
          }
        }
        .head-info-count {
          display: inline-block;
          float: left;
          .head-info-welcome {
            font-size: 1.05rem;
            margin-bottom: .1rem;
          }
          .head-info-desc {
            color: rgba(0, 0, 0, 0.45);
            font-size: .8rem;
            padding: .2rem 0;
            p {
              margin-bottom: 0;
            }
          }
          .head-info-time {
            color: rgba(0, 0, 0, 0.45);
            font-size: .8rem;
            padding: .2rem 0;
          }
        }
      }
    }
    .count-info {
      .visit-count-wrapper {
        padding-left: 0 !important;
        .visit-count {
          padding: .5rem;
          border-color: #f1f1f1;
          .ant-card-body {
            padding: .5rem 1rem !important;
          }
        }
      }
      .project-wrapper {
        padding-right: 0 !important;
        .project-card {
          border: none !important;
          .ant-card-head {
            border-left: 1px solid #f1f1f1 !important;
            border-top: 1px solid #f1f1f1 !important;
            border-right: 1px solid #f1f1f1 !important;
          }
          .ant-card-body {
            padding: 0 !important;
            table {
              width: 100%;
              td {
                width: 50%;
                border: 1px solid #f1f1f1;
                padding: .6rem;
                .project-avatar-wrapper {
                  display:inline-block;
                  float:left;
                  margin-right:.7rem;
                  .project-avatar {
                    color: #42b983;
                    background-color: #d6f8b8;
                  }
                }
              }
            }
          }
          .project-detail {
            display:inline-block;
            float:left;
            text-align:left;
            width: 78%;
            .project-name {
              font-size:.9rem;
              margin-top:-2px;
              font-weight:600;
            }
            .project-desc {
              color:rgba(0, 0, 0, 0.45);
              p {
                margin-bottom:0;
                font-size:.6rem;
                white-space:nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
              }
            }
          }
        }
      }
    }
  }
</style>
