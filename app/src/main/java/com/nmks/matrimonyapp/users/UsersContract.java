package com.nmks.matrimonyapp.users;

import com.nmks.matrimonyapp.adapter.UserListRecyAdapter;

public class UsersContract {


    public interface UsersView {
        void setRecyAdapter(UserListRecyAdapter listRecyAdapter);

        void setLoading(boolean b);

        void showSnackbar(String str);

        void showCancelledSnackBar(String first);
    }

    public interface UserPresenter {
        void showList();

        void updateList();

        void showCancelledSnack(String first);
    }
}
