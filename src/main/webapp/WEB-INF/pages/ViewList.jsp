<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
    <link type="text/css" rel="stylesheet" href="/stylesheets/bootstrap.css"/>
    <link rel="icon" href="../images/ToDoListMakerLogo.png" type="image/x-icon" />
    <script src="../scripts/jquery-3.1.1.min.js"></script>

    <style>
        body {
            font-family: Verdana, Helvetica, sans-serif;
            background-color: lightblue;
        }

        #file {
            margin: 10px;
        }

        .fileButton {
            padding: 5px;
        }

        .itemButton {
            padding: 5px;
        }

        .divider {
            background-color: #33393c;
            height: 5px;
            width: 100%;
        }

        .section {
            border: 2px solid #33393c;
            padding: 10px;
            margin: 10px;
        }

        #itemsTable {
            border: 1px solid #33393c;
            margin-top: 10px;
        }

        #detailsTable td {
            padding: 5px;
        }

        .title {
            font-size: 24px;
            font-weight: bold;
            margin: 10px;
        }

        .selected {
            background-color: white;
        }

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

                //update the itemToBeDeleted field
                $("#itemToBeDeleted").attr("value", $(this).attr('id'));
                $("#itemToBeEdited").attr("value", $(this).attr('id'));
            });

            $("#addNewListItemButton").click(function(){
                $("#newListItemDialog").show();
            });
        });

        function populateEditListItemDialog(){
            //we are setting the value of the edit text fields to the html between the td elements of the
            //selected list item row
            $("#editItemCategoryInput").val($(".selected #itemCategory").html());
            $("#editItemDescriptionInput").val($(".selected #itemDescription").html());
            $("#editItemStartDateInput").val($(".selected #itemStartDate").html());
            $("#editItemEndDateInput").val($(".selected #itemEndDate").html());
            if($(".selected #itemCompleted").html() == "true"){
                $("#editItemCompleted").attr("checked", true);
            }
            else{
                $("#editItemCompleted").attr("checked", false);
            }

            $("#editListItemDialog").show();
        }
    </script>
</head>

<body>
    <form method="post">
    <div id="file">
        <!-- It doesnt seem like these buttons will be used according to how the application is built -->
        <%--<button class="fileButton" type="submit" title="Create New List">--%>
            <%--<img src="../images/New.png" />--%>
        <%--</button>--%>
        <%--<button class="fileButton" type="submit" title="Load Existing List">--%>
            <%--<img src="../images/Load.png" />--%>
        <%--</button>--%>
        <%--<button class="fileButton" type="submit" title="Save List">--%>
            <%--<img src="../images/Save.png" />--%>
        <%--</button>--%>
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

        <form method="post">
            <div id="itemTools">
                <button type="button" class="itemButton" id="addNewListItemButton" title="Add item">
                    <img src="../images/Add.png" />
                </button>
                <button class="itemButton" type="submit" title="Remove Item" onclick="form.action='/removeListItem'">
                    <img src="../images/Remove.png" />
                </button>

                <!-- list name parameter -->
                <input type="hidden" value="${listName}" name="listName"/>

                <!-- id of list being viewed -->
                <input type="hidden" value="${listId}" name="listId"/>

                <input type="hidden" name="ownerName" value="${ownerName}" />

                <!-- keeps track of selected list item id -->
                <input id="itemToBeDeleted" type="hidden" name="id"/>

                <button class="itemButton" type="submit" onclick="form.action='/moveItemUp';">
                    <img src="../images/MoveUp.png" />
                </button>
                <button class="itemButton" type="submit" onclick="form.action='/moveItemDown';">
                    <img src="../images/MoveDown.png" />
                </button>
                <button class="fileButton" type="button" id="updateListItemButton" title="Edit" onclick="populateEditListItemDialog()">Update
                </button>
            </div>
        </form>

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
                        <td id="itemCategory">${listItem.category}</td>
                        <td id="itemDescription">${listItem.description}</td>
                        <td id="itemStartDate">${listItem.startDate}</td>
                        <td id="itemEndDate">${listItem.endDate}</td>
                        <td id="itemCompleted">${listItem.completed}</td>
                    </tr>
                </c:forEach>

            </tbody>
        </table>
    </div>
    <div id="newListItemDialog" style="display:none;">
        New Task
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
    <div id="editListItemDialog" style="display:none;">
        Edit Task
        <form method="post" action="/editListItem">
            <input id="editItemCategoryInput" type="text" name="category"/>
            <input id="editItemDescriptionInput" type="text" name="description"/>
            <input id="editItemStartDateInput" type="text" name="startDate"/>
            <input id="editItemEndDateInput" type="text" name="endDate"/>
            <input id="editItemCompleted" type='checkbox' value="true" name="completed"/>
            <input type='checkbox' checked="checked" value="false" name="completed" style="display: none;"/>
            <input type="hidden" value="${listName}" name="listName"/>
            <input type="hidden" value="${listId}" name="listId"/>
            <input type="hidden" name="ownerName" value="${ownerName}" />
            <!-- keeps track of selected list item id -->
            <input id="itemToBeEdited" type="hidden" name="id"/>
            <button type="submit" value="edit">Update</button>
        </form>
    </div>
</body>
</html>
