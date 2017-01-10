package com.inovatis.inovatis.notepad;

/**
 * Created by Lud' on 21/10/2016.
 */

import android.content.Context;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.Collections;

public class NoteBook {

    private Context context;
    private ArrayList<Note> mNotes;
    public ArrayAdapter<Note> noteAdapter;

    private static NoteBook mInstance = null;

    //global point of access
    public static synchronized NoteBook getInstance(Context context)
    {
        if (mInstance == null){
            mInstance = new NoteBook(context);
        }
        return mInstance;
    }

    //constructor
    private NoteBook(Context context) {
        this.context = context;
        mNotes = new ArrayList<Note>();
        noteAdapter = new ArrayAdapter<Note>(context, android.R.layout.simple_list_item_1, mNotes);
    }

    public void setNotes(ArrayList<Note> notes)
    {
        mNotes.clear();
        mNotes.addAll(notes);
        Collections.sort(mNotes);
        noteAdapter.notifyDataSetChanged();
    }

    public void addNote(Note note)
    {
        mNotes.add(0, note);
        Collections.sort(mNotes);
        noteAdapter.notifyDataSetChanged();
    }

    public void updateNote(Note note)
    {
        for (int i=0; i<mNotes.size(); i++)
        {
            if (mNotes.get(i).getID() == note.getID()) {
                mNotes.set(i, note);
                Collections.sort(mNotes);
                noteAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    public void deleteNote(long id)
    {
        for (int i=0; i<mNotes.size(); i++)
        {
            if (mNotes.get(i).getID() == id) {
                mNotes.remove(i);
                Collections.sort(mNotes);
                noteAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

}
