package com.inovatis.inovatis.notepad;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

import com.inovatis.inovatis.ProfileFragment;
import com.inovatis.inovatis.R;

public class NotepadActivity extends AppCompatActivity {

    DbHelper mDbHelper;
    NoteBook mNotebook;
    Button bt_profilehome;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Set the UI layout for this Activity (defined in activity_main.xml)
        setContentView(R.layout.activity_notepad);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Inovatis Mémos");
        setSupportActionBar(mToolbar);

        mNotebook = NoteBook.getInstance(this);
        mDbHelper = DbHelper.getInstance(this);



        loadNotes();
        initializeListView();


    }



    @Override
    protected void onStart()
    {
        super.onStart();


    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //loads all Notes from database asynchronously
    public void loadNotes()
    {
        FetchTask asyncFetch = new FetchTask();
        asyncFetch.execute();
    }

    private void initializeListView()
    {
        //bind the Notebook adapter to the ListView
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(mNotebook.noteAdapter);




        //define the listener for row click
        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Note curNote = mNotebook.noteAdapter.getItem(position);

                Intent intent = new Intent(NotepadActivity.this, EditActivity.class);
                intent.putExtra("id", curNote.getID());
                intent.putExtra("title", curNote.getTitle());
                intent.putExtra("text", curNote.getText());
                startActivity(intent);
            }
        };






        listView.setOnItemClickListener(clickListener);
    }

    //event handler for click on action button
    public void addNoteButton_OnClick(View view)
    {

        //launch a new Activity with a note editing dialog
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id", Note.NEW_ID);
        intent.putExtra("title", "");
        intent.putExtra("text", "");
        startActivity(intent);
    }



    //event handler for toolbar menu click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_about:
                Utils.ShowAboutDialog(this);
                return true;

            default:
                //invoke the superclass to handle unrecognized action
                return super.onOptionsItemSelected(item);
        }
    }

    //
    //Summary: inner class used to perform asynchronous retrieval of Notes
    //
    private class FetchTask extends AsyncTask<Void, Void, ArrayList<Note>> {

        protected ArrayList<Note> doInBackground(Void... params) {
            DbHelper db = DbHelper.getInstance(NotepadActivity.this.getApplicationContext());
            return db.getAllNotes();
        }

        protected void onPostExecute(ArrayList<Note> notes) {
            mNotebook.setNotes(notes);
            mNotebook.noteAdapter.notifyDataSetChanged();

            String msg = "No notes";
            if (notes.size() == 1){
                msg = String.format("1 note loaded");
            }
            else if (notes.size() > 1) {
                msg = String.format("%d notes loaded", notes.size());
            }
            Toast toast = Toast.makeText(NotepadActivity.this.getApplicationContext(), msg, Toast.LENGTH_LONG);
            toast.show();
        }
    }



}


