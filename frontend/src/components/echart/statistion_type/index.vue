<template>
  <div id="chart_type"></div>
</template>

<script>
export default {
  name: 'StatistionType',
  props: {
    statistionTypeData: {
      type: Array
    }
  },
  watch: {
    statistionTypeData: function (val) {
      this.setStatistionType(val)
    }
  },
  data () {
    return {
      myChart: null,
      option: {
        color: ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'],
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '消费类型',
            type: 'pie',
            radius: '50%',
            data: [],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
    }
  },
  mounted () {
    this.myChart = this.$echarts.init(document.getElementById('chart_type'))
  },
  methods: {
    setStatistionType (val) {
      if (val !== undefined) {
        this.option.series[0].data = []
        val.forEach(item => {
          this.option.series[0].data.push({value: item.money, name: item.type})
        })
        this.myChart.setOption(this.option)
      } else {
        this.$message.warning('无数据，请选择其他月份')
      }
    }
  }
}
</script>

<style scoped>

</style>
