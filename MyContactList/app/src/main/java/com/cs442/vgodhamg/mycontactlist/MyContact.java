package com.cs442.vgodhamg.mycontactlist;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;

public class MyContact extends AppCompatActivity {

    ContentResolver cr1;
    Cursor cur;
    int gid=0;
    Cursor cursor,cursor1;
    Cursor cursor2;
    String id,name,phone;

    //arraylists for edit

    ArrayList<String> editId = new ArrayList<String>();
    ArrayList<String> editName = new ArrayList<String>();
    ArrayList<String> editPhn = new ArrayList<String>();
//end
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> list1 = new ArrayList<String>();
    ArrayList<ContentProviderOperation> ops =
            new ArrayList<ContentProviderOperation>();
    ArrayList<ContentProviderOperation> opsEdit =
            new ArrayList<ContentProviderOperation>();
    ArrayList<String> spinnerArray = new ArrayList<String>();
     ArrayAdapter<String> adap;
    ArrayAdapter<String> adap1;
    ContentResolver cr;
    ListView lv;
    String nameReceived,phoneReceived;
    Button btn_contact,btn_addContact,btn_group;
   // int rawContactID;
  //  String navn1="vedant.godhamgaonkar";
    String selectText;
    EditText edit_Name,edit_Mobile;
    ArrayList<String> contactData=new ArrayList<String>();
    ArrayList<String> selectedItem=new ArrayList<String>();
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private static final int RESULT_PICK_CONTACT=1;
    ArrayList<Item> groupMembers = new ArrayList<Item>();
    static final String SOME_VALUE = "int_value";
    static final String SOME_OTHER_VALUE = "string_value";
    int save;
    ArrayList<String> contactData1 = new ArrayList<String>();
    String savestring;
    String editedItemID;
    HashMap<Item,ArrayList<Item>> groupList = new HashMap<Item,ArrayList<Item>>();
   int rawContactID = ops.size();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact);
     //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);



        btn_contact = (Button) findViewById(R.id.button);
        btn_contact.getBackground().setColorFilter(0xFF40FFC9, PorterDuff.Mode.MULTIPLY);
        btn_group = (Button) findViewById(R.id.groupbtn);
        btn_group.getBackground().setColorFilter(0xFF40FFC9, PorterDuff.Mode.MULTIPLY);
        btn_addContact = (Button) findViewById(R.id.add_contact);
        btn_addContact.getBackground().setColorFilter(0xFF40FFC9, PorterDuff.Mode.MULTIPLY);
        edit_Name = (EditText) findViewById(R.id.edit_name);
        edit_Name.setTextColor(Color.parseColor("#ffffff"));
        edit_Name.setHintTextColor(Color.parseColor("#ffffff"));
        edit_Mobile = (EditText) findViewById(R.id.edit_phone);
        edit_Mobile.setTextColor(Color.parseColor("#ffffff"));
        edit_Mobile.setHintTextColor(Color.parseColor("#ffffff"));
        Spinner spinner = (Spinner) findViewById(R.id.spinner);


        //rawContactID = ops.size();

        String selection1 = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
        String[] selectionArgs1 = { "0", "1" };
         cursor2 = getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection1, selectionArgs1, null);
        cursor2.moveToFirst();
        int len1 = cursor2.getCount();
        for (int i = 0; i < len1; i++)
        {
           String title1 = cursor2.getString(cursor2.getColumnIndex(ContactsContract.Groups.TITLE));
          //  Log.d("Hiiiiiiiiiiiiii::::",title1);
            spinnerArray.add(title1);
            cursor2.moveToNext();
        }
        cursor2.close();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


     /*   for(int j=0 ; j<title1.length;j++) {

            spinnerArray.add(title1[j]);
        }*/
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.custometextview, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                selectText = parent.getItemAtPosition(pos).toString();
                // Log.d("Biiiiiiiiiiiiii::::", selectText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lv = (ListView) findViewById(R.id.listViewContact);
      //  lv.setBackgroundResource(R.drawable.customshape);





        cr1 = getContentResolver();
        cursor1 = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (cursor1.moveToNext()) {
            try {
                String contactId1 = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                String name1 = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                String hasPhone = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phones1 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId1, null, null);
                    while (phones1.moveToNext()) {
                        String phoneNumber1 = phones1.getString(phones1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        long idContact = phones1.getLong(phones1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                        contactData1.add(name1 + "\n" + phoneNumber1);
                        String myString = Long.toString(idContact);
                        editId.add(myString);
                        editName.add(name1);
                        editPhn.add(phoneNumber1);
                    }
                    phones1.close();
                }
            } catch (Exception e) {
            }
        }


        adap1 = new ArrayAdapter<String>(MyContact.this, R.layout.custometextview, contactData1);
        lv.setAdapter(adap1);




        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                ArrayList<String> contactData = new ArrayList<String>();
                cr = getContentResolver();
                cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                while (cursor.moveToNext()) {
                    try {
                        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (phones.moveToNext()) {
                                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                contactData.add(name + "\n" + phoneNumber);
                            }
                            phones.close();
                        }
                    } catch (Exception e) {
                    }
                }


                adap = new ArrayAdapter<String>(MyContact.this, R.layout.custometextview, contactData);
                lv.setAdapter(adap);

            }
        });


        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
                String[] selectionArgs = { "0", "1" };
                Cursor cursor = getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
                cursor.moveToFirst();
                int len = cursor.getCount();

                ArrayList<String> numbers = new ArrayList<String>();
                for (int i = 0; i < len; i++)
                {
                    String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
                   // Log.d("Hiiiiiiiiiiiiii::::",navn1);

                   // Log.d("Hiiiiiiiiiiiiii::::",selectText+"\t"+title);
                    if (title.equals(selectText))
                    {

                      //  Log.d("Hiiiiiiiiiiiiii::::",selectText+title);
                        String[] cProjection = { ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID };

                        Cursor groupCursor = getContentResolver().query(
                                ContactsContract.Data.CONTENT_URI,
                                cProjection,
                                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
                                        + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
                                        + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
                                new String[] { String.valueOf(id) }, null);
                        if (groupCursor != null && groupCursor.moveToFirst())
                        {
                            do
                            {

                                int nameCoumnIndex = groupCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                                String name = groupCursor.getString(nameCoumnIndex);

                                long contactId = groupCursor.getLong(groupCursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));

                                Cursor numberCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER }, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);

                                if (numberCursor.moveToFirst())
                                {
                                    int numberColumnIndex = numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                    do
                                    {
                                        String phoneNumber = numberCursor.getString(numberColumnIndex);
                                        numbers.add(name + "\n" + phoneNumber);
                                        //Log.d("Hiiiiiiiiiiiiii::::::::",name+phoneNumber);
                                    } while (numberCursor.moveToNext());
                                    numberCursor.close();
                                }
                            } while (groupCursor.moveToNext());
                            groupCursor.close();
                        }
                        break;
                    }

                    cursor.moveToNext();
                }
                cursor.close();
                // Log.d("Hiiiiii","numbers"+);

               // list1 = getAllNumbersFromGroupId(navn1);
                Intent i = new Intent(getApplicationContext(),GroupActivity.class);
                i.putStringArrayListExtra("Array", numbers);
                i.putExtra("selection",selectText);
                startActivity(i);

            }

        });


        btn_addContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                .build());

                        // Adding insert operation to operations list
                        // to insert display name in the table ContactsContract.Data
                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, edit_Name.getText().toString())
                                .build());

                        // Adding insert operation to operations list
                        // to insert Mobile Number in the table ContactsContract.Data
                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, edit_Mobile.getText().toString())
                                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                .build());

                        edit_Name.setText("");
                        edit_Mobile.setText("");

                        try {
                            // Executing all the insert operations as a single database transaction
                            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                            Toast.makeText(getBaseContext(), "Contact Is Successfully Added To Your Contact List, Please Refresh The Contact List", Toast.LENGTH_LONG).show();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }catch (OperationApplicationException e) {
                    e.printStackTrace();
                }

            }


        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                String selectedItem = contactData1.get(pos);
                editedItemID = editId.get(pos);
                String editedItemName = editName.get(pos);
                String editedItemPhn = editPhn.get(pos);

              //  Log.d("Hiiiiiiiiiiiiii::::::::", editedItemID);
                Toast.makeText(getBaseContext(), "Please Edit The Contact", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Intent.ACTION_EDIT);
                Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(editedItemID));
                i.setData(contactUri);


                //    Uri mUri = ContentUris.withAppendedId(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                //          Long.parseLong(editedItemID));
            /*    Intent intent = new Intent(Intent.ACTION_EDIT);
               // intent.setDataAndType(mUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                //intent.putExtra("finishActivityOnSaveCompleted", true);
                //add the below line
                intent.setData(Uri.parse(ContactsContract.Contacts.CONTENT_LOOKUP_URI + "/" + editedItemID));
             //  intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("finishActivityOnSaveCompleted", true);
                startActivityForResult(intent, 1); */

              /*  Intent iEdit = new Intent(getApplicationContext(), EditContact.class);
                iEdit.putExtra("ArrayEditName", editedItemName);
                iEdit.putExtra("ArrayEditPhone", editedItemPhn);
                Toast.makeText(getBaseContext(), editedItemName + editedItemPhn + " is selected from the list", Toast.LENGTH_SHORT).show();
                //   i.putExtra("selection",selectText); */
                startActivityForResult(i, 1);
                //   startActivity(i);

                return true;
            }
        });


       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                editedItemID = editId.get(position);
                String editedItemName1 = editName.get(position);
                String editedItemPhn1 = editPhn.get(position);

                Toast.makeText(getBaseContext(), "Please Remove Phone Number Only, For Deleting The Contact", Toast.LENGTH_LONG).show();
                Intent in = new Intent(Intent.ACTION_EDIT);
                Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(editedItemID));
                in.setData(contactUri);
           /*    // ArrayList<ContentProviderOperation> ops3 =
                       // new ArrayList<ContentProviderOperation>();
                ArrayList ops3 = new ArrayList();
                ops3.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                        .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=? and " + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(editedItemID), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE})
                        .build());
              //  getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);

        ArrayList ops1 = new ArrayList();
        int rawContactId1 = ops1.size();
        String selectPhone = ContactsContract.Contacts.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Contacts.Data.MIMETYPE + "='"  +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'" + " AND " + ContactsContract.CommonDataKinds.Phone.TYPE + "=? AND " + ContactsContract.CommonDataKinds.Phone.NUMBER + "=?";
        String[] phoneArgs = new String[] { String.valueOf(rawContactId1), editedItemPhn };
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(selectPhone, phoneArgs)
                .build());
        //resolver.applyBatch(ContactsContract.AUTHORITY, ops);

                try {
                    // Executing all the insert operations as a single database transaction
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops1);
                    Toast.makeText(getBaseContext(), "Contact is deleted added", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }catch (OperationApplicationException e) {
                    e.printStackTrace();
                } */

                startActivityForResult(in, 1);
                //   startActivity(i);

             //   return true;

              /*  Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(editedItemPhn));
                Cursor cur = getContentResolver().query(contactUri, null, null, null, null);
                try {
                    if (cur.moveToFirst()) {
                        do {
                            if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.NUMBER)).equalsIgnoreCase(editedItemPhn)) {
                                String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                                getContentResolver().delete(uri, null, null);
                               // return true;
                            }

                        } while (cur.moveToNext());
                    }

                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                } finally {
                    cur.close();
                }
*/

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save custom values into the bundle
        savedInstanceState.putInt(SOME_VALUE, save);
        savedInstanceState.putString(SOME_OTHER_VALUE, savestring);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        save = savedInstanceState.getInt(SOME_VALUE);
        savestring = savedInstanceState.getString(SOME_OTHER_VALUE);
    }

 /*   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {

                        nameReceived = data.getStringExtra("NameSentBack");
                phoneReceived = data.getStringExtra("PhoneSentBack");

               // c = Integer.parseInt(message);
              //  new_message = new_message + c;
              //  s = Integer.toString(new_message);


                opsEdit.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());

                // Adding insert operation to operations list
                // to insert display name in the table ContactsContract.Data
                opsEdit.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, Integer.parseInt(editedItemID))
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,nameReceived)
                        .build());

                // Adding insert operation to operations list
                // to insert Mobile Number in the table ContactsContract.Data
                opsEdit.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, Integer.parseInt(editedItemID))
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneReceived)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());

                //return_total.setText(s);

                try {
                    // Executing all the insert operations as a single database transaction
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, opsEdit);
                    Toast.makeText(getBaseContext(), "Contact is successfully edited", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }catch (OperationApplicationException e) {
                    e.printStackTrace();
                }
            }


        }

    } */

}

