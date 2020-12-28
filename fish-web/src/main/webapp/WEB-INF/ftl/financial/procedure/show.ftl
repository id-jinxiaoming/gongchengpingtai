<!DOCTYPE html>
<html>

<head>


<#include "/common/header.ftl">
    <script src="${basePath}/static/js/date.js"></script>


</head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">

    <div class="row">

        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>财务手续资料</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link">
                            <i class="fa fa-chevron-up"></i>
                        </a>
                    </div>
                </div>
                <div class="ibox-content">

                        <div  class="wrapper wrapper-content" id="printDiv">

                        <div class="hr-line-dashed"></div>
                        <table class="table table-bordered table-hover">
                            <thead>
                            <tr>

                            <th style="text-align:center;">id</th>
                            <th style="text-align:center;">手续资料</th>
                            </tr>
                            </thead>
                        <#list item as g>
                            <tr class="long-td">
                                <td>${g.id}</td>
                                <td style="text-align: center;">
                                    <a href="javascript:;"  onclick="openImg('${g.images}')">
                                        <img src="${g.images}" style="width:100px;height:100px" onerror="this.src='${basePath}/static/webupload/empty_mid.png'"/>
                                    </a>
                                   <#--<img src="${g.images}" width="180px" height="100px" />-->
                                </td>
                            </tr>
                        </#list>

                        </table>

                        </div>

                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-sm-8">
                               <#-- <button class="btn btn-primary" onclick="printOrder()" type="button">打印订单</button>-->
                                <a  class="btn btn-danger" type="button"  href="${basePath}/financial/procedure/list"><i class="fa fa-close"></i> 返回</a>
                            </div>
                        </div>

                </div>
            </div>
        </div>
    </div>

</div>



<!-- 全局js -->
<script src="${basePath}/static/hplus/js/jquery.min.js?v=2.1.4"></script>
<script src="${basePath}/static/hplus/js/bootstrap.min.js?v=3.3.6"></script>

<!-- 自定义js -->
<script src="${basePath}/static/hplus/js/content.js?v=1.0.0"></script>
<!-- jQuery Validation plugin javascript-->
<script src="${basePath}/static/hplus/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${basePath}/static/hplus/js/plugins/validate/messages_zh.min.js"></script>

<script src="${basePath}/static/common/js/jquery.form.js"></script>

<script src="${basePath}/static/layer/layer.js"></script>

<script >

    $().ready(function() {

    });

    var indexWin = '';
    //openWin
    function openWin(title,url,height){

        //页面层
        indexWin = layer.open({
            type: 2,
            area: ['500px', height+'px'],
            title:title,
            content:url

        });

    }

    function openImg(img){
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            skin: 'layui-layer-nobg', //没有背景色
            shadeClose: true,
            content:'<img width="100%" height="100%" src="'+img+'" />'
        });
    }


    function update(){
        window.location.reload();

    }

    function printOrder() {
        var headStr = "<html><head><title>商城订单</title></head><body>";
        var footStr = "</body>";
        var bodyStr = document.all.item("printDiv").innerHTML;
        var oldStr = document.body.innerHTML;
        document.body.innerHTML = headStr+bodyStr+footStr;
        window.print();
        document.body.innerHTML=oldStr;

    }






</script>




</body>

</html>