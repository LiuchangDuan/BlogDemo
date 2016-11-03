package com.example.blogdemo;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blogdemo.adapters.ArticleAdapter;
import com.example.blogdemo.beans.Article;
import com.example.blogdemo.listener.OnItemClickListener;
import com.example.blogdemo.myui.AutoLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */
public class ArticleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AutoLoadRecyclerView.OnLoadListener {

    protected ArticleAdapter mAdapter;

    protected SwipeRefreshLayout mSwipeRefreshLayout;

    protected AutoLoadRecyclerView mRecyclerView;

    final protected List<Article> mDataSet = new ArrayList<Article>();

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        initRefreshView(rootView);
        initAdapter();
        mSwipeRefreshLayout.setRefreshing(true);
        return rootView;
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

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoad() {

    }

}
