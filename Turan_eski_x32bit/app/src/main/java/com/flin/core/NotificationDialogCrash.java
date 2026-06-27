package com.flinc.core;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.flin.online.MenuActivity;
import com.flin.online.SetingsActivity;

import androidx.core.internal.view.SupportMenu;
import androidx.fragment.app.DialogFragment;

public class NotificationDialogCrash extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("У вас отсутствуют файлы для запуска игры.\nЗайдите в раздел 'Настройки' и нажмите кнопку 'Переустановить игру'.\nВо время переустановки не сворачивайте игру.")
                .setPositiveButton("Перейти в настройки", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getActivity(), SetingsActivity.class);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
}
