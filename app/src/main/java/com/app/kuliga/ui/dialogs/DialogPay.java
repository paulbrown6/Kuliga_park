package com.app.kuliga.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.kuliga.R;


public class DialogPay {

    private AlertDialog alert;
    public static final int MESSAGE_OK = 1;
    public static final int MESSAGE_FAIL = 2;

    @SuppressLint("InflateParams")
    public AlertDialog createAlertDialog(Activity activity, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_pay_fulljava, null)).setCancelable(true);
        alert = builder.create();
        alert.show();
        Button button = alert.findViewById(R.id.dialog_pay_button_ok);
        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.cancel();
                }
            });
        TextView text_success = alert.findViewById(R.id.dialog_pay_text_success);
        TextView text_false = alert.findViewById(R.id.dialog_pay_text_false);
        if (message == 1) {
            text_success.setVisibility(View.VISIBLE);
        }
        if (message == 2) {
            text_false.setVisibility(View.VISIBLE);
        }
        return alert;
    }


}
