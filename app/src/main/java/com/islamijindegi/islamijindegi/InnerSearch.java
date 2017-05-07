package com.islamijindegi.islamijindegi;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahed on 4/3/2017.
 */

public class InnerSearch {
    public SearchResponse delegate=null;

    Dialog dialog;
    Button okBtn,cancelBtn;
    Spinner categorySpinner,typeSpinner;
    Integer catPos=0;
    Context context;
    DatabaseHelper DB;
    String selectedCategory=null;
    AppPreference appPreferance;



    public InnerSearch(Context context) {
        this.context = context;
        DB= new DatabaseHelper(context);
        appPreferance=new AppPreference(context);
        
    }

    public void LoadSearchView(){

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.search);
        dialog.setTitle("Search");
        //dialog.create();

        categorySpinner = (Spinner) dialog.findViewById(R.id.catspinner);
        typeSpinner = (Spinner) dialog.findViewById(R.id.typeSpinner);

        okBtn = (Button) dialog.findViewById(R.id.okBtn);
        cancelBtn = (Button) dialog.findViewById(R.id.cancleBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catPos==0)
                    Toast.makeText(context,"Nothing selected",Toast.LENGTH_LONG).show();
                else{

                    //delegate = (SearchResponse) context;
                    delegate.SearchResponseText(selectedCategory);

                }
                dialog.hide();
            }
        });


         categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selectedCategory=parent.getItemAtPosition(position).toString();
                 catPos=position;
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {
                catPos=0;
             }
         });

        //loadCategoryToSpinner();
        //loadPostType();
        loadPostAuthor();
        dialog.show();


        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                postListView.smoothScrollToPosition(0);

            }
        });
*/
    }


    private void loadPostType(){
        DB.openDB();


        List<String> types = new ArrayList<String>();

        Cursor type = DB.getTypes();

        types.add("Select any category");
        for(type.moveToFirst();!type.isAfterLast();type.moveToNext()){
            types.add(type.getString(type.getColumnIndex(DB.POSTTYPE)));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, types);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        // attaching data adapter to spinner
        typeSpinner.setAdapter(dataAdapter);

    }

    private void loadPostAuthor(){
        DB.openDB();


        List<String> types = new ArrayList<String>();

        Cursor type = DB.getPostAuthor(appPreferance.get_pref(appPreferance.POSTTYPE),appPreferance.get_pref(appPreferance.POSTCATEGORY));

        types.add("Select any category");
        for(type.moveToFirst();!type.isAfterLast();type.moveToNext()){
            types.add(type.getString(type.getColumnIndex(DB.POSTAUTHOR)));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, types);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        // attaching data adapter to spinner
        categorySpinner.setAdapter(dataAdapter);

    }
    private void loadCategoryToSpinner() {

        List<String> categories = new ArrayList<String>();

        categories.add("Select any category");
        categories.add("ঈমান");
        categories.add("ইবাদাত");
        categories.add("মু‘আমালাত");
        categories.add("মু‘আশারাত");
        categories.add("আখলাক");
        categories.add("দা‘ওয়াত");
        categories.add("পরিপূর্ণ দীন");
        categories.add("বিদ‘আত");
        categories.add("জীবনী");
        categories.add("মালফুজাত");
        categories.add("ইতিহাস");
        categories.add("মাদ্রাসার কিতাব");
        categories.add("মাওলানা আশরাফ আলী থানভী রহ.");
        categories.add("শাইখুল হাদীস যাকারিয়া রহ.");
        categories.add("মুফতী শফী রহ.");
        categories.add("মুফতী মনসূরুল হক দা.বা.");
        categories.add("মুফতী তাকী উসমানী দা.বা.");
        categories.add("অন্যান্য উলামাদের কিতাব");
        categories.add("মেয়েদের জন্য");
        categories.add("লা-মাযহাবী");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        // attaching data adapter to spinner
        categorySpinner.setAdapter(dataAdapter);

    }
}
