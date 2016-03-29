package com.cs442.vgodhamg.mycontactlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class GroupActivity extends AppCompatActivity {
ListView lv ;
    ArrayList<String> ar;
    ArrayAdapter<String> adap;
    TextView tv;
   // HashMap<Item,ArrayList<Item>> ar;
    Intent iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        tv = (TextView)findViewById(R.id.textView);

        ar = new  ArrayList<String>();

     //   Intent iv = new Intent();
        iv = getIntent();
        String product = iv.getStringExtra("selection");
        tv.setText(product);

        //ArrayList<HashMap<Item,ArrayList<Item>>> arl = (ArrayList<HashMap<Item,ArrayList<Item>>>) getIntent().getSerializableExtra("arraylist");
        ar =  iv.getStringArrayListExtra("Array");
      //  tv.setText((CharSequence) arl);
        adap = new ArrayAdapter<String>(this,R.layout.custometextview,ar);
       lv = (ListView)findViewById(R.id.listView);
        lv.setBackgroundResource(R.drawable.customshape);
        lv.setAdapter(adap);

    }

}
