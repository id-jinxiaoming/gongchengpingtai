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
                    <h5>用户信息</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link">
                            <i class="fa fa-chevron-up"></i>
                        </a>
                    </div>
                </div>
                <div class="ibox-content">
                    <form class="form-horizontal m-t" id="myForm" action="" method="post" >

                        <input type="hidden" name="userId" value="${item.userId}" />

                        <div class="form-group">
                            <label class="col-sm-3 control-label">用户名：</label>
                            <div class="col-sm-4">
                                <p class="form-control-static">${item.username}&nbsp;&nbsp;<#if item.status == 1>[正常]<#else >[审核中]</#if></p>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">姓名：</label>
                            <div class="col-sm-4">
                                <p class="form-control-static">${item.nickname}</p>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">手机号：</label>
                            <div class="col-sm-4">
                                <p class="form-control-static">${item.mobile}</p>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">隶属项目或公司：</label>
                            <div class="col-sm-4">
                                <p class="form-control-static">${item.affiliateProject}</p>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">状态：</label>
                            <div class="col-sm-4">
                                <p class="form-control-static">${item.status}</p>
                            </div>
                        </div>


                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-sm-8 col-sm-offset-3">
                                <a  class="btn btn-danger" type="button"  href="${basePath}/invoice/application/list"><i class="fa fa-close"></i> 返回</a>
                            </div>
                        </div>
                    </form>
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

        $("#pwdForm").validate({
            rules: {
                name: "required",
                type: "required"

            }
        });

        $('#pwdForm').on('submit', function() {
            ;
            $(this).ajaxSubmit({
                type: 'post',
                beforeSubmit:function(){

                    return $("#pwdForm").valid();

                },
                success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
                    // 此处可对 data 作相关处理

                    if(data.status == 200){

                        layer.msg(data.message,{icon: 6,time:2000,shade: 0.1},function(index){
                            layer.close(index);

                        });
                        layer.close(indexWin);
                    }else{
                        layer.msg(data.message,{icon: 5,time:2000,shade: 0.1},function(index){
                            layer.close(index);
                        });
                    }


                }
            });
            return false; // 阻止表单自动提交事件
        });
        $("#accountForm").validate({
            rules: {
                name: "required",
                type: "required"

            }
        });

        $('#accountForm').on('submit', function() {

            $(this).ajaxSubmit({
                type: 'post',
                beforeSubmit:function(){

                    return $("#accountForm").valid();

                },
                success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
                    // 此处可对 data 作相关处理

                    if(data.status == 200){

                        layer.msg(data.message,{icon: 6,time:2000,shade: 0.1},function(index){
                            layer.close(index);
                            window.location.reload();

                        });

                    }else{
                        layer.msg(data.message,{icon: 5,time:2000,shade: 0.1},function(index){
                            layer.close(index);
                        });
                    }


                }
            });
            return false; // 阻止表单自动提交事件
        });

    });

    var indexWin = '';
    //openWin
    function openWin(title,win){

        //页面层
        indexWin = layer.open({
            type: 1,
            area: ['40%', '68%'],
            title:title,
            shadeClose: true,
            content:$("#"+win+"Win")

        });

    }






</script>




</body>

</html>