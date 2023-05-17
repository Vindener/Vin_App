package com.example.vin.addition;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.vin.LoadActivity;
import com.example.vin.R;

public class ConfirmationDialogFragment  extends DialogFragment {
    public static final int RESULT_YES = 1;
    public static final int RESULT_NO = 0;

    public static final int REQUEST_CONFIRMATION = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Ви бажаєте вийти з облікового запису?")
                .setPositiveButton("Так", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendResult(RESULT_YES);
                    }
                })
                .setNegativeButton("Ні", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendResult(RESULT_NO);
                    }
                });
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();

        // Отключить возможность взаимодействия за пределами окна
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONFIRMATION) {
            if (resultCode == Activity.RESULT_OK) {
                int result = data.getIntExtra("result", -1);
                if (result == ConfirmationDialogFragment.RESULT_YES) {
                    // Действия при выборе "Да"
                    getActivity().finish(); // Закрывает текущую активити
                } else if (result == ConfirmationDialogFragment.RESULT_NO) {
                    // Действия при выборе "Нет"
                }
            }
        }
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("result", resultCode);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);

        if (resultCode == RESULT_YES) {
            // Очистить SharedPreferences
            SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            // Перейти на LoadActivity

            Intent loadIntent = new Intent(getActivity(), LoadActivity.class);
            startActivity(loadIntent);
            getActivity().finish();
        }
        else{

        }
    }

}
