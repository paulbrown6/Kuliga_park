package com.app.kuliga.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.kuliga.R;
import com.app.kuliga.data.api.retrofit.ApiCall;
import com.app.kuliga.ui.activitys.AuthActivity;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;


public class DialogLogOut {

    private AlertDialog alert;

    @SuppressLint("InflateParams")
    public AlertDialog createAlertDialog(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_message_fulljava, null)).setCancelable(true);
        alert = builder.create();
        alert.show();
        Button button = alert.findViewById(R.id.dialog_message_button_ok);
        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.cancel();
                    MainViewModel.deleteUser();
                    ApiCall.getInstance().setCookie(null);
                    activity.startActivity(new Intent(activity, AuthActivity.class));
                    activity.finish();
                }
            });
        TextView text = alert.findViewById(R.id.dialog_message_text);
        text.setText(message);
        return alert;
    }


}
