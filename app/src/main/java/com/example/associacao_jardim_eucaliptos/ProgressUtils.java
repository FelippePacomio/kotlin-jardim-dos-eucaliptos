package com.example.associacao_jardim_eucaliptos;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class ProgressUtils {

    private static AlertDialog progressDialog;

    public static void showProgressDialog(Context context) {
        if (progressDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.progress_layout, null);
            builder.setView(view);

            progressDialog = builder.create();
        }

        progressDialog.show();
    }

    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
