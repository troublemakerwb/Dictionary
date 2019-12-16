package com.example.hp.wordslist;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hp on 2019/11/29.
 */

public class Words {
    public static final String AUTHORITY = "com.example.hp.wordslist.wordsprovider";//URI授权者
    public Words() {
    }
    public static abstract class Word implements BaseColumns {
        public static final String TABLE_NAME="Book";
        public static final String COLUMN_NAME_WORD="word";//列：单词
        public static final String COLUMN_NAME_MEANING="mean";//列：单词含义
        public static final String COLUMN_NAME_SAMPLE="eg";//单词示例
        //MIME类型
        public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
        public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
        public static final String MINE_ITEM = "vnd.com.example.hp.wordlist.Words";
        public static final String MINE_TYPE_SINGLE = MIME_ITEM_PREFIX + "/" + MINE_ITEM;
        public static final String MINE_TYPE_MULTIPLE = MIME_DIR_PREFIX + "/" + MINE_ITEM;
        public static final String PATH_SINGLE = "Book/#";//单条数据的路径
        public static final String PATH_MULTIPLE = "Book";//多条数据的路径
        // Content Uri
    //    public static final String CONTENT_URI_STRING =
        public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;
        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
    }
}