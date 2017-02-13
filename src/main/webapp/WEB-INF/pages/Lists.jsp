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
        <h4>Welcome ${username}</h4><form method="post"><button type="submit" value="(signout)" onclick="form.action='/signout';">Sign in</button></form>
        <h3>Your To Do Lists:</h3>

        <button type="button" id="addNewListButton">Add New List</button>

        <c:forEach var="listValue" items="${lists}">
            <h3>${listValue.listName}</h3>
            <form method="post">
                <input type="submit" value="edit" onclick="form.action='/editList';"/>
                <input type="submit" value="delete" onclick="form.action='/deleteList';"/>
                <input type="hidden" value="${listValue.id}" name="id"/>
                <input type="hidden" value="${listValue.listName}" name="name"/>
                <input type="hidden" value="${listValue.ownerName}" name="ownerName"/>
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
