package com.tomash.contactgetter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tomash.contactgetter.entity.Contact;
import com.tomash.contactgetter.main.ContactsGetter;
import com.tomash.contactgetter.main.Sorting;

import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private ContactsGetter mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mHandler = new ContactsGetter(this);


        findViewById(R.id.sniff_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ContactsGetter.Builder(MainActivity.this)
                    .setSortOrder(Sorting.BY_DISPLAY_NAME_ASC)
                    .withNameLike("a")
                    .build();
            }
        });
    }
}
