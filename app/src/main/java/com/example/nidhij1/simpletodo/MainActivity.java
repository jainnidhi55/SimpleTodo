package com.example.nidhij1.simpletodo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

        //keyboard goes away
        InputMethodManager imm = (InputMethodManager) getSystemService(etNewItem.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etNewItem.getApplicationWindowToken(), 0);

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
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //builder.setCancelable(true);
                builder.setTitle("Enter new task name");
                //builder.setMessage("Message");
                View li = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialogue, null);
                View dialogueView = (View) li.findViewById(R.id.dlg);
                builder.setView(dialogueView);
                final EditText dlgedit = (EditText) li.findViewById(R.id.editText);
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*items.remove(position);
                                itemsAdapter.notifyDataSetChanged();
                                String itemText = dlgedit.getText().toString();
                                //add the item to the list via the adapter
                                itemsAdapter.add(itemText);
                                //clear the EditText by setting it to an empty String
                                dlgedit.setText("");
                                writeItems();*/

                                //extra
                                // extract updated item value from result extras

                                String updatedItem = dlgedit.getText().toString();
                                Log.d("todo", "PRINT" + updatedItem);
                                // get the position of the item which was edited
                                // update the model with the new item text at the edited position
                                items.set(position, updatedItem);
                                // notify the adapter the model changed
                                itemsAdapter.notifyDataSetChanged();
                                // Store the updated items back to disk
                                writeItems();
                                // notify the user the operation completed OK

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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
