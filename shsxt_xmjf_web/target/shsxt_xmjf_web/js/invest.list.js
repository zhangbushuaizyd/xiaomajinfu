$(function () {
    /**
     *  ①请求后台投资列表数据
     *  js 属于弱类型语言,里面可以不指定参数
     */
    loadInvestListData();
    /**
     * ②期限点击事件
     */
    $(".tab").click(function () {
        //1.先移除样式
        $(".tab").removeClass("list_active");
        //2. 移除样式后点击哪个按钮,就添加样式 第一种方式
        $(this).addClass("list_active");
        //$(".tab").not($(this)).removeClass("list_active"); //第二种方式
        //3. 拿到兄弟节点的位置 取得期限信息
        var itemCycle = $(this).index();
        var isHistory = 0;
        if (itemCycle == 4) {
            isHistory = 1;
        }
        var itemType = $("#itemType").val();
        loadInvestListData(itemType, itemCycle, isHistory);
    })


});

function loadInvestListData(itemType, itemCycle, isHistory, pageNum, pageSize) {
    //提取ajax参数
    var params = {};
    params.isHistory = 0; //可投项目
    //分页
    params.pageNum = 1;
    params.pageSize = 10;

    //参数校验
    if (!isEmpty(itemCycle)) {
        params.itemCycle = itemCycle;
    }
    if (!isEmpty(itemType)) {
        params.itemType = itemType;
    }
    if (!isEmpty(isHistory)) {
        params.isHistory = isHistory;
    }
    if (!isEmpty(pageNum)) {
        params.pageNum = pageNum;
    }
    if (!isEmpty(pageSize)) {
        params.pageSize = pageSize;
    }
    $.ajax({
        type: "post",
        url: ctx + "/item/list",
        data: params,
        dataType: "json",
        success: function (data) {
            var list = data.list;
            //页码是一个数组
            var pageArray = data.navigatepageNums;
            if (data.total > 0) {
                //(1)有数据,遍历list,拼接到页面  <tbody id="pcItemList">   </tbody>中
                initTrHtml(list);
                //(2)初始化页数
                initPageHtml(pageArray, data.pageNum);
                //(3) 初始化插件
                initItemRate()
                //(4) 初始化倒计时

            } else {
                $("#pcItemList").html("");
                $("#pages").html("");
            }
        }
    })
}

function initTrHtml(list) {
    if (list.length > 0) {
        var trs = "";
        for (var i = 0; i < list.length; i++) {
            //list的js遍历
            var temp = list[i]

            //开头tr
            trs = trs + "<tr>";
            //第①格年化率
            trs = trs + "<td>";
            trs = trs + "<strong>" + temp.item_rate + "</strong>";
            trs = trs + "<span>%";
            //年化率后面小字
            if (temp.item_add_rate > 0) {
                trs = trs + "+" + temp.item_add_rate + "%";
            }
            trs = trs + "</span>";
            trs = trs + "</td>";

            //第②格期限
            trs = trs + "<td>";
            trs = trs + temp.item_cycle;
            if (temp.item_cycle_unit == 1) {
                trs = trs + "天";
            }
            if (temp.item_cycle_unit == 2) {
                trs = trs + "月";
            }
            if (temp.item_cycle_unit == 3) {
                trs = trs + "季度";
            }
            if (temp.item_cycle_unit == 4) {
                trs = trs + "年";
            }
            trs = trs + "</td>";
            //第③格项目名称
            trs = trs + "<td>";
            trs = trs + temp.item_name;
            if (temp.item_isnew == 1) {
                trs = trs + "<strong class='colorful' new>NEW</strong>";
            }
            if (temp.item_isnew == 0 && temp.move_vip == 1) {
                trs = trs + "<strong class='colorful' app>APP</strong>";
            }
            if (temp.item_isnew == 0 && temp.move_vip == 0 && temp.item_isrecommend == 1) {
                trs = trs + "<strong class='colorful' hot>HOT</strong>";
            }
            trs = trs + "</td>";

            //第④格信用等级
            trs = trs + "<td class='trust_range'>";
            if (temp.total > 65 && temp.total <= 75) {
                trs = trs + "B";
            }
            if (temp.total > 75 && temp.total <= 85) {
                trs = trs + "A-";
            }
            if (temp.total > 85 && temp.total <= 90) {
                trs = trs + "A";
            }
            if (temp.total > 90) {
                trs = trs + "A+";
            }
            trs = trs + "</td>";

            //第⑤格担保机构
            trs = trs + "<td>";
            trs = trs + "<image src='/img/logo.png'/>";
            trs = trs + "</td>";

            //第⑥格投资进度
            // trs = trs + "<td>" + temp.item_scale + "%</td>";
            if(temp.item_status==1){
                trs=trs+"<td>";
                trs=trs+"<strong class='countdown time' data-time='"+temp.syTime+"' data-item='"+temp.id+"'>";
                trs=trs+"<time class='hour'></time>";
                trs=trs+" : <time class='min'></time>";
                trs=trs+" : <time class='sec'></time>";
                trs=trs+"</strong>";
                trs=trs+"</td>";
            }else{
                trs=trs+"<td class='data_val' attr-val='"+temp.item_scale+"'></td>";
            }

            //第⑦格具体操作按钮
            trs = trs + "<td>";
            var href=ctx+"/item/details/"+temp.id;
            if(temp.item_status==1){
                trs=trs+"<p><a href='"+href+"'><input class='countdownButton' valid type='button' value='即将开标'></a></p>"
            }
            if(temp.item_status==10 || temp.item_status==13 || temp.item_status==18){
                trs=trs+"<p class='left_money'>可投金额"+temp.syAmount+"元</p><p><a href='"+href+"' ><input valid type='button' value='立即投资'></a></p>"
            }
            if(temp.item_status==20){
                trs=trs+"<p><a href='"+href+"'><input not_valid type='button' value='已抢完'></a></p>"
            }
            if(temp.item_status==30 || temp.item_status==31){
                trs=trs+"  <p><a  href='"+href+"'><input not_valid type='button' value='还款中'></a></p>"
            }
            if(temp.item_status==32){
                trs=trs+" <p style='position: relative'> <a class='yihuankuan' href='"+href+"'>已还款</a><div class='not_valid_pay'></div></p>"
            }
            if(temp.item_status==23 ){
                trs=trs+" <p><a href='"+href+"'><input not_valid type='button' value='已满标'></a></p>"
            }
            trs = trs + "</td>";
            //结尾tr
            trs = trs + "</tr>";

        }
        //.html将字符转换为html
        $("#pcItemList").html(trs);
    }
}

