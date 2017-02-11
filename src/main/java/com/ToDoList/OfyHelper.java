package com.ToDoList;
import com.ToDoList.Beans.ListItem;
import com.ToDoList.Beans.ToDoList;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/*
 * Created by awaeschoudhary on 2/10/17.
 */
public class OfyHelper implements ServletContextListener{
    public void contextInitialized(ServletContextEvent event) {
        // This will be invoked as part of a warmup request, or the first user request if no warmup
        // request.
        ObjectifyService.register(ToDoList.class);
        ObjectifyService.register(ListItem.class);
    }

    public void contextDestroyed(ServletContextEvent event) {
        // App Engine does not currently invoke this method.
    }
}
