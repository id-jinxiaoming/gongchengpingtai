<!DOCTYPE html>
<html>

<head>

	
	<#include "/common/header.ftl">

</head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
        <div class="ibox-title">
            <div class="builder-tabs builder-form-tabs">
                <ul class="nav nav-tabs">
                    <li class="active"><a href="${basePath}/engineering/management/list">工程管理列表</a></li>
                </ul>
            </div>
        </div>
	    <div class="ibox-content" style="border-color: transparent">
	        <!--搜索框开始-->           
	        <div class="row">
	            <div class="col-sm-12">  
	                                                   
	                <form name="admin_list_sea" class="form-search" method="get" action="${basePath}/engineering/management/list">
	                    <div class="col-sm-3" style="padding-right: 0px;padding-left: 0px;">
	                        <div class="input-group">
	                            <input type="text" id="key" class="form-control" name="key" value="${key?default('')}" placeholder="输入手机号" />
	                            <span class="input-group-btn"> 
	                                <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 搜索</button>
	                            </span>
	                        </div>
	                    </div>
	                </form>                         
	            </div>
	        </div>
	        <!--搜索框结束-->

	
	        <div class="example-wrap">
	            <div class="example">

		            <table class="table table-bordered table-hover">
		                <thead>
                            <tr>
                        		<th style="text-align:center;">
									<input type="checkbox"  value="" id="all_check" class="all-checks"   >
                                </th>
                                <th>ID</th>
                                <th>业务名称</th>
                                <th>上传人姓名</th>
                                <th>手机号</th>
								 <th>提交时间</th>
                                <th>操作</th>
                            </tr>
		                </thead>
		             	<script id="datalist" type="text/html">
		                {{# for(var i=0;i<d.length;i++){  }}
							<tr class="long-td">
                                <td>
                                    <input type="checkbox" class="i-checks" value="{{d[i].id}}">
                                </td>
                                <td>{{d[i].id}}</td>
                                <td>
                                    {{# if(d[i].status==1){ }}
                                    财务手续
                                    {{# }else{ }}
                                    工程管理
                                    {{# } }}
                                </td>
                                <td>{{d[i].nickName}}</td>
                                <td>{{d[i].telephone}}</td>
								<td>{{d[i].createTime}}</td>
                                <td>
								<@shiro.hasPermission name="engineering:management:show">

                                    <button type="button" class="btn btn-primary  btn-xs" onclick="show('{{d[i].id}}')"><i class="fa fa-paste"></i>查看资料</button>
								</@shiro.hasPermission>

								<@shiro.hasPermission name="engineering:management:delete">

                                    <button type="button" class="btn btn-primary  btn-xs" onclick="del('{{d[i].id}}')">删除</button>
								</@shiro.hasPermission>

								</td>
		                    </tr>		               
		                {{# } }}
		            	</script>
		                <tbody id="data_list"></tbody>		                
		            </table>

	                <div id="AjaxPage" style=" text-align: right;"></div>
	                <div id="allpage" style=" text-align: right;"></div>
	            </div>
	        </div>
	    </div>
	</div>
</div>

<!-- 等待加载动画 -->
<div class="spiner-example">
    <div class="sk-spinner sk-spinner-three-bounce">
        <div class="sk-bounce1"></div>
        <div class="sk-bounce2"></div>
        <div class="sk-bounce3"></div>
    </div>
</div>

	
	<script src="${basePath}/static/hplus/js/jquery.min.js?v=2.1.4"></script>
    <script src="${basePath}/static/hplus/js/bootstrap.min.js?v=3.3.6"></script>
	<script src="${basePath}/static/hplus/js/plugins/iCheck/icheck.min.js"></script>
  	<script src="${basePath}/static/hplus/js/content.js?v=1.0.0"></script>
  	<script src="${basePath}/static/layer/layer.js"></script>
  	<script src="${basePath}/static/laypage-v1.3/laypage/laypage.js"></script>
  	<script src="${basePath}/static/laytpl/laytpl.js"></script>

  	
	<script type="text/javascript">
	var key="${key?default('')}";
	$(function(){

        initCheck();
        //运行
        Ajaxpage();

    })
	function initCheck(){
        $('.all-checks').iCheck({
            checkboxClass: 'icheckbox_square-green',
            radioClass: 'iradio_square-green',
        });
        $('#all_check').on('ifChecked', function(event){

            $('.i-checks').iCheck('check');


        });
        $('#all_check').on('ifUnchecked', function(event){

            $('.i-checks').iCheck('uncheck');
        });

	}
	//以下将以jquery.ajax为例，演示一个异步分页
	function Ajaxpage(curr){
		$.ajax({
			url: "${basePath}/engineering/management/list",
			data:{page:curr||1,key:key},
			type: "POST",
			dataType:'json',
			success:function(data){
				var data=data.data;
				$(".spiner-example").css('display','none'); //数据加载完关闭动画
				if(data==''||data.records==null||data.records.length==0){
				    $("#data_list").html('<td colspan="20" style="padding-top:10px;padding-bottom:10px;font-size:16px;text-align:center">暂无数据</td>');
				}
				else
				{
					var pages = data.pages;
					dataList(data.records);
				    //显示分页
				    laypage({
				      cont: $('#AjaxPage'), //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
				      pages: pages, //通过后台拿到的总页数
				      skin: '#1AB5B7',//分页组件颜色
			          groups: 3,//连续显示分页数
				      curr: curr || 1, //当前页
				      jump: function(obj, first){ //触发分页后的回调
				        if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
				        	Ajaxpage(obj.curr);
				        }
				      }
				    });
				}
				
			},
			error:function(er){
			
			}
		});
	  
	};

	
	function dataList(list){

	    var tpl = document.getElementById('datalist').innerHTML;
	    laytpl(tpl).render(list, function(html){
	        document.getElementById('data_list').innerHTML = html;
            $('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-green',
            });
	    });
	}

    function show(id){
        window.location.href="${basePath}/engineering/management/show/"+id;

    }

    //删除记录
    function del(id){

        layer.confirm('确认删除吗?', {icon: 3, title:'提示'}, function(index){

            $.ajax({
                url: "${basePath}/engineering/management/delete",
                data:"id="+id,
                type: "POST",
                dataType:'json',
                success:function(res){
                    if(res.status == 200){
                        layer.msg('删除成功',{icon:1,time:1500,shade: 0.1});
                        $('#all_check').iCheck('uncheck');
                        Ajaxpage();


                    }else{
                        layer.msg(res.message,{icon:0,time:1500,shade: 0.1});
                    }

                },
                error:function(er){

                }
            });
            layer.close(index);
        })


    }

    var indexWin = '';
    //openWin
    function check(id){

        //页面层
        indexWin = layer.open({
            type: 2,
            area: ['500px', '350px'],
            title:"审核",
            content:"${basePath}/users/users/check/"+id

        });

    }

	</script>
	
</body>
</html>