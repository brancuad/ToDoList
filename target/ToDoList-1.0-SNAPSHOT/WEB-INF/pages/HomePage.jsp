<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
        $(function() {
            $("#itemsTable tr").click(function() {
                if ($(this).hasClass("disabled")) {
                    return;
                }

                $("#itemsTable tr").removeClass("selected");
                $(this).addClass("selected");
            });
        });
    </script>
</head>

<body>

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
        <button class="fileButton" type="submit" title="Exit">
            <img src="../images/Exit.png" />
        </button>
    </div>

    <div class="divider"></div>

    <div class="title">To Do List</div>


    <div id="details" class="section">
        <h3>Details</h3>
        <table id="detailsTable">
            <tr>
                <form>
                    <td>Name of Todo List:</td>
                    <td>
                        <input type="text" name="listName" />
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

        <div id="itemTools">
            <button class="itemButton" type="submit" title="Add item">
                <img src="../images/Add.png" />
            </button>
            <button class="itemButton" type="submit" title="Remove Item">
                <img src="../images/Remove.png" />
            </button>
            <button class="itemButton" type="submit" title="Save List">
                <img src="../images/MoveUp.png" />
            </button>
            <button class="itemButton" type="submit" title="Exit">
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
                <%-- sample --%>
                <tr>
                    <td>Schoolwork</td>
                    <td>CSE 308 Project</td>
                    <td>2017-02-10</td>
                    <td>2017-02-10</td>
                    <td>false</td>
                </tr>
                <tr>
                    <td>Chores</td>
                    <td>Do the Dishes</td>
                    <td>2017-02-11</td>
                    <td>2017-02-11</td>
                    <td>true</td>
                </tr>
            </tbody>
        </table>
    </div>

</body>
</html>

