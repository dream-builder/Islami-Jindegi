package com.islamijindegi.islamijindegi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Map;

/**
 * Created by Shahed on 2/22/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DBNAME="ijdb.db";
    private static final int VERSION=1;

    private static final String POSTTABLE ="posts";
    public static final String ID="_id";
    public static final String POSTID="post_id";
    public static final String POSTTITLE="post_title";
    public static final String POSTCATEGORY="post_category";
    public static final String POSTAUTHOR="post_author";
    public static final String POSTTYPE="post_type";
    public static final String POSTURL="post_url";

    public static final String POSTMETAEXTFILE="post_meta_ext_file";
    public static final String POSTMETAUPLOADFILE="post_meta_upload_file";
    public static final String POSTMETAVIDEOLINK="post_meta_video_link";

    public static final String POSTDOWNLOADFILEURL="post_download_file_url";
    public static final String POSTLASTUPDATE="post_last_update";
    public static final String POSTCONTENT="post_content";
    public static final String POSTNEW="post_new";



//CategoryList table

    public static  final  String CATEGORYLISTTABLE = "category_list";
    public static  final  String CATEGORYID = "_id";
    public static  final  String CATEGORYNAME = "cat_name";
    public static  final  String CATEGORYCHILDNAME = "cat_parent";


    private SQLiteDatabase ijDB;

    public Context context;

    public DatabaseHelper(Context context) {
        super(context, DBNAME,null, VERSION);

        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query ="CREATE TABLE IF NOT EXISTS "+ POSTTABLE +"(";
            query   +=  ID +" INTEGER PRIMARY KEY AUTOINCREMENT,";
            query   +=  POSTID+" TEXT NOT NULL UNIQUE,";
            query   +=  POSTTITLE+" TEXT NOT NULL,";
            query   +=  POSTAUTHOR+" TEXT NOT NULL,";
            query   +=  POSTCONTENT+" TEXT NOT NULL,";
            query   +=  POSTCATEGORY+" TEXT NOT NULL,";
            query   +=  POSTTYPE+" TEXT NOT NULL,";
            query   +=  POSTLASTUPDATE+" TEXT NOT NULL,";
            query   +=  POSTURL+" TEXT NOT NULL,";
            query   +=  POSTMETAEXTFILE+" TEXT NOT NULL,";
            query   +=  POSTMETAUPLOADFILE+" TEXT NOT NULL,";
            query   +=  POSTDOWNLOADFILEURL+" TEXT NOT NULL,"; //IN APP FILE LOCATION, DIRECT STORE IN DEVICE MEMORY
            query   +=  POSTMETAVIDEOLINK+" TEXT NOT NULL,";
            query   +=  POSTNEW + " INTEGER NOT NULL DEFAULT 0";

            query   +=  ")";

        //db.execSQL(query);

        db.execSQL(query);

        //Create category table
        query  =  "CREATE TABLE IF NOT EXISTS "+ CATEGORYLISTTABLE +"(";
        query   +=  CATEGORYID +" INTEGER PRIMARY KEY AUTOINCREMENT,";
        query   +=  CATEGORYNAME+" TEXT NOT NULL,";
        query   +=  CATEGORYCHILDNAME+" TEXT NOT NULL";
        query   +=  ")";

        db.execSQL(query);
        addCategoryItems(db);

    }


    public void createCategoryTable(){
        //Create category table
        String query  =  "CREATE TABLE IF NOT EXISTS "+ CATEGORYLISTTABLE +"(";
        query   +=  CATEGORYID +" INTEGER PRIMARY KEY AUTOINCREMENT,";
        query   +=  CATEGORYNAME+" TEXT NOT NULL,";
        query   +=  CATEGORYCHILDNAME+" TEXT NOT NULL";
        query   +=  ")";

        openDB();
        ijDB.execSQL(query);

        addCategoryItems(ijDB);

        closeDB();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public  void openDB(){
        ijDB = getWritableDatabase();
    }

    public void closeDB(){
        if(ijDB != null && ijDB.isOpen())
            ijDB.close();
    }


    //public long insertPost(String postID, String postTitle, String postCategory, String postUrl, String postType){
    public long insertPost(Map<String,String> post){

        ContentValues values = new ContentValues();

        StringBuffer stringBuffer =new StringBuffer();

        for(Map.Entry<String,String> newPost : post.entrySet()){

            values.put(newPost.getKey(),newPost.getValue());
            stringBuffer.append(newPost.getKey() + newPost.getValue() +"\n");
        }

        Toast.makeText(context,stringBuffer,Toast.LENGTH_SHORT).show();

        return ijDB.insert(POSTTABLE,null,values);
    }


    public Cursor getAllPosts(){

        String query ="SELECT * FROM "+ POSTTABLE;

        return ijDB.rawQuery(query,null);
    }

    public void deleteAllPost(){
        String query ="DELETE FROM "+ POSTTABLE;
        ijDB.execSQL(query);
    }


    public Boolean postExists(String id){

        String query ="SELECT "+POSTID+" FROM "+ POSTTABLE +" WHERE " + POSTID +"=" +id;

        Cursor cur= ijDB.rawQuery(query,null);

        if(cur.getCount()>0)
            return true;

        return false;
    }


    public Cursor getAllPostsByCategory(String type,String category,String author){

        String where = null;

        String query ="SELECT * FROM "+ POSTTABLE ;//

        if(type!=null && !type.equals("0")) {

            if(where==null) {
                where=" WHERE ";
                query += where + POSTTYPE + "=\"" + type + "\" ";
            }
            else
                query += POSTTYPE + "=\"" + type + "\" ";
        }

        if(category!=null && !category.equals("")){

            if(where==null) {
                where=" WHERE ";
                query += where + POSTCATEGORY + " LIKE \"%" + category + "%\"";
            }
            else
                query+= " AND "+ POSTCATEGORY +" LIKE \"%"+category+"%\"";
        }

        if(author!=null && !author.equals("") && !author.equals("0")){

            if(where==null) {
                where=" WHERE ";
                query += where + POSTAUTHOR + " LIKE '%" + author + "%'";
            }
            else
                query+=" AND "+ POSTAUTHOR +" LIKE '%"+author+"%'";
        }

        query +=" ORDER BY " + POSTNEW +" DESC, " + POSTLASTUPDATE +" DESC ";

        //Log.d("query",query);
        return ijDB.rawQuery(query,null);


    }


    public long insertPost1(String post_id,
                            String post_title,
                            String post_content,
                            String post_category,
                            String post_author,
                            String post_type,
                            String post_url,
                            String post_ext_file,
                            String post_upload_file,
                            String post_download_file,
                            String post_video_link,
                            String post_last_update,
                            String post_new){


        ContentValues values = new ContentValues();


        values.put(POSTID,post_id);
        values.put(POSTTITLE,post_title);
        values.put(POSTCONTENT,post_content);
        values.put(POSTCATEGORY,post_category);
        values.put(POSTAUTHOR,post_author);
        values.put(POSTTYPE,post_type);
        values.put(POSTURL,post_url);
        values.put(POSTMETAEXTFILE,post_ext_file);
        values.put(POSTMETAUPLOADFILE,post_upload_file);
        values.put(POSTDOWNLOADFILEURL,post_download_file);
        values.put(POSTMETAVIDEOLINK,post_video_link);
        values.put(POSTLASTUPDATE,post_last_update);
        values.put(POSTNEW,post_new);

        return ijDB.insert(POSTTABLE,null,values);

    }


    public void updatePost(String post_id,
                            String post_title,
                            String post_content,
                            String post_category,
                            String post_author,
                            String post_type,
                            String post_url,
                            String post_ext_file,
                            String post_upload_file,
                            String post_download_file,
                            String post_video_link,
                            String post_last_update,
                            String post_new){


        ContentValues values = new ContentValues();


        values.put(POSTID,post_id);
        values.put(POSTTITLE,post_title);
        values.put(POSTCONTENT,post_content);
        values.put(POSTCATEGORY,post_category);
        values.put(POSTAUTHOR,post_author);
        values.put(POSTTYPE,post_type);
        values.put(POSTURL,post_url);
        values.put(POSTMETAEXTFILE,post_ext_file);
        values.put(POSTMETAUPLOADFILE,post_upload_file);
        values.put(POSTDOWNLOADFILEURL,post_download_file);
        values.put(POSTMETAVIDEOLINK,post_video_link);
        values.put(POSTLASTUPDATE,post_last_update);
        values.put(POSTNEW,post_new);

        ijDB.update(POSTTABLE,values,POSTID +"="+post_id,null);

    }


    public void update_download(String post_id,String path){

        ContentValues contentValues = new ContentValues();
        contentValues.put(POSTDOWNLOADFILEURL,path);
        ijDB.update(POSTTABLE,contentValues, "_id="+post_id,null);

    }

    public Cursor getCategories(String parent){
        String query ="SELECT "+ CATEGORYCHILDNAME +" FROM " +CATEGORYLISTTABLE +" WHERE "+ CATEGORYNAME +" = \""+parent+"\"";
        return ijDB.rawQuery(query,null);

    }


    public Cursor getAllCategory(){

        String query ="SELECT " + POSTCATEGORY +","+ POSTTYPE+" FROM "+ POSTTABLE;

        return ijDB.rawQuery(query,null);
    }

    public Cursor getTypes(){

        String query ="SELECT DISTINCT " + POSTTYPE +" FROM "+ POSTTABLE;

        return ijDB.rawQuery(query,null);
    }

    public String getPostContent(int id){

        String content=null;
        Cursor cur = null;
        String query ="SELECT " + POSTCONTENT +" FROM "+ POSTTABLE +" WHERE " + POSTID + " = " + id;

        cur = ijDB.rawQuery(query,null);
        cur.getCount();
        if(cur != null)
        {
            cur.moveToFirst();
            content = cur.getString(cur.getColumnIndex(POSTCONTENT));
           // Log.d("PCONTENT",content);
        }


        return content;
    }


    public void updatePostContent(String id,String content){

        ContentValues contentValues = new ContentValues();
        contentValues.put(POSTCONTENT,content);
        ijDB.update(POSTTABLE,contentValues, POSTID + "="+id,null);
    }

    public void updatePostNewStatus(String id,String status){

        ContentValues contentValues = new ContentValues();
        contentValues.put(POSTNEW,status);
        ijDB.update(POSTTABLE,contentValues, POSTID + "=\""+id+"\"",null);
    }


    public Cursor getPostAuthor(String type, String category){

        String query ="SELECT DISTINCT " + POSTAUTHOR +" FROM "+ POSTTABLE + " WHERE " + POSTTYPE +" = '"+type+"'";

       if(category!=null){

            query +=" AND " + POSTCATEGORY +" LIKE '%"+ category+"%'";
        }

        //Log.d("Author query", query);

        return ijDB.rawQuery(query,null);
    }


    public void deleteCategory(){
        String query ="DELETE FROM "+ CATEGORYLISTTABLE;
        ijDB.execSQL(query);
    }

    public void insertToCategory(SQLiteDatabase db,String parent, String child){
        ContentValues values = new ContentValues();
        values.put(CATEGORYNAME,parent);
        values.put(CATEGORYCHILDNAME,child);
        db.insert(CATEGORYLISTTABLE,null,values);
    }

    public void createCategory(){
        addCategoryItems(ijDB);
    }

    public void addCategoryItems(SQLiteDatabase db){
        String cat[][]={
                {"English" ,"Article"},
                {"English","Biography"},
                {"English","Books"},
                {"কিতাব","ঈমান"},
                {"কিতাব","ইবাদাত"},
                {"কিতাব","মু‘আমালাত"},
                {"কিতাব","মু‘আশারাত"},
                {"কিতাব","আখলাক"},
                {"কিতাব","দা‘ওয়াত"},
                {"কিতাব","পরিপূর্ণ দীন"},
                {"কিতাব","বিদ‘আত"},
                {"কিতাব","জীবনী"},
                {"কিতাব","মালফুজাত"},
                {"কিতাব","ইতিহাস"},
                {"কিতাব","মাদ্রাসার কিতাব"},
                {"কিতাব","মাওলানা আশরাফ আলী থানভী রহ."},
                {"কিতাব","শাইখুল হাদীস যাকারিয়া রহ."},
                {"কিতাব","মুফতী শফী রহ."},
                {"কিতাব","মুফতী মনসূরুল হক দা.বা."},
                {"কিতাব","মুফতী তাকী উসমানী দা.বা."},
                {"কিতাব","অন্যান্য উলামাদের কিতাব"},
                {"কুরআনের মশক",""},
                {"প্রবন্ধ","আকাইদ"},
                {"প্রবন্ধ","সুন্নতী আমল"},
                {"প্রবন্ধ","বার মাসের করণীয় বর্জনীয়"},
                {"প্রবন্ধ","লেনদেন"},
                {"প্রবন্ধ","বান্দার হক"},
                {"প্রবন্ধ","আত্মশুদ্ধি"},
                {"প্রবন্ধ","দ্বীনি মেহনত"},
                {"প্রবন্ধ","মুখোশ উন্মোচন"},
                {"প্রবন্ধ","সংক্ষিপ্ত জীবনী"},
                {"প্রবন্ধ","অন্যান্য"},
                {"বয়ান","তাবলীগ"},
                {"বয়ান","তা‘লিম"},
                {"বয়ান","তাযকিয়া"},
                {"বয়ান","ফাতাওয়া"},
                {"বয়ান","মালফুযাত"},
                {"বয়ান","মুফতী মনসূরুল হক দা.বা."},
                {"বয়ান","জুম‘আ"},
                {"বয়ান","দা‘ওয়াতুল হক"},
                {"বয়ান","মাহফিল"},
                {"বয়ান","মাওলানা মাহমূদুল হাসান দা.বা.এর বয়ান"},
                {"বয়ান","অন্যান্য উলামাদের বয়ান"},
                {"বয়ান","হাজ্জ"},
                {"বয়ান","ইস্তেমা ও তাবলীগের বয়ান"},
                {"মাদ্রাসা",""},
                {"মেয়েদের","কুরআনের মশক"},
                {"মেয়েদের","কিতাব"},
                {"মেয়েদের","প্রবন্ধ"},
                {"মেয়েদের","বয়ান "},
                {"লা-মাযহাবী","কিতাব "},
                {"লা-মাযহাবী","প্রবন্ধ"},
                {"লা-মাযহাবী","বয়ান"},
                {"ইস্তেমা","ইস্তেমা ও তাবলীগের বয়ান"},
        };

        for(int i=0; i<cat.length;i++) {
            insertToCategory(db,cat[i][0].toString(),cat[i][1]);
        }
    }

    public void makePostOld(){
        String sql="UPDATE "+POSTTABLE+" SET "+POSTNEW+"=0 WHERE "+ POSTLASTUPDATE +" >= (SELECT DATETIME('now', '-3 day'))";

        Cursor c = ijDB.rawQuery(sql,null);

        c.getCount();
    }


}
