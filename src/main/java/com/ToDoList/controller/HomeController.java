package com.ToDoList.controller;

import com.ToDoList.Beans.ListItem;
import com.ToDoList.Beans.ToDoList;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by awaeschoudhary on 2/9/17.
 */
@Controller
public class HomeController {


    //default method controller
    @RequestMapping("/")
    public ModelAndView home() {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        //if a user is not signed in, redirect to sign in page
        if (user == null) {
            ModelAndView model = new ModelAndView("redirect:" + userService.createLoginURL("/"));
            return model;
        }

        //display lists for logged in user
        else{
            List<String> names = new ArrayList<String>();

            //load lists for the user
            List<ToDoList> lists = ObjectifyService.ofy()
                    .load()
                    .type(ToDoList.class) // We want only Greetings
                    .list();

            //add names for lists to return list
            for (ToDoList list : lists) {
                if (list.getAuthorEmail() != null && list.getAuthorEmail().equals(user.getEmail())) {
                    names.add(list.listName);
                }
            }

            ModelAndView model = new ModelAndView("WEB-INF/pages/Lists");
            model.addObject("lists", names);
            model.addObject("username", user.getEmail());

            return model;
        }
    }

    @RequestMapping("/signout")
    public ModelAndView signout (){
        UserService userService = UserServiceFactory.getUserService();
        ModelAndView model = new ModelAndView("redirect:" + userService.createLogoutURL("/"));
        return model;
    }

    @RequestMapping("/createList")
    public ModelAndView createList(@RequestParam("listName") String name){
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();  // Find out who the user is.

        ToDoList list = new ToDoList(name);
        list.setAuthor(user.getEmail(),user.getUserId());

        //Save the guestbook object
        ObjectifyService.ofy().save().entity(list).now();


        return home();
    }

    @RequestMapping("/deleteList")
    public ModelAndView deleteList(@RequestParam("name") String name){
        Key<ToDoList> theList = Key.create(ToDoList.class, name);
        ObjectifyService.ofy().delete().key(theList);
        return home();
    }

    @RequestMapping("/editList")
    public ModelAndView editList(@RequestParam("name") String name){
        ///Fills a list called greeting with all the greetings from that query
        // Create the correct Ancestor key name is figured out from before , so create the guestbook with the name given
        Key<ToDoList> listKey = Key.create(ToDoList.class, name);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> listItems = ObjectifyService.ofy()
                .load()
                .type(ListItem.class) // We want only Greetings
                .ancestor(listKey)    // Anyone in this book
                .order("ord_ind")       // sort based on order index
                .limit(25)             // Only show 5 of them.
                .list();

        ModelAndView model = new ModelAndView("WEB-INF/pages/ViewList");
        model.addObject("listItems", listItems);
        model.addObject("listName", name);

        return model;

    }

    @RequestMapping("/createListItem")
    public ModelAndView createListItem(@RequestParam("listName") String listName, @RequestParam("category") String category, @RequestParam("description") String description,
                                       @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("completed") boolean completed){
        ListItem item;

        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();  // Find out who the user is.


        com.googlecode.objectify.Key<ToDoList> listKey = com.googlecode.objectify.Key.create(ToDoList.class, listName);
        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        int index = items.size();

        // CHANGE Greeting TO Task!! Add category, description, start, end, completed
        if (user != null) {
            item = new ListItem(listName, category, user.getUserId(), user.getEmail(), index, description, startDate, endDate, completed);
        } else {
            item = new ListItem(listName, category, "999", "anon", index, description, startDate, endDate, completed);
        }

        // Use Objectify to save the greeting and now() is used to make the call synchronously as we
        // will immediately get a new page using redirect and we want the data to be present.
        //Save the greeting and reload the /guestbook.jsp page so that is is automatically updated int he webpage
        ObjectifyService.ofy().save().entity(item).now();

        //To Delete

        //ObjectifyService.ofy().delete().key(thingKey1).now();  // synchronous
        return editList(listName);

    }

    @RequestMapping("/removeListItem")
    public ModelAndView removeListItem(@RequestParam("listName") String listName, @RequestParam("id") String itemId){

        long itemIdLong = Long.parseLong(itemId);

        Key<ToDoList> listKey = Key.create(ToDoList.class, listName);
        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        //Save order number so we can update the rest easier
        ListItem itemToBeRemoved = ObjectifyService.ofy().load().type(ListItem.class).parent(listKey).id(itemIdLong).now();
        int removeOrderNumber = itemToBeRemoved.ord_ind;

        // Delete from datastore using unique id
        ObjectifyService.ofy().delete().type(ListItem.class).parent(listKey).id(itemIdLong).now();



        // decrement ord_ind for every entry with ord greater than deleted one
        for (ListItem item : items) {
            if (item.ord_ind > removeOrderNumber){
                item.ord_ind--;
            }
        }

        ObjectifyService.ofy().save().entities(items).now();

        return editList(listName);

    }

    @RequestMapping("/moveItemUp")
    public ModelAndView moveItemUp(@RequestParam("listName") String listName, @RequestParam("id") String itemId){
        Key<ToDoList> listKey = Key.create(ToDoList.class, listName);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        long itemIdLong = Long.parseLong(itemId);

        //Save order number of the selected entry
        ListItem item = ObjectifyService.ofy().load().type(ListItem.class).parent(listKey).id(itemIdLong).now();
        int orderNumber = item.ord_ind;

        // first item on the list can't be moved up
        if (orderNumber > 0) {
            item.ord_ind--;

            // increment order number of the item on top of the selected item
            for (ListItem listItem : items) {
                if (listItem.ord_ind == orderNumber - 1 && listItem.id != itemIdLong) {
                    listItem.ord_ind++;
                }
            }

            ObjectifyService.ofy().save().entities(items).now();


        }
        return editList(listName);

    }

    @RequestMapping("/moveItemDown")
    public ModelAndView moveItemDown(@RequestParam("listName") String listName, @RequestParam("id") String itemId){
        Key<ToDoList> listKey = Key.create(ToDoList.class, listName);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        long itemIdLong = Long.parseLong(itemId);

        //Save order number of the selected entry
        ListItem item = ObjectifyService.ofy().load().type(ListItem.class).parent(listKey).id(itemIdLong).now();
        int orderNumber = item.ord_ind;

        // can't move last item on the list down
        if (orderNumber < items.size()-1) {
            item.ord_ind++;

            // increment order number of the item on top of the selected item
            for (ListItem listItem : items) {
                if (listItem.ord_ind == orderNumber + 1 && listItem.id != itemIdLong) {
                    listItem.ord_ind--;
                }
            }

            ObjectifyService.ofy().save().entities(items).now();


        }
        return editList(listName);

    }
}
