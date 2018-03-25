
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <#include "public/head.ftl">
</head>

  <body>
  <div class="container">
	  <div class="jumbotron">
		  <h3>${data.title}</h3>
		  <p>${data.content}</p>
          <p>${data.describe}</p>
		  <p><a class="btn btn-primary btn-lg" href="#" role="button">Learn More</a></p>
	  </div>

	  <div class="row">
          <table class="table">
              <thead>
              <tr>
                  <th>项目</th>
                  <th>内容</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                  <td>出发城市</td>
                  <td>${data.startCity} </td>
              </tr>
              <tr>
                  <td>价格</td>
                  <td>${data.price} </td>
              </tr>
              <tr>
              <tr>
                  <td>游玩路线</td>
                  <td>${data.tourLine} </td>
              </tr>
              <tr>
                  <td>行程天数</td>
                  <td>${data.tourDays} </td>
              </tr>
              <tr>
                  <td>出发日期</td>
                  <td>${data.startMonth} </td>
              </tr>
              <tr>
                  <td>费用包含</td>
                  <td>${data.costInclude} </td>
              </tr>
              <tr>
                  <td>签证须知</td>
                  <td>${data.visaRequirements} </td>
              </tr>
              <tr>
                  <td>发布时间</td>
                  <td>${data.publishDate} </td>
              </tr>
              </tbody>
          </table>
	  </div>
  </div>
  </body>

  <#include "public/bottom.ftl">
</html>
