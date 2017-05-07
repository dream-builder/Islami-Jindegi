package com.islamijindegi.islamijindegi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PostListActivity extends Fragment implements DownloadResponse,SearchResponse {

    List<PostsModel> postsModelsList;
    ListView postListView;
    FloatingActionButton fab;
    DatabaseHelper DB;
    Check check;
    Integer viewListPosition=null;
    AppPreference appPreference;
    AlertDialog.Builder builder ;
    String downloadPostID;
    Parcelable state =null; //LIST VIEW STATE WITH POSITION AND OTHER
    SocialCommunication socialCommunication;
    InnerSearch innerSearch;
    Spinner writerSpinner,typeSpinner,categorySpinner;
    Cursor cur=null;



    View mainView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_post_list,container,false);

        appPreference = new AppPreference(view.getContext());
        postListView =(ListView) view.findViewById(R.id.postListView);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        DB=new DatabaseHelper(getContext());
        check=new Check(getContext());
        socialCommunication=new SocialCommunication(getContext());

        //Spinner
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        writerSpinner = (Spinner) view.findViewById(R.id.writerSpinner);


        innerSearch=new InnerSearch(getContext());
        innerSearch.delegate=this;

        addPost();
        loadPostType();
        loadPostAuthor();
        //setTypeSpinner();

        if(appPreference.get_pref(appPreference.LOADCATEGORYTOFRAGMENT).equals("1"))
        {
            loadAllCat();
            appPreference.set_pref(appPreference.LOADCATEGORYTOFRAGMENT,"0");
        }

        fab.setVisibility(View.INVISIBLE);
        postListActions();

        this.mainView=view;
        return view;
    }


    private void postListActions() {

        postListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                fab.setVisibility(View.VISIBLE);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postListView.smoothScrollToPosition(0);
                        //innerSearch.LoadSearchView();
                    }
                });

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                state = postListView.onSaveInstanceState();
            }

        });

    }


    private void addPost(){

        DB.openDB();

        Cursor post = DB.getAllPostsByCategory(
                appPreference.get_pref(appPreference.POSTTYPE)
                ,appPreference.get_pref(appPreference.POSTCATEGORY),
                appPreference.get_pref(appPreference.POSTAUTHOR));

        //appPreference.set_pref(appPreference.POSTAUTHOR,"");

        postsModelsList = new ArrayList<>();
        //List<PostsModel> postsModelList = new ArrayList<>();

        for(post.moveToFirst();!post.isAfterLast();post.moveToNext()){
            //finalString.append());
            //finalString.append("\n");

            PostsModel postsModel = new PostsModel();

            //postsModel.setId(finalObject.getInt("post_id"));
            postsModel.setId(post.getInt(post.getColumnIndex(DB.ID)));
            postsModel.setPost_id(post.getString(post.getColumnIndex(DB.POSTID)));


            postsModel.setPost_title(post.getString(post.getColumnIndex(DB.POSTTITLE)));
            postsModel.setPost_author(post.getString(post.getColumnIndex(DB.POSTAUTHOR)));

            postsModel.setUlr(post.getString(post.getColumnIndex(DB.POSTURL)));
            postsModel.setPost_type(post.getString(post.getColumnIndex(DB.POSTTYPE)));
            postsModel.setCategory(post.getString(post.getColumnIndex(DB.POSTCATEGORY)));

            postsModel.setPost_meta_ext_file(post.getString(post.getColumnIndex(DB.POSTMETAEXTFILE)));
            postsModel.setPost_meta_upload_file(post.getString(post.getColumnIndex(DB.POSTMETAUPLOADFILE)));
            postsModel.setPost_download_file_url(post.getString(post.getColumnIndex(DB.POSTDOWNLOADFILEURL)));

            postsModel.setPost_new(post.getString(post.getColumnIndex(DB.POSTNEW)));



            postsModelsList.add(postsModel);

        }

        PostAdapter postAdapter = new PostAdapter(getContext(),R.layout.kitab_list_view,postsModelsList);
        postListView.setAdapter(postAdapter);


        if(state!=null)
            postListView.onRestoreInstanceState(state);

        DB.closeDB();
    }

    @Override
    public void downloadCompleted(String output) {
        DB.openDB();
        DB.update_download(downloadPostID,output);
        DB.closeDB();

        //REfresh POST LIST
        addPost();
        refreshPostList();

        Toast.makeText(getContext(), R.string.downlaod_completed, Toast.LENGTH_SHORT).show();

    }

    private void refreshPostList(){
        postListView.deferNotifyDataSetChanged();
    }

    @Override
    public void SearchResponseText(String searchText) {

        appPreference.set_pref(appPreference.POSTAUTHOR,searchText);
        addPost();
        //Toast.makeText(getContext(),searchText+"got",Toast.LENGTH_LONG).show();
    }

    public  class PostAdapter extends ArrayAdapter{

        private List<PostsModel> postsModelList;
        private int resources;
        private LayoutInflater inflater;
        private ImageButton browse,readPdfImageButton,playImageButton,downloadImageButton,extFileImageButton,socialShareImageButton;
        private ImageButton newBtn;
        private ImageButton readContent;
        private TextView postTitle, postCategory,postAuthor;
        private DownloadMaterial downloadMaterial;


        public PostAdapter(Context context, int resource, List<PostsModel> objects) {
            super(context, resource, objects);

            postsModelList=objects;
            this.resources=resource;

            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(convertView==null){

                convertView=inflater.inflate(resources,null);
            }

            //Initialization
            postTitle=(TextView) convertView.findViewById(R.id.TitleTextView);
            postAuthor=(TextView)convertView.findViewById(R.id.authorTextView);

            readContent=(ImageButton) convertView.findViewById(R.id.readContentImageButton);
            browse=(ImageButton) convertView.findViewById(R.id.browserImageButton);
            readPdfImageButton=(ImageButton) convertView.findViewById(R.id.ReadPdfImageButton);
            playImageButton=(ImageButton) convertView.findViewById(R.id.playImageButton);
            downloadImageButton=(ImageButton) convertView.findViewById(R.id.downloadImageButton);
            extFileImageButton=(ImageButton) convertView.findViewById(R.id.extFileImageButton);
            socialShareImageButton=(ImageButton) convertView.findViewById(R.id.shareImageButton);
            viewListPosition=position;
            downloadMaterial=new DownloadMaterial(getContext());
            newBtn =(ImageButton) convertView.findViewById(R.id.newItem);



            final String postDBid = String.valueOf(postsModelList.get(position).getId());
            final String extFile = postsModelList.get(position).getPost_meta_ext_file();
            final String url = postsModelList.get(position).getUlr().toString();
            final String offlineFile = postsModelList.get(position).getPost_download_file_url();
            final String uploadFile = postsModelList.get(position).getPost_meta_upload_file();
            final String postType = postsModelList.get(position).getPost_type();
            final String postCategory = postsModelList.get(position).getCategory();
            final String postID = postsModelList.get(position).getPost_id();
            final String postTitleText =postsModelList.get(position).getPost_title();
            final String postWriter =postsModelList.get(position).getPost_author();
            final String postNew =postsModelList.get(position).getPost_new();



            String downloadableFile=null;

            if(extFile==null || extFile.equals(""))
                downloadableFile=uploadFile;
            else
                downloadableFile=extFile;

            final String dnFile=downloadableFile;


            //Set data
            if(postNew.equals("1")) {
                postTitle.setTextColor(Color.rgb(255,110,31));
                newBtn.setVisibility(View.VISIBLE);
            }else {
                newBtn.setVisibility(View.GONE);
                postTitle.setTextColor(Color.rgb(76, 158, 76));
            }
            postTitle.setText(postTitleText /*+downloadableFile+postCategory + postID*/);
            postAuthor.setText(postsModelList.get(position).getPost_author()/*+offlineFile.length()+"|"+offlineFile*/);

            if(postWriter.equals(""))
                postAuthor.setVisibility(View.GONE);
            else
                postAuthor.setVisibility(View.VISIBLE);



            //Button hide and display
            playImageButton.setVisibility(convertView.GONE);
            readPdfImageButton.setVisibility(convertView.GONE);
            browse.setVisibility(convertView.GONE);
            downloadImageButton.setVisibility(convertView.GONE);
            extFileImageButton.setVisibility(convertView.GONE);

            //Google Drive file
            Boolean googleDriveFile = downloadableFile.indexOf("google")>0?true:false ;

            //Log.d("google",String.valueOf(googleDriveFile));

            if(offlineFile==null || offlineFile.equals(""))
            {
                if(downloadableFile.length()>0 ) {
                    if (!googleDriveFile)
                        downloadImageButton.setVisibility(convertView.VISIBLE);
                }
            }

            //ReadContent Button

            String pT=appPreference.get_pref(appPreference.POSTTYPE);

            if(pT.equals("articles") || pT.equals("specialamal") || pT.equals("0")){

                readContent.setVisibility(View.VISIBLE);

                readContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),WebViewBookArticle.class);

                        intent.putExtra("postTitle",postTitleText);
                        intent.putExtra("postAuthor",postsModelList.get(position).getPost_author());
                        intent.putExtra("postID",postID);
                        startActivity(intent);
                    }
                });


            }
            else
                readContent.setVisibility(View.GONE);


            //Download file
            downloadImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(check.checkInternet()) {
                        builder = new AlertDialog.Builder(getContext());

                        //builder.setMessage(dnFile);
                        builder.setMessage(R.string.download_msg);
                        builder.setCancelable(true);
                        builder.setPositiveButton(
                                R.string.download_btn,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        downloadProcess(postDBid, dnFile);
                                        dialog.cancel();
                                    }
                                });

                        builder.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                }
            });


            //External file open button
            if(googleDriveFile){
                extFileImageButton.setVisibility(convertView.VISIBLE);
            }

            //URL
            if(url.length()>0){
                browse.setVisibility(convertView.VISIBLE);
            }


            //Offline play button
            if(offlineFile.length()>0 && postType.equals("audio")){
                playImageButton.setVisibility(convertView.VISIBLE);
            }

            //Offline play button PDF
            if(offlineFile.length()>0){
                if((postType.equals("books")) || postType.equals("articles")) {
                    readPdfImageButton.setVisibility(convertView.VISIBLE);
                }
            }


            //Browser button click
            browse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(check.checkInternet()){
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        getContext().startActivity(i);
                    }
                }
            });

            //Read PDF file
            readPdfImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = new File(downloadMaterial.AppRoot().toString()+offlineFile);

                    if(file.exists()){
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                    else{
                        DB.openDB();
                        DB.update_download(postDBid,"");
                        DB.closeDB();

                        addPost();
                        postListView.deferNotifyDataSetChanged();

                        Toast.makeText(getContext(),"File not found. Please Download again.",Toast.LENGTH_LONG).show();
                    }
                }
            });

            //Play Audio
            playImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),AudioPlayer.class);
                    intent.putExtra("title",postsModelList.get(position).getPost_title());
                    intent.putExtra("author",postsModelList.get(position).getPost_author());
                    //intent.putExtra("category",postsModelList.get(position).ge());
                    intent.putExtra("url",offlineFile);
                    startActivity(intent);
                }
            });


            //External file Goole drive
            extFileImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(check.checkInternet()) {
                        String url = extFile;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
            });

            String p = appPreference.get_pref(appPreference.POSTTYPE);


            //Social sharing
            socialShareImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    socialCommunication.share(postsModelList.get(position).getPost_title(),url);
                }
            });

            return convertView;
        }




    }

    private void downloadProcess(String id,String string) {

        DownloadMaterial downloadMaterial = new DownloadMaterial(getContext());
        downloadPostID=id;
        downloadMaterial.delegate = this;
        downloadMaterial.execute(string);
    }


    private void loadPostAuthor(){
        DB.openDB();
        List<String> types = new ArrayList<String>();

        Cursor type = DB.getPostAuthor(appPreference.get_pref(appPreference.POSTTYPE),appPreference.get_pref(appPreference.POSTCATEGORY));

        types.add("লেখক/ বক্তা");
        for(type.moveToFirst();!type.isAfterLast();type.moveToNext()){

            String aut =type.getString(type.getColumnIndex(DB.POSTAUTHOR));
            if(!aut.equals(""))
                types.add(type.getString(type.getColumnIndex(DB.POSTAUTHOR)));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, types);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        // attaching data adapter to spinner
        writerSpinner.setAdapter(dataAdapter);

        DB.closeDB();


        writerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    String selectedWriter=parent.getItemAtPosition(position).toString();
                    appPreference.set_pref(appPreference.POSTAUTHOR,selectedWriter);
                }
                else{
                    appPreference.set_pref(appPreference.POSTAUTHOR,"");
                }

                addPost();
                refreshPostList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void loadAllCat(){

        DB.openDB();
        List<String> categories = new ArrayList<String>();

        if(appPreference.get_pref(appPreference.POSTTYPE).equals("books")){
            cur=DB.getCategories("কিতাব");
        }

        else if(appPreference.get_pref(appPreference.POSTTYPE).equals("audio")){
            cur=DB.getCategories("বয়ান");
        }

        else if(appPreference.get_pref(appPreference.POSTTYPE).equals("articles")){
            cur=DB.getCategories("প্রবন্ধ");
        }

        else if(appPreference.get_pref(appPreference.POSTTYPE).equals("audio")){
            cur=DB.getCategories("ইস্তেমা");
        }

        else if(appPreference.get_pref(appPreference.POSTTYPE).equals("specialamal")){
            cur=DB.getCategories("");
        }

        else if(appPreference.get_pref(appPreference.POSTTYPE).equals("0")){
            cur=DB.getCategories("English");
        }

        if(cur!=null){
            categories.add("বিষয় সমুহ");
            for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                // do what you need with the cursor here
                categories.add(cur.getString(cur.getColumnIndex(DB.CATEGORYCHILDNAME)));
            }
        }


        DB.closeDB();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        // attaching data adapter to spinner
        categorySpinner.setAdapter(dataAdapter);
        categorySpinner.setSelection(0);
    }



    private void loadPostType() {
        DB.openDB();

        List<String> categories = new ArrayList<String>();

        if(appPreference.get_pref(appPreference.POSTTYPE).equals("books") &&
                appPreference.get_pref(appPreference.POSTCATEGORY).equals("")){
                cur=DB.getCategories("কিতাব");
        }

        else if(appPreference.get_pref(appPreference.POSTTYPE).equals("audio") &&
                appPreference.get_pref(appPreference.POSTCATEGORY).equals("")){
            cur=DB.getCategories("বয়ান");
        }

        else if(appPreference.get_pref(appPreference.POSTTYPE).equals("articles") &&
                appPreference.get_pref(appPreference.POSTCATEGORY).equals("")){
            cur=DB.getCategories("প্রবন্ধ");
        }

        else if(appPreference.get_pref(appPreference.POSTTYPE).equals("audio") &&
                appPreference.get_pref(appPreference.POSTCATEGORY).equals("ইস্তেমা ")){
            cur=DB.getCategories("ইস্তেমা");
        }

        else if(appPreference.get_pref(appPreference.POSTTYPE).equals("specialamal") &&
                appPreference.get_pref(appPreference.POSTCATEGORY).equals("")){
            cur=DB.getCategories("");
        }

        else if(appPreference.get_pref(appPreference.POSTTYPE).equals("0") &&
                appPreference.get_pref(appPreference.POSTCATEGORY).equals("english")){
            cur=DB.getCategories("English");
        }

        else if(appPreference.get_pref(appPreference.POSTTYPE).equals("0") &&
                appPreference.get_pref(appPreference.POSTCATEGORY).equals("মেয়েদের")){
            cur=DB.getCategories("মেয়েদের");
        }

        if(cur!=null){
            categories.add("বিষয় সমুহ");
            for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                // do what you need with the cursor here
                categories.add(cur.getString(cur.getColumnIndex(DB.CATEGORYCHILDNAME)));
            }
        }

        DB.closeDB();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        // attaching data adapter to spinner
        categorySpinner.setAdapter(dataAdapter);
        categorySpinner.setSelection(0);

        //Previous Value
        final String previousCategory= appPreference.get_pref(appPreference.POSTCATEGORY);
        final String previousType= appPreference.get_pref(appPreference.POSTTYPE);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String cat = parent.getItemAtPosition(position).toString();

                if(position==0)
                {
                    appPreference.set_pref(appPreference.POSTTYPE,previousType);
                    appPreference.set_pref(appPreference.POSTCATEGORY,previousCategory);
                }
                else {


                    if(appPreference.get_pref(appPreference.FORGIRLS).equals("1")){

                        if(cat.equals("কিতাব")) {
                            appPreference.set_pref(appPreference.POSTTYPE, "books");
                            cat="মেয়েদের";
                            appPreference.set_pref(appPreference.POSTCATEGORY, cat);
                        }

                        if(cat.equals("প্রবন্ধ")) {
                            appPreference.set_pref(appPreference.POSTTYPE, "articles");
                            cat="মেয়েদের";
                            appPreference.set_pref(appPreference.POSTCATEGORY, cat);
                        }

                        if(cat.equals("বয়ান ")) {
                            appPreference.set_pref(appPreference.POSTTYPE, "audio");
                            cat="মেয়েদের";
                            appPreference.set_pref(appPreference.POSTCATEGORY, cat);
                        }

                        if(cat.equals("কুরআনের মশক")) {
                            appPreference.set_pref(appPreference.POSTTYPE, "audio");
                            cat= "কুরআনের মশক";
                            appPreference.set_pref(appPreference.POSTCATEGORY, cat);
                        }

                    }
                    else
                      appPreference.set_pref(appPreference.POSTCATEGORY, cat);

                }


                appPreference.set_pref(appPreference.POSTAUTHOR,"");

                loadPostAuthor();

                //Set writer to first
                writerSpinner.setSelection(0);

                addPost();
                refreshPostList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    //set selection
    private void setTypeSpinner(){

        int pos=0;


        String type = appPreference.get_pref(appPreference.POSTTYPE);
        String cat  =appPreference.get_pref(appPreference.POSTCATEGORY);


        if(type.equals("books") && cat.equals(""))
            pos=1;
        else if(type.equals("audio") && cat.equals(""))
            pos=2;

        else if(type.equals("books") && cat.equals("প্রবন্ধ"))
            pos=3;

        else if(type.equals("audio") && cat.equals("ইস্তেমা"))
            pos=4;

        categorySpinner.setSelection(pos);
    }

}
