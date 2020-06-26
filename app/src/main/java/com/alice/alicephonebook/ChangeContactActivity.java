package com.alice.alicephonebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alice.alicephonebook.adapter.DBAdapter;
import com.alice.alicephonebook.bean.Contact;

public class ChangeContactActivity extends AppCompatActivity {

    private TextView tvBack;
    private TextView tvSure;
    private EditText etContactName;
    private EditText etContactPhone;
    private EditText etContactAddress;

    private Contact contact;
    private boolean addOrUpdata;

    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_contact);

        initData();
        initView();
    }

    private void initData(){
        addOrUpdata=getIntent().getBooleanExtra("addOrUpdata",true);
        if(!addOrUpdata){
            contact=(Contact)getIntent().getSerializableExtra("contact");
        }else{
            contact=new Contact();
        }
        dbAdapter=new DBAdapter(this);
        dbAdapter.open();
    }

    private void initView(){
        tvBack=findViewById(R.id.tvBack);
        tvSure=findViewById(R.id.tvSure);
        etContactName=findViewById(R.id.etContactName);
        etContactPhone=findViewById(R.id.etContactPhone);
        etContactAddress=findViewById(R.id.etContactAddress);

        if(!addOrUpdata){
            etContactName.setHint(contact.getName());
            etContactPhone.setHint(contact.getPhone());
            etContactAddress.setHint(contact.getAddress());
        }

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.setName(etContactName.getText().toString());
                contact.setPhone(etContactPhone.getText().toString());
                contact.setAddress(etContactAddress.getText().toString());
                if(addOrUpdata){
                    dbAdapter.insert(contact);
                    Toast.makeText(ChangeContactActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                }else{
                    dbAdapter.updateOneData(contact.getId(),contact);
                    Toast.makeText(ChangeContactActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(ChangeContactActivity.this, ContactActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("contact", contact);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }
}
