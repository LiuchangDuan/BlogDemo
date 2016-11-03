package com.example.blogdemo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blogdemo.R;
import com.example.blogdemo.beans.Article;
import com.example.blogdemo.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 *
 * 主页文章列表的Adapter
 *
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    List<Article> mDataSet = new ArrayList<Article>();

    OnItemClickListener<Article> mItemClickListener;

    public ArticleAdapter(List<Article> dataSet) {
        mDataSet = dataSet;
    }

    protected Article getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return createArticleViewHolder(viewGroup);
    }

    protected ArticleViewHolder createArticleViewHolder(ViewGroup viewGroup) {
        return new ArticleViewHolder(inflateItemView(viewGroup, R.layout.recyclerview_article_item, false));
    }

    protected View inflateItemView(ViewGroup viewGroup, int layoutId, boolean attach) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, attach);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder viewHolder, int position) {
        final Article item = getItem(position);
        bindArticleToItemView(viewHolder, item);
        setupItemViewClickListener(viewHolder, item);
    }

    public void setOnItemClickListener(OnItemClickListener<Article> mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * ItemView的点击事件
     * @param viewHolder
     * @param item
     */
    protected void setupItemViewClickListener(ArticleViewHolder viewHolder, final Article item) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onClick(item);
                }
            }
        });
    }

    protected void bindArticleToItemView(ArticleViewHolder viewHolder, Article item) {
        viewHolder.titleTv.setText(item.title);
        viewHolder.publishTimeTv.setText(item.publishTime);
        viewHolder.authorTv.setText(item.author);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTv;
        public TextView publishTimeTv;
        public TextView authorTv;

        public ArticleViewHolder(View itemView) {

            super(itemView);

            titleTv = (TextView) itemView.findViewById(R.id.article_title_tv);
            publishTimeTv = (TextView) itemView.findViewById(R.id.article_time_tv);
            authorTv = (TextView) itemView.findViewById(R.id.article_author_tv);

        }

    }

}
