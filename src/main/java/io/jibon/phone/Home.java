package io.jibon.phone;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    ArrayList<ContactModel> contactsArray;
    ListView LV_contact_list;
    Activity activity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        LV_contact_list = findViewById(R.id.LV_contact_list);

        contactsArray = new ArrayList<ContactModel>();
        activity = this;

        getPhoneNumbers();

    }
    private void getPhoneNumbers() {
        try {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null,  ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            if (phones.getCount() > 0){
                ArrayList<String> numbers;
                phones.moveToFirst();

                String lastNumber = "";

                while (phones.moveToNext())
                {

                    String id = phones.getString(phones.getColumnIndex(ContactsContract.Data._ID));
                    String name= phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
                    String displayPhotoUri = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));

                    if(lastNumber.contains(" "+((phoneNumber.replaceAll("\\s", "")).replaceAll("-", "")))){
                        continue;
                    }else{
                        lastNumber += " " + ((phoneNumber.replaceAll("\\s", "")).replaceAll("-", ""));
                    }

                    numbers = new ArrayList<>();
                    numbers.add(phoneNumber);

                    ContactModel contactModel = new ContactModel();
                    contactModel.setImage(displayPhotoUri);
                    contactModel.setName(name);
                    contactModel.setNumbers(numbers);
                    contactModel.setId(id);
                    contactModel.setUri(contactUri);

                    contactsArray.add(contactModel);
                }
                ContactListAdapter contactListAdapter = new ContactListAdapter(activity, contactsArray);
                LV_contact_list.setAdapter(contactListAdapter);
            }

            phones.close();
        }catch (Exception e){
            Log.e("errnos", e.toString());
        }
    }
    @Override
    public void onBackPressed() {
        //back to android home without closing app
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
    }
}