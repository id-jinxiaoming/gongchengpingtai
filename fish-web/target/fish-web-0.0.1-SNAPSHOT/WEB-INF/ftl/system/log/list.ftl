<!DOCTYPE html>
<html>

<head>

	
	<#include "/common/header.ftl">

	
  
</head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
	    
	    <div class="ibox-content" style="border-color: transparent">
	        <!--搜索框开始-->           
	        <div class="row">
	            <div class="col-sm-12">  
	                                                   
	                <form name="admin_list_sea" class="form-search" method="get" action="${basePath}/system/log/list">
	                    <div class="col-sm-3">
	                        <div class="input-group">
	                            <input type="text" id="key" class="form-control" name="key" value="${key?default('')}" placeholder="输入需查询的帐号" />   
	                            <span class="input-group-btn"> 
	                                <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 搜索</button> 
	                            </span>
	                        </div>
	                    </div>
	                </form>                         
	            </div>
	        </div>
	        <!--搜索框结束-->
	        <div class="hr-line-dashed"></div>
	
	        <div class="example-wrap">
	            <div class="example">
				<@shiro.hasPermission name="system:log:delete">
                    <button type="button" class="btn btn-outline btn-primary" onclick="DelAll()">批量删除</button>
				</@shiro.hasPermission>
		            <table class="table table-bordered table-hover">
		                <thead>
                        		<th>
									<input type="checkbox"  value="" id="all_check" class="all-checks"   >
                                </th>
		                        <th>ID</th>
		                        <th>名称</th>
		                        <th>url</th>
		                        <th>parameter</th>
		                        <th>remoteAddr</th>
		                        <th>agent</th>
		                        <th>用户名</th>
		                        <th>开始时间</th>		                       
		                        <th>结束时间</th>

		                    </tr>
		                </thead>
		             	<script id="datalist" type="text/html">
		                {{# for(var i=0;i<d.length;i++){  }}
							<tr class="long-td">
                                <td>
                                    <input type="checkbox" class="i-checks" value="{{d[i].id}}">
                                </td>
		                        <td>{{d[i].id}}</td>
		                        <td>{{d[i].name}}</td>
							  	<td>{{d[i].url}}</td>
								<td>{{d[i].parameter}}</td>
								<td>{{d[i].remote_addr}}</td>
								<td>{{d[i].agent}}</td>
								<td>{{d[i].account}}</td>
								<td>{{d[i].begin_time}}</td>
								<td>{{d[i].end_time}}</td>

		                        

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
			url: "${basePath}/system/log/list",
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
					//alert(data.records.length);
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
	//删除所有记录
	function DelAll(){
        var checkArray=new Array();
        $(".i-checks").each(function(){
            if(true == $(this).is(':checked')){
				if($(this).val()!="")
				{
                    checkArray.push($(this).val());
				}
            }

        });
        if(checkArray.length<=0)
		{
            layer.msg('没有选中任何记录',{icon:0,time:1500,shade: 0.1});
            return;

		}
        layer.confirm('确认删除吗?', {icon: 3, title:'提示'}, function(index){

            $.ajax({
                url: "${basePath}/system/log/delete",
                data:"id="+checkArray,
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

	</script>
	
</body>
</html>