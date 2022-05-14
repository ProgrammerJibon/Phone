package io.jibon.phone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactListAdapter extends BaseAdapter{
    Activity activity;
    ArrayList<ContactModel> arrayList;
    public ContactListAdapter(Activity activity, ArrayList<ContactModel> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Long.parseLong(arrayList.get(i).getId());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ContactListViewHolder contactListViewHolder;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.lv_contact_list_item, viewGroup, false);
            contactListViewHolder = new ContactListViewHolder();
            contactListViewHolder.LV_contact_image = view.findViewById(R.id.LV_contact_image);
            contactListViewHolder.LV_contact_name = view.findViewById(R.id.LV_contact_name);
            contactListViewHolder.LV_phone_numbers = view.findViewById(R.id.LV_phone_numbers);
            view.setTag(contactListViewHolder);
        }else{
            contactListViewHolder = (ContactListViewHolder) view.getTag();
        }

        try {
            if (arrayList.get(i).getImage() != null) {
                ImageView LV_contact_image = contactListViewHolder.LV_contact_image;
                LV_contact_image.setImageURI(Uri.parse(arrayList.get(i).getImage()));
            }else{
                ImageView LV_contact_image = contactListViewHolder.LV_contact_image;
                LV_contact_image.setImageResource(R.drawable.ic_baseline_account_circle_24);
            }

            TextView LV_contact_name = contactListViewHolder.LV_contact_name;
            LV_contact_name.setText(arrayList.get(i).getName());
            ListView LV_phone_numbers = contactListViewHolder.LV_phone_numbers;
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, arrayList.get(i).getNumbers());
            LV_phone_numbers.setAdapter(arrayAdapter);
            LV_phone_numbers.setOnItemClickListener((adapterView, view1, i1, l) -> {
                try {
                    String phone = arrayList.get(i).getNumbers().get(i1);
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phone));
                    activity.startActivity(intent);
                }catch (Exception e){
                    Log.e("errnos", e.toString());
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Log.e("errnos", arrayList.get(i).getId()+"\n\t"+arrayList.get(i).getUri());
                }
            });

        }catch (Exception e){
            Log.e("errnos", e.toString());
        }

        return  view;
    }
    public class ContactListViewHolder{
    ImageView LV_contact_image = null;
    TextView LV_contact_name = null;
    ListView LV_phone_numbers = null;
    }
}
