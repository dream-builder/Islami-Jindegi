package com.islamijindegi.islamijindegi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,FragmentResponse{


    Fragment fragment;
    FragmentManager fragmentManager;
    ImageView logoImageView;
    AppPermisssion permission;
    DrawerLayout drawer;
    AppPreference appPreference;

    AlertDialog.Builder builder;
    Check check;

    Toolbar toolbar;

    private static Integer frg=0;

    private void init(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        appPreference = new AppPreference(MainActivity.this);
        builder = new AlertDialog.Builder(this);
        check= new Check(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        init();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Reset Preferance
        appPreference.set_pref(appPreference.POSTAUTHOR,"");
        appPreference.set_pref(appPreference.POSTTYPE,"");
        appPreference.set_pref(appPreference.POSTCATEGORY,"");


        first_run();
        //Create APP folder if not exists
        check.ExternalStorage();
        checkPermission();
        nav();
        landingPage();
        registerToken();


        DatabaseHelper db;
        db = new DatabaseHelper(this);
        db.openDB();
        db.makePostOld();
        db.closeDB();

    }


    private void registerToken() {
        FirebaseMessaging.getInstance().subscribeToTopic("IslamiJindegi");
        FirebaseInstanceId.getInstance().getToken();
    }


    private void first_run() {


        //appPreference.set_pref(appPreference.APPFIRSTRUN,"0");

        if(appPreference.get_pref(appPreference.APPFIRSTRUN).equals("0")){

            appPreference.set_pref(appPreference.FCMOLDTOKEN,"0");

            builder.setTitle(R.string.sync_title);
            builder.setMessage(R.string.sync_msg);
            builder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    if(check.checkInternet()) {

                        appPreference.set_pref(appPreference.WEBSYNCSTART,"1");
                        appPreference.set_pref(appPreference.LOOP,"0");
                        appPreference.set_pref(appPreference.SYNCPAGE,"0");

                        WebService webservice = new WebService(MainActivity.this);
                        //webservice.execute("http://www.islamijindegi.com/webservice/?action=sync");
                        webservice.execute("http://islamijindegi.com/webservice/?action=sync");

                    }
                    dialog.dismiss();
                   // new JsonTask().execute();

                }
            });

            builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();


        }
    }

    private void checkPermission() {
        permission = new AppPermisssion(MainActivity.this);

        permission.checkExternalStoragePermission();
        permission.checkInternetPermission();
        permission.checkNetworkStatePermission();
        permission.checkVibratePermission();
        permission.checkWakeLockPermission();
        permission.checkNetworkStatePermission();
    }


    private void nav(){

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);

        logoImageView = (ImageView) headerview.findViewById(R.id.LogoImageView);

        logoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landingPage();

                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }


    private void landingPage(){

        setTitleBarText("Islami Jindegi");

        //Landing Fragment
        fragment = new LandingActivity();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main,fragment).commit();

        frg=0;
        showHideActionBar();
    }

    private void showHideActionBar(){

        if(frg==0){
            getSupportActionBar().hide();
        }
        else
            getSupportActionBar().show();
    }

    private void setTitleBarText(String string){
        getSupportActionBar().setTitle(string);
    }

    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(frg==0) {

                new AlertDialog.Builder(this)
                        .setMessage(R.string.app_close)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.no_button, null)
                        .show();
            }
            else{

                landingPage();
            }
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        //UNCOMMENT THIS LINE BELLOW TO ACTIVE MENU
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent =  new Intent(getApplicationContext(),SyncActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //int id = item.getItemId();
        int id = item.getItemId();

        if(id == R.id.nav_1 || id == R.id.nav_2 || id == R.id.nav_3 || id == R.id.nav_4 || id == R.id.nav_5 || id == R.id.nav_6 ) {
            appPreference.set_pref(appPreference.POSTTYPE, "books");
            appPreference.set_pref(appPreference.POSTCATEGORY,item.getTitle().toString());
            setTitleBarText("কিতাব");
        }
        else if(id == R.id.nav_7 || id == R.id.nav_8 || id == R.id.nav_9 || id == R.id.nav_10 || id == R.id.nav_11 ){
            appPreference.set_pref(appPreference.POSTTYPE, "audio");
            appPreference.set_pref(appPreference.POSTCATEGORY,item.getTitle().toString());
            setTitleBarText("বয়ান");
        }
        else if(id == R.id.nav_12 || id == R.id.nav_13 || id == R.id.nav_14 || id == R.id.nav_15 || id == R.id.nav_16 || id == R.id.nav_17|| id == R.id.nav_18|| id == R.id.nav_19|| id == R.id.nav_21){
            appPreference.set_pref(appPreference.POSTTYPE, "articles");
            appPreference.set_pref(appPreference.POSTCATEGORY,item.getTitle().toString());
            setTitleBarText("প্রবন্ধ");
        }

        else if(id == R.id.nav_20){
            appPreference.set_pref(appPreference.POSTTYPE, "books");
            appPreference.set_pref(appPreference.POSTCATEGORY,item.getTitle().toString());
            setTitleBarText("কিতাব");
        }

        else if(id == R.id.nav_22){
            appPreference.set_pref(appPreference.POSTTYPE, "audio");
            appPreference.set_pref(appPreference.POSTCATEGORY,"ইস্তেমা");
            setTitleBarText("ইস্তেমার বয়ান");
        }

        else if(id == R.id.nav_26){
            appPreference.set_pref(appPreference.POSTTYPE, "books");
            appPreference.set_pref(appPreference.POSTCATEGORY,"Book");
            setTitleBarText("English Book");
        }

        else if(id == R.id.nav_28){
            appPreference.set_pref(appPreference.POSTTYPE, "books");
            appPreference.set_pref(appPreference.POSTCATEGORY,"Article");
            setTitleBarText("Article");
        }

        else if(id == R.id.nav_27){
            appPreference.set_pref(appPreference.POSTTYPE, "books");
            appPreference.set_pref(appPreference.POSTCATEGORY,"Biography");
            setTitleBarText("Biography");
        }

        else if(id==R.id.nav_31){
            Intent i = new Intent(this,ActivitySetting.class);
            startActivity(i);
        }

        if(id==R.id.nav_30){

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://imshahed.me"));
            startActivity(intent);
        }

        if(id==R.id.nav_32){

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.islamijindegi.islamijindegi.islamijindegi&hl=en"));
            startActivity(intent);
        }


        if(id==R.id.nav_29){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT,"Share IslamiJindegi");
            intent.putExtra(Intent.EXTRA_TEXT,"www.islamijindegi.com");

            startActivity(Intent.createChooser(intent,"Share Islami Jindegi"));
        }

        if(id!=R.id.nav_31 && id!=R.id.nav_30 && id!=R.id.nav_29 && id!=R.id.nav_32 ) {
            appPreference.set_pref(appPreference.POSTCATEGORY, item.getTitle().toString());
            //setTitleBarText(appPreference.get_pref(appPreference.POSTCATEGORY));

            appPreference.set_pref(appPreference.LOADCATEGORYTOFRAGMENT, "1");
            loadFragment();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadFragment(){

        //fragment =  new LandingActivity();
        fragment = new PostListActivity();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main,fragment).commit();

        frg=1;

        showHideActionBar();

    }


    private void loadSalatTimingFragment(){

        //fragment =  new LandingActivity();
        fragment = new SalatTiming();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main,fragment).commit();

        frg=1;

    }


    @Override
    public void onFragmentLoadCompleted(String fragment) {

        appPreference.set_pref(appPreference.POSTCATEGORY,"");

        switch (Integer.parseInt(fragment)){

            case 0:
                appPreference.set_pref(appPreference.POSTTYPE, "books");
                appPreference.set_pref(appPreference.FORGIRLS,"0");
                setTitleBarText("কিতাব");
                loadFragment();
                break;

            case 1:
                appPreference.set_pref(appPreference.POSTTYPE, "audio");
                appPreference.set_pref(appPreference.FORGIRLS,"0");
                setTitleBarText("বয়ান");
                loadFragment();
                break;

            case 2:
                appPreference.set_pref(appPreference.POSTTYPE, "articles");
                appPreference.set_pref(appPreference.POSTCATEGORY,"");
                appPreference.set_pref(appPreference.FORGIRLS,"0");
                setTitleBarText("প্রবন্ধ");
                loadFragment();
                break;

            case 3:
                appPreference.set_pref(appPreference.POSTTYPE, "audio");
                appPreference.set_pref(appPreference.POSTCATEGORY,"ইস্তেমা ");
                appPreference.set_pref(appPreference.FORGIRLS,"0");
                setTitleBarText("ইস্তেমার বয়ান");
                loadFragment();

                break;

            case 4:
                //OFFLINE BOOKS
                //appPreference.set_pref(appPreference.POSTTYPE, "specialamal");
                //appPreference.set_pref(appPreference.POSTCATEGORY,"অফলাইন বই");
                appPreference.set_pref(appPreference.FORGIRLS,"0");
                loadOfflineBooksFragment();

                break;

            case 5:
                //Special AMAL
                appPreference.set_pref(appPreference.POSTTYPE, "specialamal");
                appPreference.set_pref(appPreference.POSTCATEGORY,"");
                appPreference.set_pref(appPreference.FORGIRLS,"0");
                setTitleBarText("বিশেষ আমল");
                loadFragment();
                break;

            case 6:
                //For women
                appPreference.set_pref(appPreference.POSTTYPE, "0");
                appPreference.set_pref(appPreference.POSTCATEGORY,"মেয়েদের");
                appPreference.set_pref(appPreference.FORGIRLS,"1");
                setTitleBarText("মেয়েদের জন্য");
                loadFragment();
                break;

            case 7:
                //English books
                appPreference.set_pref(appPreference.POSTTYPE, "0");
                appPreference.set_pref(appPreference.POSTCATEGORY,"english");
                appPreference.set_pref(appPreference.FORGIRLS,"0");
                setTitleBarText("English");

                loadFragment();
                break;

            case 8:
                //Salat timing
                appPreference.set_pref(appPreference.FORGIRLS,"0");
                loadSalatTimingFragment();
                break;


        }

    }

    private void loadOfflineBooksFragment() {
        //fragment =  new LandingActivity();
        fragment = new Offile_books_fragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main,fragment).commit();

        getSupportActionBar().setTitle("অফলাইন কিতাব");
        frg=1;

        showHideActionBar();
    }


}
