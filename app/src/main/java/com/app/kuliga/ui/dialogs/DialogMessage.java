package com.app.kuliga.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.kuliga.R;


public class DialogMessage {

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
                }
            });
        TextView text = alert.findViewById(R.id.dialog_message_text);
        text.setText(message);
        return alert;
    }


}
