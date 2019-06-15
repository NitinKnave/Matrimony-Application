package com.nmks.matrimonyapp.users;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;

import com.google.gson.Gson;
import com.nmks.matrimonyapp.MainActivity;
import com.nmks.matrimonyapp.adapter.UserListRecyAdapter;
import com.nmks.matrimonyapp.entities.RandomUserListData;
import com.nmks.matrimonyapp.entities.Result;
import com.nmks.matrimonyapp.interfaces.ApiInterface;
import com.nmks.matrimonyapp.utis.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPresenterImpl implements UsersContract.UserPresenter{

    private UsersContract.UsersView usersView ;
    private ApiInterface apiInterface;
    private Context mContext;
    private UsersContract.UserPresenter userPresenter;


    public UserPresenterImpl(Context mContext ,UsersContract.UsersView usersView, ApiInterface apiInterface) {
        this.mContext = mContext;
        this.usersView = usersView;
        this.apiInterface = apiInterface;
        this.userPresenter = this;

    }


    void callAPI(){
        usersView.setLoading(true);
        Call<RandomUserListData> userListDataCall = apiInterface.getUsersDList(10);
        userListDataCall.enqueue(new Callback<RandomUserListData>() {
            @Override
            public void onResponse(Call<RandomUserListData> call, Response<RandomUserListData> response) {
                UserListRecyAdapter listRecyAdapter = new UserListRecyAdapter(mContext, (ArrayList<Result>) response.body().getResults(),userPresenter);
                usersView.setRecyAdapter(listRecyAdapter);
                usersView.setLoading(false);
            }

            @Override
            public void onFailure(Call<RandomUserListData> call, Throwable t) {
                Log.e("TAGU", "onFailure: ", t);
                usersView.setLoading(false);

            }
        });
    }


    @Override
    public void showList() {
        callAPI(); if (new Utils().isNetworkAvailable(mContext)){
            callAPI();
        }else {
            usersView.showSnackbar("Internet Not Avalible");
        }
    }

    @Override
    public void updateList() {
        if (new Utils().isNetworkAvailable(mContext)){
            callAPI();
        }else {
           usersView.showSnackbar("Internet Not Avalible");
        }



    }

    @Override
    public void showCancelledSnack(String first) {
        usersView.showCancelledSnackBar(first);
    }


}
