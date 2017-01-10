package com.inovatis.inovatis.notepad;

/**
 * Created by Lud' on 21/10/2016.
 */
import java.util.Date;

public class Note implements Comparable<Note>
{
    //constant (id of a new/unsaved note)
    public static final long NEW_ID = -1;

    //Members
    private long id;
    private String title;
    private String text;
    private Date date;

    //Accessors
    public long getID()
    {
        return this.id;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getText()
    {
        return this.text;
    }

    public Date getDate() { return this.date; }

    public Long getLongDate() { return this.date.getTime(); }

    //Mutators
    public void setID(long id)
    {
        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setDate(Long ms) { this.date = new Date(ms); }

    //
    //constructors
    //
    public Note(){}

    public Note(String title, String text, Date date)
    {
        this.id = NEW_ID;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public Note(long id, String title, String text, Date date)
    {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    //Sorting is done by comparing the Notes Date values
    public int compareTo(Note other)
    {
        if (this.date.getTime() < other.date.getTime()) {
            return 1;
        }
        else if (this.date.getTime() > other.date.getTime()) {
            return -1;
        }
        else {
            return 0;
        }
    }

    //This is how the note will be displayed in the ListView
    public String toString()
    {
        return this.title;
    }
}
