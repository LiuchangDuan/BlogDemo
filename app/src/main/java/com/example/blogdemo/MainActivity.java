package com.example.blogdemo;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.blogdemo.adapters.MenuAdapter;
import com.example.blogdemo.beans.MenuItem;
import com.example.blogdemo.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 当用户点击ActionBar左上角的按钮时弹出菜单
 * 当用户点击不同的菜单时切换不同的Fragment
 */
public class MainActivity extends ActionBarActivity {

    // Fragment管理器
    protected FragmentManager mFragmentManager;

    // 除了ActionBar之外的根视图
    private DrawerLayout mDrawerLayout;

    // 菜单RecyclerView
    private RecyclerView mMenuRecyclerView;

    protected Toolbar mToolbar;

    // ActionBar上的菜单开关
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getFragmentManager();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);

        mDrawerToggle.syncState();

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mMenuRecyclerView = (RecyclerView) findViewById(R.id.menu_recyclerview);

        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(new MenuItem(getString(R.string.article), R.drawable.home));
        menuItems.add(new MenuItem(getString(R.string.about_menu), R.drawable.about));
        menuItems.add(new MenuItem(getString(R.string.exit), R.drawable.exit));

        MenuAdapter menuAdapter = new MenuAdapter(menuItems);
        menuAdapter.setOnItemClickListener(new OnItemClickListener<MenuItem>() {
            @Override
            public void onClick(MenuItem item) {
                clickMenuItem(item);
            }
        });

        mMenuRecyclerView.setAdapter(menuAdapter);

    }

    // TODO
    private void clickMenuItem(MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.iconResId) {
            case R.drawable.home:
                break;
            case R.drawable.about:
                break;
            // 退出
            case R.drawable.exit:
                isQuit();
                break;
            default:
                break;
        }
    }

    private void isQuit() {
        new AlertDialog.Builder(this).setTitle("确认退出？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("取消", null).create().show();
    }

}
