package com.app.kuliga.ui.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.kuliga.R;
import com.app.kuliga.data.api.retrofit.ApiCall;
import com.app.kuliga.data.entity.Card;
import com.app.kuliga.data.entity.Service;
import com.app.kuliga.ui.dialogs.DialogLogOut;
import com.app.kuliga.ui.dialogs.DialogPay;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;
import com.app.kuliga.ui.fragments.viewmodels.ServicesViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private AppBarConfiguration mAppBarConfiguration;
    private final String TAG = "MainActivity";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fulljava);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout_fulljava);
        NavigationView navigationViewDrawer = findViewById(R.id.nav_view_drawer);
        BottomNavigationView navigationViewBottom = findViewById(R.id.nav_view_bottom);
        mAppBarConfiguration = new AppBarConfiguration.Builder().setOpenableLayout(drawer).build();
        navController = Navigation.findNavController(this, R.id.nav_host_main);
        NavigationUI.setupWithNavController(navigationViewDrawer, navController);
        NavigationUI.setupWithNavController(navigationViewBottom, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, drawer);
        navigationViewDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.close();
                if (item.getItemId() != R.id.nav_logout) {
                    navigationViewDrawer.setCheckedItem(item.getItemId());
                    navController.navigate(item.getItemId());
                } else {
                    logOut();
                }
                return true;
            }
        });
        navigationViewBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navController.navigate(item.getItemId());
                return true;
            }
        });
        MainViewModel.loadCards();
        MainViewModel.getLoadCardsResult().observe(this, new Observer<MainViewModel.LoadCardsResult>() {
            @Override
            public void onChanged(MainViewModel.LoadCardsResult result) {
                if (result == null) {
                    return;
                }
                if (result.getError() != null) {
                    Toast.makeText(MainActivity.this,
                            "Ошибка загрузки карт", Toast.LENGTH_SHORT).show();
                }
                if (result.getSuccess() != null) {
                    if (!result.getSuccess().isEmpty()) {
                        MainViewModel.saveCardsToDB(result.getSuccess());
                        for (Card card : result.getSuccess()) {
                            MainViewModel.loadCardBalance(card);
                        }
                    }
                }
            }
        });
        MainViewModel.getLoadCardResult().observe(this, new Observer<MainViewModel.LoadCardResult>() {
            @Override
            public void onChanged(MainViewModel.LoadCardResult result) {
                if (result == null) {
                    DialogLogOut dialog = new DialogLogOut();
                    dialog.createAlertDialog(MainActivity.this,
                            "Для корректной работы приложения требуется перезайти в аккаунт.");
                    return;
                }
                if (result.getError() != null) {
                    Toast.makeText(MainActivity.this,
                            "Ошибка загрузки баланса карты", Toast.LENGTH_SHORT).show();
                }
                if (result.getSuccess() != null) {
                    Card card = result.getSuccess();
                    if (card.getBalance() != null){
                        for(Card c : MainViewModel.getCards()) {
                            if (card.getCode().equals(c.getCode())) {
                                c.setBalance(card.getBalance());
                                MainViewModel.saveCardToDB(c);
                            }
                        }
                    }
                }
            }
        });

        MainViewModel.getPayResult().observe(this, new Observer<MainViewModel.OperationResult>() {
            @Override
            public void onChanged(MainViewModel.OperationResult result) {
                if (result.getError() != null) {
                    Toast.makeText(MainActivity.this,
                            "Ошибка пополнения баланса", Toast.LENGTH_SHORT).show();
                    navController.navigateUp();
                }
                if (result.getSuccess() != null) {
                    if (result.getSuccess() == 0){
                        if (!MainViewModel.getPayURL().equals("")) {
                            startActivity(new Intent(MainActivity.this, WebActivity.class));
                        } else {
                            DialogPay dialogPay = new DialogPay();
                            dialogPay.createAlertDialog(MainActivity.this, 2);
                        }
                        navController.navigateUp();
                    } else {
                        DialogPay dialogPay = new DialogPay();
                        dialogPay.createAlertDialog(MainActivity.this, result.getSuccess());
                        MainViewModel.startTimer();
                        for (Card card: MainViewModel.getCards()) {
                            MainViewModel.loadCardBalance(card);
                        }
                    }
                }
            }
        });

        ServicesViewModel.loadServices();
        ServicesViewModel.getServicesResult().observe(this, new Observer<ServicesViewModel.LoadServicesResult>() {
            @Override
            public void onChanged(ServicesViewModel.LoadServicesResult result) {
                if (result == null) {
                    return;
                }
                if (result.getError() != null) {
                    ServicesViewModel.loadServicesFromDB();
                }
                if (result.getSuccess() != null) {
                    ServicesViewModel.saveServicesToDB(result.getSuccess());
                    for (Service service : result.getSuccess()) {
                        ServicesViewModel.loadTariff(service);
                    }
                }
            }
        });
        ServicesViewModel.getServiceResult().observe(this, new Observer<ServicesViewModel.LoadServiceResult>() {
            @Override
            public void onChanged(ServicesViewModel.LoadServiceResult result) {
                if (result == null) {
                    return;
                }
                if (result.getError() != null) {
//                    Toast.makeText(MainActivity.this,
//                            "Ошибка загрузки тарифов", Toast.LENGTH_SHORT).show();
                }
                if (result.getSuccess() != null) {
                    Service service = result.getSuccess();
                    ServicesViewModel.saveServiceToDB(service);
                }
            }
        });

        View headerLayout = navigationViewDrawer.getHeaderView(0);
        TextView username = headerLayout.findViewById(R.id.header_username);
        if (MainViewModel.getUser().getLastName() != null
                && MainViewModel.getUser().getFirstName() != null){
            username.setText(MainViewModel.getUser().getLastName() +
                    " " + MainViewModel.getUser().getFirstName());
        } else {
            username.setText(MainViewModel.getUser().getEmail());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            if (!navController.navigateUp()) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_fulljava);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut(){
        MainViewModel.deleteUser();
        ApiCall.getInstance().setCookie(null);
        startActivity(new Intent(MainActivity.this, AuthActivity.class));
        MainActivity.this.finish();
    }
}