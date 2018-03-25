<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<#include "public/head.ftl">
    <!-- Custom styles for this template -->
    <link href="css/search.css" rel="stylesheet">
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">搜索引擎</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">Home</a></li>
                <li><a href="#about">About</a></li>
                <li><a href="#contact">Contact</a></li>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</nav>

<div class="container">
    <div class="starter-template">
        <h1>Search EveryWhere</h1>
        <form role="form" action="${tour}/search" method="post">
            <div class="form-group">
				<div class="row">
					<div class="col-md-12">
                        <div class="input-group">
                            <span class="input-group-addon" id="basic-addon1">Search</span>
                            <input name="keyWords" type="text" class="form-control" placeholder="Words" aria-describedby="basic-addon1">
                        </div>
					</div>
                </div>
                <button type="submit" class="btn btn-default">搜索</button>
            </div>
        </form>
    </div>

	<div class="row">
        <ul class="media-list">
			<#if data?exists>
				<#list data as result>
					<li class="media">
						<div class="media-left">
							<a href="#">
								<img class="media-object" src="#" alt="#">
							</a>
						</div>
						<div class="media-body">
							<h4 class="media-heading"> <a href="/detailDocById/${result.id}">${result.title}</a></h4>
							<span>${result.describe}</span>
						</div>
					</li>
				</#list>
			</#if>
        </ul>
	</div>
</div><!-- /.container -->

</body>

<#include "public/bottom.ftl">
<script type="text/javascript">
    $(function(){

    });
</script>
</html>
