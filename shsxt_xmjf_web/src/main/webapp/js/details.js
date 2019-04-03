$(function () {
    $("#rate").radialIndicator({
        barColor: 'orange',
        barWidth: 5,
        roundCorner : true,
        percentage: true,
        radius:30
    });

    var radialObj = $("#rate").data('radialIndicator');
    radialObj.animate($("#rate").attr("data-val"));

    //点击按钮切换
    $("#tabs div").click(function () {
        $("#tabs div").removeClass("tab_active");
        $(this).addClass("tab_active");
        var show= $("#contents .tab_content").eq($(this).index());
        show.show();
        $("#contents .tab_content").not(show).hide();
        //点击投标记录查询操作
        if($(this).index()==2){
            /**
             * 查询投资记录
             */
            loadInvestListData();
        }

    })

});

function loadInvestListData(pageNum) {
    var p=1;
    if(!isEmpty(pageNum)){
        p=pageNum;
    }
    var itemId=$("#itemId").val();
    $.ajax({
        type:"post",
        url:ctx+"/invest/list",
        data:{
            itemId:itemId,
            pageNum:p
        },
        dataType:"json",
        success:function (data) {
            var pageArray=data.navigatepageNums;
            var list=data.list;
            if(data.total>0){
                /**
                 * tr
                 *   td
                 *     手机号   金额   时间
                 */
                initTrHtml(list);
                initPageHtml(pageArray,data.pageNum);
            }else{
                /*alert("暂无投资记录!");*/
                $("#pages").html("<img style='margin-left: -70px;padding:40px;' " +                     "src='/img/zanwushuju.png'>");
                $("#recordList").html("");
            }

        }
    })
}

//拼接记录
function initTrHtml(list) {
    if(list.length>0){
        var trs="";
        for(var i=0;i<list.length;i++){
            var temp=list[i];
            trs=trs+"<tr><td>"+temp.phone+"</td><td>"+temp.amount+"</td><td>"+temp.time+"</td></tr>";
        }
        $("#recordList").html(trs);
    }
}
//分页按钮
function initPageHtml(pageArray,currentPage) {
    /**
     *   <li class="active"><a title="第一页" >1</a></li>
     <li><a title="第二页">2</a></li>
     <li><a title="第三页">3</a></li>
     */
    var lis="";
    for(var j=0;j<pageArray.length;j++){
        var p=pageArray[j];
        var href="javascript:toPageData("+p+")";
        if(currentPage==p){
            lis=lis+"<li class='active'><a href='"+href+"' title='第"+p+"页' >"+p+"</a></li>";
        }else{
            lis=lis+"<li ><a href='"+href+"' title='第"+p+"页' >"+p+"</a></li>";
        }
    }
    $("#pages").html(lis);
}
//换页
function toPageData(pageNum) {
    loadInvestListData(pageNum);
}

function toRecharge() {
    $.ajax({
        type:"post",
        url:ctx+"/user/checkUserIsRealName",
        dataType:"json",
        success:function (data) {
            if(data.code==200){
                window.location.href=ctx+"/account/recharge";
            }else{
                layer.confirm("尊敬的用户,您还未进行实名认证操作,是否执行实名认证?", {
                    btn: ['前往认证','稍后认证'] //按钮
                }, function(){
                    window.location.href=ctx+"/account/auth";
                }, function(){

                });
            }
        }
    })
}
