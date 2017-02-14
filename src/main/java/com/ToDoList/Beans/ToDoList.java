package com.ToDoList.Beans;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by awaeschoudhary on 2/10/17.
 */
@Entity
public class ToDoList {
    //Name of the book serves as the id of the book
    @Id public Long id;
    public String listName;
    private String ownerName;
    private String author_email;
    //public or private
    public boolean priv;
    @Index private String author_id;

    public ToDoList() {}

    public ToDoList(String listName)
    {
        this.listName = listName;
    }

    public void setAuthor(String author_email,String author_id,boolean priv)
    {
        this.author_email=author_email;
        this.author_id=author_id;
        this.priv = priv;
    }


    public String getAuthorEmail()
    {
        return author_email;
    }

    public String getAuthorId()
    {
        return author_id;
    }

    public String getOwnerName(){
        return ownerName;
    }
    public void setOwnerName(String ownerName){
        this.ownerName = ownerName;
    }

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id= id;
    }

    public String getListName(){
        return listName;
    }
    public void setListName(String listName){
        this.listName= listName;
    }

    public void setStatus(boolean status)
    {
        this.priv=status;
    }

    public boolean getStatus()
    {
        return priv;
    }


}
