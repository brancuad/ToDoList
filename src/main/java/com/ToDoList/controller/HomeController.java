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
import java.util.Collections;
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

            //load lists for the user
            List<ToDoList> lists = ObjectifyService.ofy()
                    .load()
                    .type(ToDoList.class)
                    .filter("author_id", user.getUserId()) //only get lists for the logged in user.
                    .list();

            ModelAndView model = new ModelAndView("WEB-INF/pages/Lists");
            model.addObject("lists", lists);
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

        //Save the list
        ObjectifyService.ofy().save().entity(list).now();

        return home();
    }

    @RequestMapping("/deleteList")
    public ModelAndView deleteList(@RequestParam("id") Long listId){
        Key<ToDoList> theList = Key.create(ToDoList.class, listId);
        ObjectifyService.ofy().delete().key(theList);
        return home();
    }

    @RequestMapping("/editList")
    public ModelAndView editList(@RequestParam("id") Long listId, @RequestParam("name") String listName, @RequestParam("ownerName") String ownerName){
        //create key for list with specified list ID
        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

        List<ListItem> listItems = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .order("ord_ind")       // sort based on order index
                .limit(25)             // not sure if we need to limit the results. But ill leave this here for now.
                .list();

        ModelAndView model = new ModelAndView("WEB-INF/pages/ViewList");
        model.addObject("listItems", listItems);
        model.addObject("listName", listName);
        model.addObject("listId", listId);
        model.addObject("ownerName", ownerName);

        return model;

    }


    @RequestMapping("/saveListInfo")
    public ModelAndView saveListInfo(@RequestParam("listId") Long id, @RequestParam("name") String name, @RequestParam("ownerName") String ownerName){
        //get ToDoList using the id
        ToDoList list = ObjectifyService.ofy()
                .load()
                .type(ToDoList.class)
                .id(id).now();

        //update list info
        list.setOwnerName(ownerName);
        list.listName = name;

        //save
        ObjectifyService.ofy().save().entity(list).now();

        return editList(id, name, ownerName);
    }

    @RequestMapping("/createListItem")
    public ModelAndView createListItem(@RequestParam("listId") Long listId, @RequestParam("listName") String listName, @RequestParam("ownerName") String ownerName, @RequestParam("category") String category, @RequestParam("description") String description,
                                       @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("completed") boolean completed){
        ListItem item;

        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();  // Find out who the user is.


        com.googlecode.objectify.Key<ToDoList> listKey = com.googlecode.objectify.Key.create(ToDoList.class, listId);
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
            item = new ListItem(listId, category, user.getUserId(), user.getEmail(), index, description, startDate, endDate, completed);
        } else {
            item = new ListItem(listId, category, "999", "anon", index, description, startDate, endDate, completed);
        }

        // Use Objectify to save the greeting and now() is used to make the call synchronously as we
        // will immediately get a new page using redirect and we want the data to be present.
        //Save the greeting and reload the /guestbook.jsp page so that is is automatically updated int he webpage
        ObjectifyService.ofy().save().entity(item).now();

        //To Delete

        //ObjectifyService.ofy().delete().key(thingKey1).now();  // synchronous
        return editList(listId, listName, ownerName);

    }

    @RequestMapping("/removeListItem")
    public ModelAndView removeListItem(@RequestParam("listId") Long listId, @RequestParam("listName") String listName, @RequestParam("id") String itemId, @RequestParam("ownerName") String ownerName){

        long itemIdLong = Long.parseLong(itemId);

        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);
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

        return editList(listId, listName, ownerName);
    }

    @RequestMapping("/moveItemUp")
    public ModelAndView moveItemUp(@RequestParam("listId") Long listId, @RequestParam("listName") String listName, @RequestParam("id") String itemId, @RequestParam("ownerName") String ownerName){
        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

        //get all list items for the list being viewed
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
        return editList(listId, listName, ownerName);

    }

    @RequestMapping("/moveItemDown")
    public ModelAndView moveItemDown(@RequestParam("listId") Long listId, @RequestParam("listName") String listName, @RequestParam("id") String itemId, @RequestParam("ownerName") String ownerName){
        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

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
        return editList(listId, listName, ownerName);

    }

    @RequestMapping("/editListItem")
    public ModelAndView editListItem(@RequestParam("listId") Long listId, @RequestParam("listName") String listName, @RequestParam("id") Long itemId, @RequestParam("ownerName") String ownerName,
                                     @RequestParam("category") String category, @RequestParam("description") String description, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("completed") boolean completed){

        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

        //retrieve list item from datastore
        ListItem item = ObjectifyService.ofy().load().type(ListItem.class).parent(listKey).id(itemId).now();

        //set new values
        item.setCategory(category);
        item.setDescription(description);
        item.setStartDate(startDate);
        item.setEndDate(endDate);
        item.setCompleted(completed);

        //save
        ObjectifyService.ofy().save().entity(item).now();

        return editList(listId, listName, ownerName);
    }

    @RequestMapping("/sortCatUp")
    public ModelAndView sortCatUp(@RequestParam("listId") Long listId, @RequestParam("listName") String listName,
                                  @RequestParam("ownerName") String ownerName) {

        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        if (items.size() == 0) {
            return editList(listId, listName, ownerName);
        }

        ArrayList<String> cats = new ArrayList<String>();
        // Populate list with category names
        for (ListItem listItem : items) {
            cats.add(listItem.getCategory());
        }

        Collections.sort(cats);

        for (int i = 0; i < items.size(); i++) {
            for (ListItem listItem : items) {
                if (cats.get(i).equals(listItem.category)) {
                    listItem.ord_ind = i;
                }
            }
        }

        ObjectifyService.ofy().save().entities(items).now();

        return editList(listId, listName, ownerName);
    }

    @RequestMapping("/sortCatDown")
    public ModelAndView sortCatDown(@RequestParam("listId") Long listId, @RequestParam("listName") String listName,
                                    @RequestParam("ownerName") String ownerName) {

        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        if (items.size() == 0) {
            return editList(listId, listName, ownerName);
        }

        ArrayList<String> cats = new ArrayList<String>();
        // Populate list with category names
        for (ListItem listItem : items) {
            cats.add(listItem.getCategory());
        }

        Collections.sort(cats);

        for (int i = 0; i < items.size(); i++) {
            for (ListItem listItem : items) {
                if (cats.get(i).equals(listItem.category)) {
                    listItem.ord_ind = items.size() - 1 - i;
                }
            }
        }

        ObjectifyService.ofy().save().entities(items).now();

        return editList(listId, listName, ownerName);
    }

    @RequestMapping("/sortDesUp")
    public ModelAndView sortDesUp(@RequestParam("listId") Long listId, @RequestParam("listName") String listName,
                                  @RequestParam("ownerName") String ownerName) {

        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        if (items.size() == 0) {
            return editList(listId, listName, ownerName);
        }

        ArrayList<String> desc = new ArrayList<String>();
        // Populate list with category names
        for (ListItem listItem : items) {
            desc.add(listItem.getDescription());
        }

        Collections.sort(desc);

        for (int i = 0; i < items.size(); i++) {
            for (ListItem listItem : items) {
                if (desc.get(i).equals(listItem.getDescription())) {
                    listItem.ord_ind = i;
                }
            }
        }

        ObjectifyService.ofy().save().entities(items).now();

        return editList(listId, listName, ownerName);
    }

    @RequestMapping("/sortDesDown")
    public ModelAndView sortDesDown(@RequestParam("listId") Long listId, @RequestParam("listName") String listName,
                                    @RequestParam("ownerName") String ownerName) {

        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        if (items.size() == 0) {
            return editList(listId, listName, ownerName);
        }

        ArrayList<String> desc = new ArrayList<String>();
        // Populate list with category names
        for (ListItem listItem : items) {
            desc.add(listItem.getDescription());
        }

        Collections.sort(desc);

        for (int i = 0; i < items.size(); i++) {
            for (ListItem listItem : items) {
                if (desc.get(i).equals(listItem.getDescription())) {
                    listItem.ord_ind = items.size() - 1 - i;
                }
            }
        }

        ObjectifyService.ofy().save().entities(items).now();

        return editList(listId, listName, ownerName);
    }

    @RequestMapping("/sortSDateUp")
    public ModelAndView sortSDateUp(@RequestParam("listId") Long listId, @RequestParam("listName") String listName,
                                    @RequestParam("ownerName") String ownerName) {

        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        if (items.size() == 0) {
            return editList(listId, listName, ownerName);
        }

        ArrayList<String> desc = new ArrayList<String>();
        // Populate list with category names
        for (ListItem listItem : items) {
            desc.add(listItem.getStartDate());
        }

        Collections.sort(desc);

        for (int i = 0; i < items.size(); i++) {
            for (ListItem listItem : items) {
                if (desc.get(i).equals(listItem.startDate)) {
                    listItem.ord_ind = i;
                }
            }
        }

        ObjectifyService.ofy().save().entities(items).now();

        return editList(listId, listName, ownerName);
    }

    @RequestMapping("/sortSDateDown")
    public ModelAndView sortSDateDown(@RequestParam("listId") Long listId, @RequestParam("listName") String listName,
                                      @RequestParam("ownerName") String ownerName) {

        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        if (items.size() == 0) {
            return editList(listId, listName, ownerName);
        }

        ArrayList<String> desc = new ArrayList<String>();
        // Populate list with category names
        for (ListItem listItem : items) {
            desc.add(listItem.getStartDate());
        }

        Collections.sort(desc);

        for (int i = 0; i < items.size(); i++) {
            for (ListItem listItem : items) {
                if (desc.get(i).equals(listItem.startDate)) {
                    listItem.ord_ind = items.size() - 1 - i;
                }
            }
        }

        ObjectifyService.ofy().save().entities(items).now();

        return editList(listId, listName, ownerName);
    }

    @RequestMapping("/sortEDateUp")
    public ModelAndView sortEDateUp(@RequestParam("listId") Long listId, @RequestParam("listName") String listName,
                                    @RequestParam("ownerName") String ownerName) {

        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        if (items.size() == 0) {
            return editList(listId, listName, ownerName);
        }

        ArrayList<String> desc = new ArrayList<String>();
        // Populate list with category names
        for (ListItem listItem : items) {
            desc.add(listItem.getEndDate());
        }

        Collections.sort(desc);

        for (int i = 0; i < items.size(); i++) {
            for (ListItem listItem : items) {
                if (desc.get(i).equals(listItem.endDate)) {
                    listItem.ord_ind = i;
                }
            }
        }

        ObjectifyService.ofy().save().entities(items).now();

        return editList(listId, listName, ownerName);
    }

    @RequestMapping("/sortEDateDown")
    public ModelAndView sortEDateDown(@RequestParam("listId") Long listId, @RequestParam("listName") String listName,
                                      @RequestParam("ownerName") String ownerName) {

        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        if (items.size() == 0) {
            return editList(listId, listName, ownerName);
        }

        ArrayList<String> desc = new ArrayList<String>();
        // Populate list with category names
        for (ListItem listItem : items) {
            desc.add(listItem.getEndDate());
        }

        Collections.sort(desc);

        for (int i = 0; i < items.size(); i++) {
            for (ListItem listItem : items) {
                if (desc.get(i).equals(listItem.endDate)) {
                    listItem.ord_ind = items.size() - 1 - i;
                }
            }
        }

        ObjectifyService.ofy().save().entities(items).now();

        return editList(listId, listName, ownerName);
    }

    @RequestMapping("/sortCompUp")
    public ModelAndView sortCompUp(@RequestParam("listId") Long listId, @RequestParam("listName") String listName,
                                   @RequestParam("ownerName") String ownerName) {

        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        if (items.size() == 0) {
            return editList(listId, listName, ownerName);
        }

        ArrayList<Boolean> completed = new ArrayList<Boolean>();
        // Populate list with category names
        for (ListItem listItem : items) {
            completed.add(listItem.getCompleted());
        }

        Collections.sort(completed);

        for (int i = 0; i < items.size(); i++) {
            for (ListItem listItem : items) {
                if (completed.get(i) == listItem.getCompleted()) {
                    listItem.ord_ind = i;
                }
            }
        }

        ObjectifyService.ofy().save().entities(items).now();

        return editList(listId, listName, ownerName);
    }

    @RequestMapping("/sortCompDown")
    public ModelAndView sortCompDown(@RequestParam("listId") Long listId, @RequestParam("listName") String listName,
                                     @RequestParam("ownerName") String ownerName) {

        Key<ToDoList> listKey = Key.create(ToDoList.class, listId);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        List<ListItem> items = ObjectifyService.ofy()
                .load()
                .type(ListItem.class)
                .ancestor(listKey)
                .list();

        if (items.size() == 0) {
            return editList(listId, listName, ownerName);
        }

        ArrayList<Boolean> completed = new ArrayList<Boolean>();
        // Populate list with category names
        for (ListItem listItem : items) {
            completed.add(listItem.getCompleted());
        }

        Collections.sort(completed);

        for (int i = 0; i < items.size(); i++) {
            for (ListItem listItem : items) {
                if (completed.get(i) == listItem.getCompleted()) {
                    listItem.ord_ind = items.size() - 1 - i;
                }
            }
        }

        ObjectifyService.ofy().save().entities(items).now();

        return editList(listId, listName, ownerName);
    }
}
