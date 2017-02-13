
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
            Welcome ${username}
            <form method="post">
                <button class="fileButton" title="Sign Out" type="submit" value="(signout)" onclick="form.action='/signout';">
                    <img src="/images/Exit.png" />
                </button>
            </form>
        </div>

        <div class="title">Your To Do Lists:
            <button class="fileButton" type="button" id="addNewListButton" title="Add New List">
                <img src="/images/Add.png" />
            </button>
        </div>

        <c:forEach var="listValue" items="${lists}">
            <div class="section">
                <h3>${listValue.listName}</h3>
                <form method="post">
                    <input type="submit" value="edit" onclick="form.action='/editList';"/>
                    <input type="submit" value="delete" onclick="form.action='/deleteList';"/>
                    <input type="hidden" value="${listValue.id}" name="id"/>
                    <input type="hidden" value="${listValue.listName}" name="name"/>
                    <input type="hidden" value="${listValue.ownerName}" name="ownerName"/>
                </form>
            </div>
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