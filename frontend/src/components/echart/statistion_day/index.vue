<template>
  <div id="chart_day"></div>
</template>

<script>
export default {
  name: 'StatistionDay',
  data () {
    return {
      myChart: null,
      option: {
        color: ['#00BFFF', '#ADD8E6', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {},
        dataset: {
          source: [
            ['product', '支出', '收入']
          ]
        },
        xAxis: {type: 'category'},
        yAxis: {},
        // Declare several bar series, each will be mapped
        // to a column of dataset.source by default.
        series: [
          {type: 'bar'},
          {type: 'bar'}
        ]
      }
    }
  },
  mounted () {
    this.myChart = this.$echarts.init(document.getElementById('chart_day'))
    this.getStatistionDay()
  },
  methods: {
    getStatistionDay () {
      this.$get('/cos/consume/statistionDay').then((r) => {
        r.data.data.forEach(item => {
          this.option.dataset.source.push([item.date, item.income !== undefined ? parseFloat(item.income) : 0, item.expenses !== undefined ? parseFloat(item.expenses) : 0])
        })
        this.myChart.setOption(this.option)
      })
    }
  }
}
</script>

<style scoped>

</style>
