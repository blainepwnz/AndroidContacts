package com.tomash.contactgetter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tomash.contactgetter.entity.Address;
import com.tomash.contactgetter.entity.Contact;
import com.tomash.contactgetter.main.DbHandler;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DbHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new DbHandler(this);
        findViewById(R.id.sniff_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(123);
               List<Contact> contactList =  mHandler.getContacts();
                System.out.println(12345);
                System.out.println(contactList);
            }
        });
    }
}
