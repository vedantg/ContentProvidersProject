package com.cs442.vgodhamg.mycontactlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class EditContact extends AppCompatActivity {

    ListView lvMain;
    ArrayList<String> editContactArray ;
    ArrayAdapter<String> contactAdap;
    String contactSentName,contactSentPhone;
    EditText editNameSent,editPhoneSent;
    Button btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editNameSent=(EditText)findViewById(R.id.editTextName);
        editPhoneSent=(EditText)findViewById(R.id.editTextPhone);
        btn_save = (Button)findViewById(R.id.button_save);
        editContactArray = new ArrayList<String >();

        Intent i = getIntent();

        contactSentName = i.getStringExtra("ArrayEditName");
        contactSentPhone = i.getStringExtra("ArrayEditPhone");

        editNameSent.setText(contactSentName);

        editPhoneSent.setText(contactSentPhone);
       // editContactArray.add(contactSent);
     /*   contactAdap = new ArrayAdapter<String>(this,R.layout.custometextview,editContactArray);
        lvMain = (ListView)findViewById(R.id.listViewEditContact);
        lvMain.setBackgroundResource(R.drawable.customshape);
        lvMain.setAdapter(contactAdap); */

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent();
                intent.putExtra("NameSentBack", editNameSent.getText().toString());
                intent.putExtra("PhoneSentBack", editPhoneSent.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();//finishing activity
            }
        });

    }

}
