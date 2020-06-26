package com.alice.alicephonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alice.alicephonebook.adapter.ContactListAdapter;
import com.alice.alicephonebook.adapter.DBAdapter;
import com.alice.alicephonebook.bean.Contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvEdit;
    private TextView tvAdd;
    private TextView tvNoneTips;
    private EditText etFind;
    private ListView lvContact;

    private DBAdapter dbAdapter;

    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData(){
        dbAdapter=new DBAdapter(this);
        dbAdapter.open();
        contacts=dbAdapter.queryAllData();
        if(contacts==null){
            contacts=new ArrayList<>();
        }
    }

    private void initView(){
        tvEdit=findViewById(R.id.tvEdit);
        tvAdd=findViewById(R.id.tvAdd);
        tvNoneTips=findViewById(R.id.tvNoneTips);
        etFind=findViewById(R.id.etFind);
        lvContact=findViewById(R.id.lvContact);

        changeListView("");

        etFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeListView(etFind.getText().toString());
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangeContactActivity.class);
                intent.putExtra("addOrUpdata",true);
                startActivity(intent);
            }
        });
    }

    private void changeListView(String find){
        ContactListAdapter adapter;
        if(find.isEmpty()){
            adapter = new ContactListAdapter(this, R.layout.item_contact, contacts);
            displayListOrTips(contacts.size());
        }else{
            List<Contact> newContacts=new ArrayList<>();
            for(Contact contact:contacts){
                if(contact.getName().contains(find)){
                    newContacts.add(contact);
                }
            }
            adapter = new ContactListAdapter(this, R.layout.item_contact, newContacts);
            displayListOrTips(newContacts.size());
        }

        lvContact.setAdapter(adapter);
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "点击了"+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("contact", contacts.get(position));
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }

    private void displayListOrTips(int num){
        lvContact.setVisibility(View.GONE);
        tvNoneTips.setVisibility(View.GONE);
        if(num==0){
            tvNoneTips.setVisibility(View.VISIBLE);
        }else {
            lvContact.setVisibility(View.VISIBLE);
        }
    }


}