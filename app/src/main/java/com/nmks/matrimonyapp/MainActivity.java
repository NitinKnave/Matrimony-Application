package com.nmks.matrimonyapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.nmks.matrimonyapp.adapter.SpacesItemDecoration;
import com.nmks.matrimonyapp.adapter.UserListRecyAdapter;
import com.nmks.matrimonyapp.interfaces.ApiInterface;
import com.nmks.matrimonyapp.users.UserPresenterImpl;
import com.nmks.matrimonyapp.users.UsersContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements UsersContract.UsersView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @BindView(R.id.userRV)
    RecyclerView userRV;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.relativeMain)
    RelativeLayout relativeLayoutMain;
    private ApiInterface apiService;
    private UserPresenterImpl userPresenter;
    private UserListRecyAdapter listRecyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        userPresenter = new UserPresenterImpl(this, this, apiService);
        refreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark,R.color.colorPrimary,android.R.color.holo_blue_light);
        refreshLayout.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        userRV.setLayoutManager(layoutManager);
        userRV.addItemDecoration(new SpacesItemDecoration(20));
    }

    @Override
    protected void onResume() {
        super.onResume();
        userPresenter.showList();
    }

    @Override
    public void setRecyAdapter(UserListRecyAdapter listRecyAdapter) {
        this.listRecyAdapter = listRecyAdapter;
        userRV.setAdapter(listRecyAdapter);
    }

    @Override
    public void setLoading(boolean b) {
        refreshLayout.setRefreshing(b);
    }

    @Override
    public void showSnackbar(String str) {
        refreshLayout.setRefreshing(false);
        Snackbar snackbar = Snackbar
                .make(relativeLayoutMain, str, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getResources().getString(R.string.try_again), this);
        snackbar.show();

    }

    @Override
    public void showCancelledSnackBar(String first) {
        Snackbar snackbar = Snackbar
                .make(relativeLayoutMain, "You Have Cancelled "+first+" Invitation", Snackbar.LENGTH_LONG);
        snackbar.setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listRecyAdapter.addItemToPosition();
            }
        });
        snackbar.show();
    }

    @Override
    public void onRefresh() {
        userPresenter.updateList();
    }

    @Override
    public void onClick(View v) {
        userPresenter.updateList();
    }
}
