<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/bootstrap.css"/>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
    <script src="../scripts/jquery-3.1.1.min.js"></script>

    <script>
        $("document").ready(function(){
            $("#addNewListButton").click(function(){
                showAddListDialog();
            });
        });
        function showAddListDialog(){
            $("#addListDialog").show();
        }
    </script>
</head>

<body>
<div class="title">
    Welcome ! Log In To Start Creating ToDoLists
    <form method="post">
        <button class="fileButton" title="Sign In" type="submit" value="(signin)" onclick="form.action='/signin';">
            <img src="/images/Exit.png" />
        </button>
    </form>
</div>

<div class="title">Public To Do Lists:
</div>

<c:forEach var="listValue" items="${lists}">
<div class="section">
<h3>${listValue.listName}</h3>
<form method="post">
<input type="submit" value="view" onclick="form.action='/viewPublicList';"/>
<input type="hidden" value="${listValue.id}" name="id"/>
<input type="hidden" value="${listValue.listName}" name="name"/>
<input type="hidden" value="${listValue.ownerName}" name="ownerName"/>
</form>
</div>
</c:forEach>


</body>
</html>