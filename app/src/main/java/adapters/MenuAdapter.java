package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blogdemo.R;

import java.util.ArrayList;
import java.util.List;

import beans.MenuItem;
import listener.OnItemClickListener;

/**
 * Created by Administrator on 2016/11/3.
 *
 * 菜单列表Adapter
 *
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    List<MenuItem> mDataSet = new ArrayList<MenuItem>();

    OnItemClickListener<MenuItem> mItemClickListener;

    public MenuAdapter(List<MenuItem> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MenuViewHolder(inflateItemView(viewGroup, R.layout.menu_item));
    }

    @Override
    public void onBindViewHolder(MenuViewHolder viewHolder, int position) {
        final MenuItem item = getItem(position);
        viewHolder.nameTextView.setText(item.text);
        viewHolder.userImageView.setImageResource(item.iconResId);
        setupItemViewClickListener(viewHolder, item);
    }

    public void setOnItemClickListener(OnItemClickListener<MenuItem> clickListener) {
        this.mItemClickListener = clickListener;
    }

    /**
     * ItemView的点击事件
     * @param viewHolder
     * @param item
     */
    protected void setupItemViewClickListener(MenuViewHolder viewHolder, final MenuItem item) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onClick(item);
                }
            }
        });
    }

    protected MenuItem getItem(int position) {
        return mDataSet.get(position);
    }

    protected View inflateItemView(ViewGroup viewGroup, int layoutId) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {

        public ImageView userImageView;
        public TextView nameTextView;

        public MenuViewHolder(View itemView) {
            super(itemView);
            userImageView = (ImageView) itemView.findViewById(R.id.menu_icon_imageview);
            nameTextView = (TextView) itemView.findViewById(R.id.menu_text_tv);
        }

    }

}
