
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <#include "public/head.ftl">
</head>

  <body>
  <h1>Temp</h1>

  <#list page.list as result>
 	 ${result.title!}<br/>
 	 ${result.describe}
  </#list>

  </body>

  <#include "public/bottom.ftl">
</html>
