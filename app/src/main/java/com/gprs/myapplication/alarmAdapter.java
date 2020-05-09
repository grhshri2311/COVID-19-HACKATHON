package com.gprs.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class alarmAdapter extends ArrayAdapter {


    ArrayList<String> name,desc,time;
    private Activity context;

    public alarmAdapter(ArrayList<String> name, ArrayList<String> desc, ArrayList<String> time, Activity context) {
        super(context,R.layout.alarmitem,name);
        this.name = name;
        this.desc = desc;
        this.time = time;
        this.context = context;
    }


    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=null;
         rowView = inflater.inflate(R.layout.alarmitem, null, true);
            //this code gets references to objects in the listview_row.xml file
            TextView name1 = (TextView) rowView.findViewById(R.id.name);
            TextView desc1= (TextView) rowView.findViewById(R.id.desc);
            TextView time1 = (TextView) rowView.findViewById(R.id.time);

            if(position<name.size() && position<desc.size() && position<time.size()) {
                name1.setText(name.get(position));
                desc1.setText(desc.get(position));
                time1.setText(time.get(position));
            }


        return rowView;

    };


}
