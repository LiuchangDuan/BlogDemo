package com.example.blogdemo;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blogdemo.adapters.ArticleAdapter;
import com.example.blogdemo.beans.Article;
import com.example.blogdemo.db.DatabaseHelper;
import com.example.blogdemo.listener.OnItemClickListener;
import com.example.blogdemo.myui.AutoLoadRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 文章列表主界面，包含自动滚动广告栏、文章列表
 *
 * Created by Administrator on 2016/11/3.
 */
public class ArticleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AutoLoadRecyclerView.OnLoadListener {

    protected int mCategory = Article.ALL;

    protected ArticleAdapter mAdapter;

    protected SwipeRefreshLayout mSwipeRefreshLayout;

    protected AutoLoadRecyclerView mRecyclerView;

    final protected List<Article> mDataSet = new ArrayList<Article>();

    private int mPageIndex = 1;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        initRefreshView(rootView);
        initAdapter();
        mSwipeRefreshLayout.setRefreshing(true);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataSet.addAll(DatabaseHelper.getInstance().loadArticles());
        mAdapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        mAdapter = new ArticleAdapter(mDataSet);
        mAdapter.setOnItemClickListener(new OnItemClickListener<Article>() {
            @Override
            public void onClick(Article article) {
                if (article != null) {
                    loadArticle(article);
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        getArticles(1);
    }

    private void loadArticle(Article article) {
        Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
        intent.putExtra("post_id", article.post_id);
        intent.putExtra("title", article.title);
        startActivity(intent);
    }

    private void initRefreshView(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (AutoLoadRecyclerView) rootView.findViewById(R.id.articles_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setOnLoadListener(this);
    }

    public void setArticleCategory(int category) {
        mCategory = category;
    }

    /**
     * 从网络上获取文章数据
     *
     * getArticles函数只是封装了异步请求流程
     *
     * @param page
     */
    private void getArticles(final int page) {
        new AsyncTask<Void, Void, List<Article>>() {

            protected void onPreExecute() {
                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            protected List<Article> doInBackground(Void... params) {
                return performRequest(page);
            }

            protected void onPostExecute(List<Article> result) {
                // 移除已经更新的数据
                result.removeAll(mDataSet);
                // 添加新数据
                mDataSet.addAll(result);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                // 存储文章列表到数据库
                DatabaseHelper.getInstance().saveArticles(result);
                if (result.size() > 0) {
                    mPageIndex++;
                }
            }

        }.execute();
    }

    private List<Article> performRequest(int page) {
        HttpURLConnection urlConnection = null;
        try {
            /**
             * type=articles代表要请求文章类型的数据
             * page代表加载的是第一页的数据（因为数据较多，需要分页加载）
             * count代表每次返回的文章数量
             * category=1代表返回所有类型的文章
             * 返回的结果是JSONArray
             */
            String getUrl = "http://www.devtf.cn/api/v1/?type=articles&page=" + mPageIndex
                    + "&count=20&category=1";
            urlConnection = (HttpURLConnection) new URL(getUrl).openConnection();
            urlConnection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            return parse(new JSONArray(result)); // 将Json解析为文章列表
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return new ArrayList<Article>();
    }

    /**
     * 解析文章数据
     * @param jsonArray
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private List<Article> parse(JSONArray jsonArray) {
        List<Article> articleLists = new LinkedList<Article>();
        int count = jsonArray.length();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (int i = 0; i < count; i++) {
            JSONObject itemObject = jsonArray.optJSONObject(i);
            Article articleItem = new Article();
            articleItem.title = itemObject.optString("title");
            articleItem.author = itemObject.optString("author");
            articleItem.post_id = itemObject.optString("post_id");
            String category = itemObject.optString("category");
            articleItem.category = TextUtils.isEmpty(category) ? 0 : Integer.valueOf(category);
            articleItem.publishTime = formatDate(dateformat, itemObject.optString("date"));
            Log.d("", "title : " + articleItem.title + ", id = " + articleItem.post_id);
            articleLists.add(articleItem);
        }
        return articleLists;
    }

    private String formatDate(SimpleDateFormat dateFormat, String dateString) {
        try {
            Date date = dateFormat.parse(dateString);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onRefresh() {
        getArticles(1);
    }

    @Override
    public void onLoad() {
        mSwipeRefreshLayout.setRefreshing(true);
        getArticles(mPageIndex);
    }

}
