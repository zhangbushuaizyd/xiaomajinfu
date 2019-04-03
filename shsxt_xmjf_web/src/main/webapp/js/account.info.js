$(function () {
    loadAccountInfo();

    loadInvestIncomeInfo();
});


function loadAccountInfo() {

    $.ajax({
        type:"post",
        url:ctx+"/account/countAccountInfoByUserId",
        dataType:"json",
        success:function (data) {
            Highcharts.chart('pie_chart', {
                chart: {
                    spacing : [40, 0 , 40, 0]
                },
                title: {
                    floating:true,
                    text: '总资产:'+data.data2+"￥"
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            format: '<b>{point.name}</b>: {point.y}￥',
                            style: {
                                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                            }
                        },
                        point: {
                            events: {
                                /* mouseOver: function(e) {  // 鼠标滑过时动态更新标题
                                 // 标题更新函数，API 地址：https://api.hcharts.cn/highcharts#Chart.setTitle
                                 chart.setTitle({
                                 text: e.target.name+ '\t'+ e.target.y + ' %'
                                 });
                                 }*/
                                //,
                                // click: function(e) { // 同样的可以在点击事件里处理
                                //     chart.setTitle({
                                //         text: e.point.name+ '\t'+ e.point.y + ' %'
                                //     });
                                // }
                            }
                        },
                    }
                },
                series: [{
                    type: 'pie',
                    innerSize: '80%',
                    name: '金额占比',
                    data:data.data1
                }]
            }, function(c) { // 图表初始化完毕后的会掉函数
                // 环形图圆心
                var centerY = c.series[0].center[1],
                    titleHeight = parseInt(c.title.styles.fontSize);
                // 动态设置标题位置
                c.setTitle({
                    y:centerY + titleHeight/2
                });
            });
        }
    });
}


function loadInvestIncomeInfo() {
    $.ajax({
        type:"post",
        url:ctx+"/invest/countInvestIncomeInfoByUserId",
        dataType:"json",
        success:function (data) {
            Highcharts.chart('line_chart_01', {
                chart: {
                    type: 'spline'
                },
                title: {
                    text: '最近五个月投资与收益资产折线图分布'
                },
                subtitle: {
                    text: '数据来源:小马金服'
                },
                xAxis: {
                    categories: data.data1
                },
                yAxis: {
                    title: {
                        text: '金额'
                    },
                    labels: {
                        formatter: function () {
                            return this.value + '￥';
                        }
                    }
                },
                tooltip: {
                    crosshairs: true,
                    shared: true
                },
                plotOptions: {
                    spline: {
                        marker: {
                            radius: 4,
                            lineColor: '#666666',
                            lineWidth: 1
                        }
                    }
                },
                series: [{
                    name: '投资',
                    marker: {
                        symbol: 'square'
                    },
                    data: data.data2
                }, {
                    name: '收益',
                    marker: {
                        symbol: 'diamond'
                    },
                    data: data.data3
                }]
            });




            Highcharts.chart('line_chart_02',{
                chart: {
                    type: 'column'
                },
                title: {
                    text: '最近五个月投资与收益资产柱状图分布'
                },
                subtitle: {
                    text: '数据来源: 小马金服'
                },
                xAxis: {
                    categories: data.data1,
                    crosshair: true
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: '金额￥'
                    }
                },
                tooltip: {
                    // head + 每个 point + footer 拼接成完整的 table
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.1f} ￥</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        borderWidth: 0
                    }
                },
                series: [{
                    name: '投资',
                    data: data.data2
                }, {
                    name: '收益',
                    data: data.data3
                }]
            });

        }
    });


}