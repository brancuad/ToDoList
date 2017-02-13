<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/bootstrap.css"/>
        <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
    <link rel="icon" href="../images/ToDoListMakerLogo.png" type="image/x-icon" />
    <script src="../scripts/jquery-3.1.1.min.js"></script>

    <style>

    </style>

    <script>
        $("document").ready(function() {
            //event handlers
            $("#itemsTable tr").click(function() {
                if ($(this).hasClass("disabled")) {
                    return;
                }

                $("#itemsTable tr").removeClass("selected");
                $(this).addClass("selected");

                //update the selectedItem field
                $("#selectedItem").attr("value", $(this).attr('id'));
            });

            $("#addNewListItemButton").click(function(){
                $("#newListItemDialog").show();
            });
        });

        function moveDown() {
            $("#" + $("#selectedItem").attr("value")).next().click();
        }

        function moveUp() {
            $("#" + $("#selectedItem").attr("value")).prev().click();
        }

        $(document).keydown(function(e) {
            switch(e.which) {
                case 38: // up
                moveUp();
                break;

                case 40: // down
                moveDown();
                break;

                default: return;
            }
        });
    </script>
</head>

<body>
    <form method="post">
    <div id="file">
        <!-- It doesnt seem like these buttons will be used according to how the application is built -->
        <button class="fileButton" type="submit" title="Create New List">
            <img src="../images/New.png" />
        </button>
        <button class="fileButton" type="submit" title="Load Existing List">
            <img src="../images/Load.png" />
        </button>
        <button class="fileButton" type="submit" title="Save List">
            <img src="../images/Save.png" />
        </button>
        <!-- end comment -->

        <button class="fileButton" type="submit" title="Exit" onclick="form.action='/'">
            <img src="../images/Exit.png" />
        </button>
    </div>
    </form>

    <div class="divider"></div>

    <div class="title">To Do List</div>


    <div id="details" class="section">
        <h3>Details</h3>
        <table id="detailsTable">
            <tr>
                <form method="post" action="/saveListInfo">
                    <td>Name of Todo List:</td>
                    <td>
                        <input type="text" name="name" value="${listName}"/>
                    </td>

                    <td>Name of Owner:</td>
                    <td>
                        <input type="text" name="ownerName" value="${ownerName}" />
                    </td>
                    <input type="hidden" name="listId" value="${listId}"/>
                    <input type="submit" value="Update" />
                </form>
            </tr>
        </table>
    </div>

    <div id="items" class="section">
        <h3>Items</h3>

            <div id="itemTools">

                <form method="post" style="display: inline;">
                    <button type="button" class="itemButton" id="addNewListItemButton" title="Add item">
                        <img src="../images/Add.png" />
                    </button>
                    <button class="itemButton" type="submit" title="Remove Item" onclick="form.action='/removeListItem'">
                        <img src="../images/Remove.png" />
                    </button>
                </form>

                <!-- list name parameter -->
                <input type="hidden" value="${listName}" name="listName"/>

                <!-- id of list being viewed -->
                <input type="hidden" value="${listId}" name="listId"/>

                <input type="hidden" name="ownerName" value="${ownerName}" />

                <!-- keeps track of selected list item id -->
                <input id="selectedItem" type="hidden" name="id"/>

                <button class="itemButton" type="submit" onclick="moveUp()">
                    <img src="../images/MoveUp.png" />
                </button>
                <button class="itemButton" type="submit" onclick="moveDown()">
                    <img src="../images/MoveDown.png" />
                </button>

            </div>

        <table class="table" id="itemsTable">
            <thead>
                <tr class="disabled">
                    <th>Category</th>
                    <th>Description</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Completed</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="listItem" items="${listItems}">
                    <tr id="${listItem.id}">
                        <td>${listItem.category}</td>
                        <td>${listItem.description}</td>
                        <td>${listItem.startDate}</td>
                        <td>${listItem.endDate}</td>
                        <td>${listItem.completed}</td>
                    </tr>
                </c:forEach>

            </tbody>
        </table>
    </div>
    <div id="newListItemDialog" style="display:none;">
        New List Item
        <form action="/createListItem" method="post">
            <input type="text" name="category"/>
            <input type="text" name="description"/>
            <input type="date" name="startDate"/>
            <input type="date" name="endDate"/>
            <input type='checkbox' value="true" name="completed"/>
            <input type='checkbox' checked="checked" value="false" name="completed" style="display: none;"/>
            <input type="hidden" value="${listName}" name="listName"/>
            <input type="hidden" value="${listId}" name="listId"/>
            <input type="hidden" name="ownerName" value="${ownerName}" />
            <input type="submit" value="Add"/>
        </form>
    </div>
</body>
</html>
