<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>人气书籍</title>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="js/jquery-1.11.3.min.js"></script>
    <script src="js/highcharts/highcharts.js"></script>
    <script src="js/highcharts/modules/exporting.js"></script>
    <link rel="stylesheet" href="css/bs.css"/>
    <script src="js/base-loading.js"></script>
</head>
<body>
<div class="row" id="center_header" style="height: 100px;border-bottom: 1px solid #CCCCCC;">
</div>
<div id="container" style="min-width:400px;height:400px"></div>
<script>
    $.getJSON('<%=basePath%>'+'admin/report/views/pie', function (data) {
        Highcharts.chart('container', {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: '书籍访问量前8名',
                style:{"fontSize": "22px"}
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
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    }
                }
            },
            series: [{
                name: 'Brands',
                colorByPoint: true,
                data : data
            }]
        });
    })

</script>
</body>
</html>










