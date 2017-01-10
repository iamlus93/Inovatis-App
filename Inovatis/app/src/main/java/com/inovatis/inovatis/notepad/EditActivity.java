package com.inovatis.inovatis.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inovatis.inovatis.R;

import java.util.Date;

//
//Summary: This is the note editor activity. It allows the
//user to edit the title/body of the note and save or delete it.
//
public class EditActivity extends AppCompatActivity {

    DbHelper mDbHelper;
    NoteBook mNotebook;
    long mCurNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the UI layout for this Activity (defined in activity_editnote.xml)
        setContentView(R.layout.activity_editnote);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Edit Note");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(mToolbar);

        mDbHelper = DbHelper.getInstance(this);
        mNotebook = NoteBook.getInstance(this);

        mCurNoteId = Note.NEW_ID;
        initialize();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //the activity is stopping, save the note's current draft
        saveNote(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editnote, menu);
        return true;
    }

    private void initialize() {
        Intent myIntent = getIntent();
        mCurNoteId = myIntent.getLongExtra("id", Note.NEW_ID);
        String title = myIntent.getStringExtra("title");
        String body = myIntent.getStringExtra("text");

        setNoteTitle(title);
        setNoteBody(body);
    }

    private String getNoteTitle() {
        EditText titleText = (EditText) findViewById(R.id.titleText);
        return titleText.getText().toString();
    }

    private String getNoteBody() {
        EditText bodyText = (EditText) findViewById(R.id.bodyText);
        return bodyText.getText().toString();
    }

    private void setNoteTitle(String title){
        EditText titleText = (EditText)findViewById(R.id.titleText);
        titleText.setText(title, TextView.BufferType.EDITABLE);
    }

    private void setNoteBody(String body){
        EditText bodyText = (EditText)findViewById(R.id.bodyText);
        bodyText.setText(body, TextView.BufferType.EDITABLE);
    }

    //event handler for toolbar menu click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_save:
                saveNote(false);
                return true;

            case R.id.action_delete:
                deleteNote();
                return true;

            case R.id.action_about:
                Utils.ShowAboutDialog(this);
                return true;

            default:
                //invoke the superclass to handle unrecognized action
                return super.onOptionsItemSelected(item);
        }
    }

    //Deletes the current Note
    private void deleteNote() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setCancelable(false); // This blocks the 'BACK' button
        dlg.setTitle("Confirm Delete");
        dlg.setMessage("Are you sure you want to delete the note?");
        dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int buttonId) {
                new DeleteTask().execute(mCurNoteId);  //delete Note from database asynchronously
                mNotebook.deleteNote(mCurNoteId);
                setNoteTitle("");
                setNoteBody("");
                finish();
            }
        });
        dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int buttonId) {
                dialog.dismiss();
            }
        });
        dlg.create().show();
    }

    //Saves the current Note. If autoSave is true, then feedback is
    //hidden (this indicates this method was not called explicitly by user)
    private void saveNote(boolean autoSave)
    {
        //get inputs
        String title = getNoteTitle();
        String body = getNoteBody();

        //get current datetime
        Date date = new Date(System.currentTimeMillis());

        if (title.isEmpty() && body.isEmpty())
        {
            if (!autoSave) {
                Toast toast = Toast.makeText(this.getApplicationContext(), "Note is empty, nothing to save", Toast.LENGTH_SHORT);
                toast.show();
            }
            return;
        }

        if (title.isEmpty())
        {
            title = "Untitled Note";
        }

        //insert the note if it's new, otherwise update existing note
        if (mCurNoteId == Note.NEW_ID)
        {
            mCurNoteId = mDbHelper.insertNote(new Note(title, body, date));
            mNotebook.addNote(new Note(mCurNoteId, title, body, date));
        }
        else
        {
            Note n = new Note(mCurNoteId, title, body, date);
            mDbHelper.updateNote(n);
            mNotebook.updateNote(n);
        }

        if (!autoSave) {
            Toast toast = Toast.makeText(this.getApplicationContext(), "Note saved", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    //
    //Summary: inner class used to perform asynchronous Note deletion
    //
    private class DeleteTask extends AsyncTask<Long, Void, String> {

        protected String doInBackground(Long... params) {
            DbHelper db = DbHelper.getInstance(EditActivity.this.getApplicationContext());

            Long id = params[0];
            if (id != Note.NEW_ID) {
                int rowsDeleted = db.deleteNote(id);
                if (rowsDeleted > 0) {
                    return "Note deleted";
                }
                else {
                    return "Error deleting note";
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                Toast toast = Toast.makeText(EditActivity.this.getApplicationContext(), result, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
