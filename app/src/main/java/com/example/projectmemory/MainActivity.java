package com.example.projectmemory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
/**
 * Set up the main page and lists of the app
 *
 * <p>
 *     Controls list creation, and list classification.
 *     It loads all the {@link com.example.projectmemory.ListContainer}, and let's the user display them.
 *     Save lists containers onStop using {@link MainActivity#saveLists()} and SharedPreferences.
 *     Load list containers onCreate using {@link MainActivity#loadLists()}.
 * </p>
 * */
public class MainActivity extends AppCompatActivity {
    ListContainer CommonLists; //Holds the common lists
    public static final String JSON_DATA = "JSON_DATA";
    public static final String APP_PREFS = "APPLICATION_PREFERENCES";
    /**
     * Create the main page and load the lists
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadLists();
    }
    /**
     * Save the lists using SharedPreferences
     */
    @Override
    protected void onStop() {
        // call the superclass method first
        super.onStop();

        //Update and save all current lists in SharedPreferences
        saveLists();
    }
    /**
     * When the user press the button, create a new common list
     *
     * <p>
     *     Takes the user input from the EditText in {@link com.example.projectmemory.R.layout#activity_main}
     *     and use it as a name to crate a new {@link com.example.projectmemory.List} and add it to
     *     {@link com.example.projectmemory.MainActivity#CommonLists}
     * </p>
     *
     * @param view
     * @return {@link com.example.projectmemory.List}
     * */
    public void createCommonList(View view){
        //Get list name
        EditText list = (EditText) findViewById(R.id.CommonListName);
        String listName = list.getText().toString();
        CommonLists.createList(listName);
        Toast.makeText(this, "List added to your Common lists", Toast.LENGTH_SHORT).show();

    }
    /**
     * Save all the {@link com.example.projectmemory.ListContainer}
     *
     * <p>
     *     Serialize all the {@link com.example.projectmemory.ListContainer} objects into JSON
     *     objects and then use shared preferences to save them all.
     * </p>*/
    public void saveLists(){
        //Use SharedPreferences to store all the lists
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Serialize and save CommonLists
        Gson gson = new Gson();
        String json = gson.toJson(CommonLists);
        editor.putString(JSON_DATA, json);
        editor.apply();
    }

    public void loadLists(){
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        //Load data from shared preferences
        String listContainers = sharedPreferences.getString(JSON_DATA, null);

        //Deserialize
        Gson gson = new Gson();
        ListContainer savedCommonLists = gson.fromJson(listContainers, ListContainer.class);

        //Check for null and update
        if (savedCommonLists == null){
            CommonLists = new ListContainer();
        }
        else{
            this.CommonLists = savedCommonLists;
        }
    }
}