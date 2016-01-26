<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 16-1-11
  Time: 下午3:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <title>SVN</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1">
    <meta http-equiv="description" content="this is svn content">
    <link rel="stylesheet" type="text/css" href="">
    <link rel="stylesheet" type="text/css" href="">
    <script type="text/javascript" src=""></script>
    <script type="text/javascript" src=""></script>

    <script type="text/javascript">
        $(function(){
            $('#test').treegrid({
                title : 'SVN liebiao',
                nowrap :false,
                rownumbers:true,
                collapsible:false,
                url:'svn?pid=',
                idField:'url',
                treeField:'url',
                frozenColumns : [ [ {

                    title : '路径',

                    field : 'url',

                    width : 350,

                    formatter : function(value) {

                        return '<span style="color:red">' + decodeURI(value.substr(value.lastIndexOf("/"))) + '</span>';

                    }

                } ] ],

                columns : [ [ {

                    field : 'name',

                    title : '名称',

                    width : 120

                }, {

                    field : 'size',

                    title : '文件大小(KB)',

                    width : 80,

                    rowspan : 2

                }, {

                    field : 'revision',

                    title : '版本号',

                    width : 80,

                    rowspan : 2

                }, {

                    field : 'author',

                    title : '作者',

                    width : 100,

                    rowspan : 2

                }, {

                    field : 'date',

                    title : '修改日期',

                    width : 130,

                    rowspan : 2

                }, {

                    field : 'commitMessage',

                    title : '注释',

                    width : 150,

                    rowspan : 2

                }, {

                    field : 'kind',

                    title : '操作',

                    width : 120,

                    align : 'center',

                    rowspan : 2,

        })
    </script>
</head>
<body>
       <table id="test"></table>
</body>
</html>