function initPageHtml(pageArray, currentPage) {
    var lis = "";
    //1. 遍历数组
    for (var j = 0; j < pageArray.length; j++) {
        var p = pageArray[j];
        //toPageData 跳转的一个方法
        var href = "javascript:toPageData(" + p + ")";
        if (currentPage == p) {
            lis = lis + "<li class='active'><a href='" + href + "' title='第" + p + "页' >" + p + "</a></li>";
        } else {
            lis = lis + "<li ><a href='" + href + "' title='第" + p + "页' >" + p + "</a></li>";
        }
    }
    $("#pages").html(lis);
}

function initItemData(itemType) {
    //组合查询操作
    var itemCycle;
    $(".tab").each(function () {
        if ($(this).hasClass("list_active")) {
            itemCycle = $(this).index();
        }
    });
    if (itemCycle == 4) {
        isHistory = 1;
    }
    //重新加载数据
    loadInvestListData(itemType, itemCycle, isHistory);
}

//下面页面跳转方法
function toPageData(pageNum) {
    var itemCycle;
    var isHistory = 0;
    $(".tab").each(function () {
        if ($(this).hasClass("list_active")) {
            itemCycle = $(this).index();
        }
    });
    if (itemCycle == 4) {
        isHistory = 1;
    }
    var itemType = $("#itemtype").val();
    loadInvestListData(itemType, itemCycle, isHistory, pageNum);
}

function initItemRate() {
    $(".data_val").each(function () {
        $(this).radialIndicator({
            barColor: 'orange',
            barWidth: 5,
            roundCorner : true,
            percentage: true,
            radius:30
        });

        var radialObj = $(this).data('radialIndicator');
        radialObj.animate($(this).attr("attr-val"));
    })
}

function initInvestDjs() {
    $(".countdown").each(function () {
        var syTime= $(this).attr("data-time");
        var itemId=$(this).attr("data-item");
        timer(syTime,$(this),itemId);
    })
}

function timer(intDiff,obj,itemId){
    if( obj.timers){
        clearInterval( obj.timers);
    }
    obj.timers=setInterval(function(){
        var day=0,
            hour=0,
            minute=0,
            second=0;//时间默认值
        if(intDiff > 0){
            day = Math.floor(intDiff / (60 * 60 * 24));
            hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
            minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour *
                60);
            second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour *
                60 * 60) - (minute * 60);
        }
        if (hour <= 9) hour = '0' + hour;
        if (minute <= 9) minute = '0' + minute;
        if (second <= 9) second = '0' + second;
        obj.find('.hour').html(hour);
        obj.find('.min').html(minute);
        obj.find('.sec').html(second);
        intDiff--;
        if(intDiff==-1){
            $.ajax({
                url : ctx+'/basItem/updateBasItemStatusToOpen',
                dataType : 'json',
                type : 'post',
                data:{
                    itemId:itemId
                },
                success : function(data) {
                    if(data.code==200){
                        window.location.reload()
                    }
                },
                error : function(textStatus, errorThrown) {
                }
            });
        }
    }, 1000);
}