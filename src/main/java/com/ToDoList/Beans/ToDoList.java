package com.ToDoList.Beans;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by awaeschoudhary on 2/10/17.
 */
@Entity
public class ToDoList {
    //Name of the book serves as the id of the book
    @Id public String listName;
    private String author_email;
    private String author_id;

    public ToDoList() {}

    public ToDoList(String listName)
    {
        this.listName = listName;
    }

    public void setAuthor(String author_email,String author_id)
    {
        this.author_email=author_email;
        this.author_id=author_id;
    }


    public String getAuthorEmail()
    {
        return author_email;
    }

    public String getAuthorId()
    {
        return author_id;
    }

}
