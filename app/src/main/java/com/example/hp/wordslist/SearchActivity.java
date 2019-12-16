package com.example.hp.wordslist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private ListView listView;
   // private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView=(ListView)findViewById(R.id.search_words);
        Intent intent=getIntent();
        ArrayList<Map<String, String>> arrayList=(ArrayList<Map<String, String>>)intent.getSerializableExtra("result");
        setWordsListView(arrayList);
    }

    private void setWordsListView(ArrayList<Map<String,String>> item){
        SimpleAdapter adapter = new SimpleAdapter(this,item,R.layout.list_item,
                new String[]{"id",Words.Word.COLUMN_NAME_WORD,"mean", "eg"},
                new int[]{R.id.list_id,R.id.list_word,R.id.list_mean, R.id.list_eg});
        listView.setAdapter(adapter);
    }


}
