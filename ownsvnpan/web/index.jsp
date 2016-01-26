<%--
  Created by IntelliJ IDEA.
  User: Dmitry.Avdeev
  Date: 10/2/12
  Time: 5:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>使用jquery ajax显示Json数据</title>
    <script language="javascript" src="jquery.js"></script>
    <script language="javascript" src="json.js"></script>
  </head>
  <body>
         <h1>JSON数据显示</h1>
         <input type="button" value="jsonview" onclick="jsonview();">
         <div id="dateMessage">
             <table id="planTable">
                 <tr><td>名称</td><td>年龄</td><td>邮箱</td></tr>
             </table>
         </div>
  </body>
</html>
<script language="javascript">
    function getData(){
        $.getJSON("http://;localhost8080",null,function call(data){
            writeHtml(data);
        });
    }
    function writeHtml(data){
        //alert(data)
        alert(data.jobs);
        var continents=data.jobs;
        for(var i=0;i<continents.length;i++){
            var newLine=$("#planTable").length;
            var row=planTable.insertRow(newLine);
            var col=row.insertCell(0);
            col.innerHTML=continents[i].name;
            col=row.insertCell(1);
            col.innerHTML=continents[i].age;
            col=row.insertCell(2);
            col.innerHTML=continents[i].email;
        }
    }
</script>
