package com.app.kuliga.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.kuliga.R;


public class DialogDocumentation {

    private AlertDialog alert;

    @SuppressLint("InflateParams")
    public AlertDialog createAlertDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_documentation, null)).setCancelable(true);
        alert = builder.create();
        alert.show();
        Button buttonCancel = alert.findViewById(R.id.button_dialog_cancel_delete);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.cancel();
                }
            });
        TextView karta = alert.findViewById(R.id.dialog_karta);
        karta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kuliga-park.ru/rezhim-raboty-i-kontakty/"));
                activity.startActivity(browserIntent);
            }
        });
        TextView politic = alert.findViewById(R.id.dialog_text_politics);
        politic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kuliga-park.ru/wp-content/uploads/2021/12/Polzovatelskoe-soglashenie-ob-usloviyah-i-poryadke-ispolzovaniya-mobilnogo-prilozheniya-.docx"));
                activity.startActivity(browserIntent);
            }
        });
        TextView mobipay = alert.findViewById(R.id.dialog_text_mobile_pay);
        mobipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kuliga-park.ru/wp-content/uploads/2021/12/Sposoby-oplaty-v-mobilnom-prilozhenii.docx"));
                activity.startActivity(browserIntent);
            }
        });
        TextView serverpay = alert.findViewById(R.id.dialog_text_server_pay);
        serverpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kuliga-park.ru/wp-content/uploads/2021/12/usloviya-oplaty-i-vozvrata-dlya-mobilnogo-prilozheniya.docx"));
                activity.startActivity(browserIntent);
            }
        });
        return alert;
    }


}
