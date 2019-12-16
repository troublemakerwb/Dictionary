package com.example.hp.wordslist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        list = (ListView) findViewById(R.id.show);
        registerForContextMenu(list);
        dbHelper=new MyDatabaseHelper(this,"wangbin",null,1);
        ArrayList<Map<String, String>> items=getAll();
        setWordsListView(items);
    }

    private ArrayList<Map<String, String>> getAll() {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("Book", null, null, null, null, null, null);
        int colums = c.getColumnCount();
        while(c.moveToNext()){
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < colums; i++) {
                String word1 = c.getColumnName(i);
                String value1 = c.getString(c.getColumnIndex(word1));
                map.put(word1, value1);
            }
            list.add(map);
        }
        return list;
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        getMenuInflater().inflate(R.menu.action, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView textId=null;
        TextView textWord=null;
        TextView textMeaning=null;
        TextView textSample=null;
        AdapterView.AdapterContextMenuInfo info=null;
        View itemView=null;
        switch (item.getItemId()){
            case R.id.action_delete:           //删除单词
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();            
                itemView=info.targetView;            
                textId =(TextView)itemView.findViewById(R.id.list_id);
                if(textId!=null){                
                    String strId=textId.getText().toString();
                    System.out.println(strId);
                    DeleteDialog(strId);
                }            
                break;
            case R.id.action_update:            //修改单词
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.list_id);
                textWord =(TextView)itemView.findViewById(R.id.list_word);
                textMeaning =(TextView)itemView.findViewById(R.id.list_mean);
                textSample =(TextView)itemView.findViewById(R.id.list_eg);
                if(textId!=null && textWord!=null && textMeaning!=null && textSample!=null){
                    String strId=textId.getText().toString();
                    String strWord=textWord.getText().toString();
                    String strMeaning=textMeaning.getText().toString();
                    String strSample=textSample.getText().toString();
                    UpdateDialog(strId, strWord, strMeaning, strSample);
                }
                break;
        }    
        return true;
    }
    //删除对话框
    private void DeleteDialog(final String strId){
        new AlertDialog.Builder(this).setTitle("删除单词").setMessage("是否真的删除单词?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //既可以使用Sql语句删除，也可以使用使用delete方法删除
                DeleteUseSql(strId);
                //Delete(strId);
                setWordsListView(getAll());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }
    //使用Sql语句删除单词
    private void DeleteUseSql(String strId) {
        String sql="delete from Book where id='"+strId+"'";
        //Gets the data repository in write mode*/
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL(sql);
    }
    //修改对话框
    private void UpdateDialog(final String strId, final String strWord, final String strMeaning, final String strSample) {
        final LinearLayout tableLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.add, null);
        ((EditText)tableLayout.findViewById(R.id.word)).setText(strWord);
        ((EditText)tableLayout.findViewById(R.id.mean)).setText(strMeaning);
        ((EditText)tableLayout.findViewById(R.id.eg)).setText(strSample);
        new AlertDialog.Builder(this).setTitle("修改单词")//标题
        .setView(tableLayout) //设置视图
        // 确定按钮及其动作
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String strNewWord = ((EditText) tableLayout.findViewById(R.id.word)).getText().toString();
                String strNewMeaning = ((EditText) tableLayout.findViewById(R.id.mean)).getText().toString();
                String strNewSample = ((EditText) tableLayout.findViewById(R.id.eg)).getText().toString();
                //既可以使用Sql语句更新，也可以使用使用update方法更新
                UpdateUseSql(strId,strNewWord, strNewMeaning, strNewSample);
                //  Update(strId, strNewWord, strNewMeaning, strNewSample);
                setWordsListView(getAll());
            }
        })
        //取消按钮及其动作
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create()//创建对话框
         .show();//显示对话框
        }
    //使用Sql语句更新单词
    private void UpdateUseSql(String strId,String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql="update Book set word=?,mean=?,eg=? where id=?";
        db.execSQL(sql, new String[]{strWord, strMeaning, strSample,strId});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    private void setWordsListView(ArrayList<Map<String,String>> item){
        SimpleAdapter adapter = new SimpleAdapter(this,item,R.layout.list_item,
                new String[]{"id",Words.Word.COLUMN_NAME_WORD,"mean", "eg"},
                new int[]{R.id.list_id,R.id.list_word,R.id.list_mean, R.id.list_eg});
        list.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id=menuItem.getItemId();
        switch (id){
            case R.id.search:
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater=getLayoutInflater();
                final LinearLayout linearLayout1=(LinearLayout)getLayoutInflater().inflate(R.layout.search,null);
                builder.setView(linearLayout1).setTitle("单词查找").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String txtSearchWord=((EditText)linearLayout1.findViewById(R.id.searchXML)).getText().toString();
                        ArrayList<Map<String, String>> items=null;
                        items=SearchUseSql(txtSearchWord);
                        // items=Search(txtSearchWord);
                        if(items.size()>0) {
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("result",items);
                            Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this,"没有找到",Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder0=new AlertDialog.Builder(MainActivity.this);
                            builder0.setTitle("跳转提示").setMessage("是否通过网页进行查询？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://dict.youdao.com/w/"+txtSearchWord+"/#keyfrom=dict2.top"));
                                    startActivity(intent);
                                }
                            }).setNegativeButton("否",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).create().show();
                        }
                    }
                }).setNegativeButton("取消",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create().show();
                return true;
            case R.id.add:
                AlertDialog.Builder builder1=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater1=getLayoutInflater();
                final LinearLayout linearLayout=(LinearLayout)getLayoutInflater().inflate(R.layout.add,null);
                builder1.setView(linearLayout).setTitle("新增单词").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str1="000";
                        String str2="111";
                        String str3="222";
                        EditText et1=(EditText)linearLayout.findViewById(R.id.word);
                        if(et1!=null) {
                            str1 = et1.getText().toString();
                        }
                        EditText et2=(EditText)linearLayout.findViewById(R.id.mean);
                        if(et2!=null) {
                            str2 = et2.getText().toString();
                        }
                        EditText et3=(EditText)linearLayout.findViewById(R.id.eg);
                        if(et3!=null) {
                            str3 = et3.getText().toString();
                        }
                        SQLiteDatabase db=dbHelper.getWritableDatabase();
                        ContentValues values=new ContentValues();
                        values.put("word",str1);
                        values.put("mean",str2);
                        values.put("eg",str3);
                        /*values.put("word","str1");
                        values.put("mean","str2");
                        values.put("eg","str3");*/
                        db.insert("Book",null,values);
                        values.clear();
                        ArrayList<Map<String, String>> items=getAll();
                        setWordsListView(items);
                    }
                }).setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
                return true;
            case R.id.news:
                AlertDialog.Builder builder2=new AlertDialog.Builder(MainActivity.this);
                builder2.setTitle("将要访问网页").setMessage("将要访问英语新闻网").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.globaltimes.cn"));
                        startActivity(intent);
                    }
                }).setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    //使用Sql语句查找
    private ArrayList<Map<String, String>> SearchUseSql(String strWordSearch) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql="select * from Book where word like ? order by word desc";
        Cursor c=db.rawQuery(sql,new String[]{"%"+strWordSearch+"%"});
        return ConvertCursor2List(c);
    }

    private ArrayList<Map<String,String>> ConvertCursor2List(Cursor cursor){

        ArrayList<Map<String,String>> arrayList=new ArrayList<Map<String,String>>();

        while(cursor.moveToNext()) {

            Map<String, String> map = new HashMap<String, String>();

            map.put("word", cursor.getString(1));

            map.put("mean", cursor.getString(2));

            map.put("eg", cursor.getString(3));

            arrayList.add(map);

        }

        return arrayList;

    }

}
