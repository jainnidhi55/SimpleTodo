package com.example.nidhij1.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items = new ArrayList<>();
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //EditText editTextAdd = (EditText)  findViewById(R.id.etNewItem);
        //editTextAdd.setInputType(InputType.TYPE_NULL);
        //reference to the list view created w the layout
        lvItems = (ListView) findViewById(R.id.lvItems);
        //initialize items list
        readItems();
        //initialize the adapter using the items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        //wire the adapter to the view
        lvItems.setAdapter(itemsAdapter);

        //items.add("First todo item");
        //items.add("Second todo item");

        //setup listener on creation
        setupListViewListener();
    }

    public void onAddItem(View v){
        //reference to EditText created with the layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        //grab EditText's content as a String
        String itemText = etNewItem.getText().toString();
        //add the item to the list via the adapter
        itemsAdapter.add(itemText);
        //clear the EditText by setting it to an empty String
        etNewItem.setText("");
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                //remove items in list at the index given by position
                items.remove(position);
                //notify the adapter that the underlying dataset has changed
                itemsAdapter.notifyDataSetChanged();
                //log info
                Log.i("MainActivity", "Removed item " + position);
                writeItems(); //writes
                //return true to tell framework that the long click was consumed
                return true;
            }
        });
    }

    //returns the file in which the data is stored
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    //read the items from the file sytem
    private void readItems() {
        try {
            //create the array using the content in the file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));

        } catch (IOException e) {
            //print error to console
            e.printStackTrace();
            //load an empty list
            items = new ArrayList<>();
        }
    }

    //write the items to the file system
    private void writeItems() {
        try {
            //save item list as a line-delimited text file
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            //print the error to the console
            e.printStackTrace();
        }
    }
}
