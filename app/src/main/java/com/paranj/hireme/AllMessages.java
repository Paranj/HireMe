package com.paranj.hireme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AllMessages extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_messages);
    }
}
