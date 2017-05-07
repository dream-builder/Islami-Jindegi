package com.islamijindegi.islamijindegi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Shahed on 2/18/2017.
 */

public class PostCustomListAdapter extends BaseAdapter {

    String [] result;
    Context context;
    private static LayoutInflater inflater=null;

    public PostCustomListAdapter(Context kitabActivity, List<PostsModel> arrayList) {
        // TODO Auto-generated constructor stub
        //result=prgmNameList;
        //authName=authNameList;

        context = kitabActivity;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();

        View rowView;
        rowView = inflater.inflate(R.layout.kitab_list_view, null);

        holder.TitleTextView=(TextView) rowView.findViewById(R.id.TitleTextView);

        holder.AuthorTextView=(TextView) rowView.findViewById(R.id.authorTextView);
        //holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

        final String URI= "lksdflkfgklsdflk";
        ImageButton browserImageButton=(ImageButton)rowView.findViewById(R.id.browserImageButton);


        browserImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String url = "http://www.example.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(URI));
                context.startActivity(i);
            }
        });

        return rowView;
    }

    public class Holder
    {
        TextView TitleTextView,AuthorTextView,CategoryTextView;
        ImageButton browserImageButton;

    }
}
