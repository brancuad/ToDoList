package com.ToDoList.Beans;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

/**
 * Created by awaeschoudhary on 2/10/17.
 */
@Entity
public class ListItem {
    /*Indcates that the parent of this list item*/
    @Parent
    Key<ToDoList> listKey;
    @Id
    public Long id;

    public String author_email;
    public String author_id;
    @Index
    public int ord_ind; //Used to sort list items.
    public String category;
    public String description;
    public String startDate;
    public String endDate;
    public boolean completed;


    //we need a no arg constructor for some reason. Can't use this class without it.
    public ListItem(){}

    /**
     * A convenience constructor
     **/
    public ListItem(Long key) {
        if( key != null ) {
            listKey = Key.create(ToDoList.class, key);  // Creating the Ancestor key
        } else {
            listKey = Key.create(ToDoList.class, 0); //TODO: handle null key better
        }
    }

    /**
     * Takes all important fields
     **/
    public ListItem(Long key, String category, String id, String email, int index, String description, String startDate, String endDate,
                        boolean completed) {
        this(key);
        author_email = email;
        author_id = id;
        ord_ind=index;
        this.category = category;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completed = completed;
    }

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id= id;
    }

    public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category = category;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public String getStartDate(){
        return startDate;
    }
    public void setStartDate(String startDate){
        this.startDate = startDate;
    }

    public String getEndDate(){
        return endDate;
    }
    public void setEndDate(String endDate){
        this.endDate = endDate;
    }

    public Boolean getCompleted(){
        return completed;
    }
    public void setCompleted(boolean completed){
        this.completed = completed;
    }
}

