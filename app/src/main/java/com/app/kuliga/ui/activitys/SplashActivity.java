package com.app.kuliga.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.app.kuliga.data.api.retrofit.ApiCall;
import com.app.kuliga.data.entity.User;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;
import java.util.List;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<User> users = User.listAll(User.class);
        User user = null;
        if (users != null && !users.isEmpty()) {
            user = users.get(users.size() - 1);
        }
        if (user == null) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        } else {
            MainViewModel.setUser(user);
            ApiCall.getInstance().setCookie(user.getCookie());
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}