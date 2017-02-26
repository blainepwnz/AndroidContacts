package com.tomash.contactgetter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.tomash.contactgetter.entity.Contact;
import com.tomash.contactgetter.main.ContactsGetter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mHandler = new ContactsGetter(this);
        final ImageView photoIv = (ImageView) findViewById(R.id.photo_iv);

        findViewById(R.id.sniff_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(123);
                List<Contact> list = new ContactsGetter.Builder(MainActivity.this)
                    .onlyWithPhotos()
                    .build();
                System.out.println(12345);
            }
        });
    }
}
