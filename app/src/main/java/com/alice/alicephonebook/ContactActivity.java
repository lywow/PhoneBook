package com.alice.alicephonebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alice.alicephonebook.adapter.DBAdapter;
import com.alice.alicephonebook.bean.Contact;

import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private TextView tvBack;
    private TextView tvEdit;
    private TextView tvContactName;
    private TextView tvContactPhone;
    private TextView tvContactAddress;
    private Button bnDelete;

    private Contact contact;

    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initData();
        initView();
    }

    private void initData(){
        contact=(Contact)getIntent().getSerializableExtra("contact");
        dbAdapter=new DBAdapter(this);
        dbAdapter.open();
    }

    private void initView(){
        tvBack=findViewById(R.id.tvBack);
        tvEdit=findViewById(R.id.tvEdit);
        tvContactName=findViewById(R.id.tvContactName);
        tvContactPhone=findViewById(R.id.tvContactPhone);
        tvContactAddress=findViewById(R.id.tvContactAddress);
        bnDelete=findViewById(R.id.bnDelete);

        tvContactName.setText("姓名："+contact.getName());
        tvContactPhone.setText("电话："+contact.getPhone());
        tvContactAddress.setText("地址："+contact.getAddress());

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ContactActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, ChangeContactActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("contact", contact);
                intent.putExtras(mBundle);
                intent.putExtra("addOrUpdata",false);
                startActivity(intent);
            }
        });

        bnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbAdapter.deleteOneData(contact.getId());
                Intent intent=new Intent(ContactActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
