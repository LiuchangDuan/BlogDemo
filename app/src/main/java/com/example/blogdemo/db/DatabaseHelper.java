package com.example.blogdemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.blogdemo.beans.Article;
import com.example.blogdemo.beans.ArticleDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 *
 * 1.创建两个articles、article_content表
 *
 * 2.存储、加载文章基本信息列表
 *
 * 3.存储、加载文章内容
 *
 * 存储数据时，先将数据存储到ContentValues中，然后再插入到表中
 *
 * 加载时先从数据库中获取到Cursor，然后再从Cursor中读取数据，最后转换为对应的实体类型
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_ARTICLES = "articles";

    public static final String TABLE_ARTICLE_CONTENT = "article_content";

    private static final String CREATE_ARTICLES_TABLE_SQL = "CREATE TABLE articles ( "
            + " post_id INTEGER PRIMARY KEY UNIQUE, "
            + " author VARCHAR(30) NOT NULL, "
            + " title VARCHAR(50) NOT NULL, "
            + " category INTEGER, "
            + " publish_time VARCHAR(50) "
            + " )";

    private static final String CREATE_ARTICLE_CONTENT_TABLE_SQL = "CREATE TABLE article_content ( "
            + " post_id INTEGER PRIMARY KEY UNIQUE, "
            + " content TEXT NOT NULL "
            + " )";

    static final String DB_NAME = "tech_frontier.db";

    static final int DB_VERSION = 1;

    private SQLiteDatabase mDatabase;

    static DatabaseHelper sDatabaseHelper;

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mDatabase = getWritableDatabase();
    }

    public static void init(Context context) {
        if (sDatabaseHelper == null) {
            sDatabaseHelper = new DatabaseHelper(context);
        }
    }

    public static DatabaseHelper getInstance() {
        if (sDatabaseHelper == null) {
            throw new NullPointerException("sDatabaseHelper is null, please call init method first.");
        }
        return sDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARTICLES_TABLE_SQL);
        db.execSQL(CREATE_ARTICLE_CONTENT_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_ARTICLES);
        db.execSQL("DROP TABLE " + TABLE_ARTICLE_CONTENT);
        onCreate(db);
    }

    /**
     * 将文章列表存储到数据库中
     * @param dataList
     */
    public void saveArticles(List<Article> dataList) {
        for (Article article : dataList) {
            mDatabase.insertWithOnConflict(TABLE_ARTICLES, null, article2ContentValues(article),
                    SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    /**
     * 将文章实体转换为ContentValues对象
     * @param item
     * @return
     */
    private ContentValues article2ContentValues(Article item) {
        ContentValues newValues = new ContentValues();
        newValues.put("post_id", item.post_id);
        newValues.put("author", item.author);
        newValues.put("title", item.title);
        newValues.put("category", item.category);
        newValues.put("publish_time", item.publishTime);
        return newValues;
    }

    /**
     * 从数据库中加载文章列表
     * @return
     */
    public List<Article> loadArticles() {
        Cursor cursor = mDatabase.rawQuery("select * from " + TABLE_ARTICLES, null);
        List<Article> result = parseArticles(cursor);
        cursor.close();
        return result;
    }

    /**
     * 从Cursor解析文章列表
     * @return
     */
    private List<Article> parseArticles(Cursor cursor) {
        List<Article> articles = new ArrayList<Article>();
        while (cursor.moveToNext()) {
            Article item = new Article();
            item.post_id = cursor.getString(0);
            item.author = cursor.getString(1);
            item.title = cursor.getString(2);
            item.category = cursor.getInt(3);
            item.publishTime = cursor.getString(4);
            articles.add(item);
        }
        return articles;
    }

    /**
     * 处理存储文章内容
     * @param detail
     */
    public void saveArticleDetails(ArticleDetail detail) {
        mDatabase.insertWithOnConflict(TABLE_ARTICLE_CONTENT, null,
                articleDetailtoContentValue(detail),
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * 通过文章id获取文章详情
     * @param postId
     * @return
     */
    public ArticleDetail loadArticleDetail(String postId) {
        Cursor cursor = mDatabase.rawQuery("select * from " + TABLE_ARTICLE_CONTENT
                + " where post_id = "
                + postId, null);
        ArticleDetail detail = new ArticleDetail(postId, parseArticleContent(cursor));
        cursor.close();
        return detail;
    }

    /**
     * 解析文章内容
     * @param cursor
     * @return
     */
    private String parseArticleContent(Cursor cursor) {
        return cursor.moveToNext() ? cursor.getString(1) : "";
    }

    /**
     * 将文件详情转换为ContentValues
     * @param detail
     * @return
     */
    protected ContentValues articleDetailtoContentValue(ArticleDetail detail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("post_id", detail.postId);
        contentValues.put("content", detail.content);
        return contentValues;
    }

}
