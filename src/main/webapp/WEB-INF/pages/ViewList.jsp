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
            $("#itemsTable tr").click(function() {
                if ($(this).hasClass("disabled")) {
                    return;
                }

                $("#itemsTable tr").removeClass("selected");
                $(this).addClass("selected");

                //update the itemToBeDeleted field
                $("#itemToBeDeleted").attr("value", $(this).attr('id'));
            });


        });
    </script>
</head>

<body>
    <form method="post">
    <div id="file">
        <button class="fileButton" type="submit" title="Create New List">
            <img src="../images/New.png" />
        </button>
        <button class="fileButton" type="submit" title="Load Existing List">
            <img src="../images/Load.png" />
        </button>
        <button class="fileButton" type="submit" title="Save List">
            <img src="../images/Save.png" />
        </button>
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
                <form>
                    <td>Name of Todo List:</td>
                    <td>
                        <input type="text" name="listName" value="${listName}"/>
                    </td>

                    <td>Name of Owner:</td>
                    <td>
                        <input type="text" name="listOwner" />
                    </td>
                </form>
            </tr>
        </table>
    </div>

    <div id="items" class="section">
        <h3>Items</h3>

        <form method="post">
            <div id="itemTools">
                <button type="button" class="itemButton" onclick="showAddListItemDialog()" title="Add item">
                    <img src="../images/Add.png" />
                </button>
                <button class="itemButton" type="submit" title="Remove Item" onclick="form.action='/removeListItem'">
                    <img src="../images/Remove.png" />
                </button>

                <!-- list name parameter -->
                <input type="hidden" value="${listName}" name="listName"/>

                <!-- keeps track of selected list item id -->
                <input id="itemToBeDeleted" type="hidden" name="id"/>

                <button class="itemButton" type="submit" onclick="form.action='/moveItemUp';">
                    <img src="../images/MoveUp.png" />
                </button>
                <button class="itemButton" type="submit" onclick="form.action='/moveItemDown';">
                    <img src="../images/MoveDown.png" />
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
    <div id="newListItemDialog">
        New List Item
        <form action="/createListItem" method="post">
            <input type="text" name="category"/>
            <input type="text" name="description"/>
            <input type="date" name="startDate"/>
            <input type="date" name="endDate"/>
            <input type='hidden' value='false' name='completed'/>
            <input type='checkbox' value='true' name='completed'/>
            <input type="hidden" value="${listName}" name="listName"/>
            <input type="submit" value="Add"/>
        </form>
    </div>
</body>
</html>
