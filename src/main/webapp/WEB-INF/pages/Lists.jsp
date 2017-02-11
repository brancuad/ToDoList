<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
    <head>
        <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
        <link type="text/css" rel="stylesheet" href="/stylesheets/bootstrap.css"/>
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
        <h4>Welcome ${username} <form method="post"><input type="submit" value="(signout)" onclick="form.action='/signout';"/></form></h4>
        <h3>Your To Do Lists:</h3>

        <div id="addNewListButton">Add New List</div>

        <c:forEach var="listValue" items="${lists}">
            <h3>${listValue}</h3>
            <form method="post">
                <input type="submit" value="edit" onclick="form.action='/editList';"/>
                <input type="submit" value="delete" onclick="form.action='/deleteList';"/>
                <input type="hidden" value="${listValue}" name="name"/>
            </form>
         </c:forEach>

         <div id="addListDialog" style="display: none;">
            Create New List
            <form action="/createList" method="post">
                <input type="text" name="listName">
                <input type="submit" value="Add"/>
            </form>
         </div>
    </body>
</html>