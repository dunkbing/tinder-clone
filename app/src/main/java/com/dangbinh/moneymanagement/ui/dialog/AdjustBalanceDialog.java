package com.dangbinh.moneymanagement.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.dangbinh.moneymanagement.R;

public class AdjustBalanceDialog extends AppCompatDialogFragment {

    private EditText editTextBalance;
    private OnAdjustBalanceListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.adjust_balance_dialog, null);
        builder.setView(view)
                .setTitle(R.string.action_adjust_balance)
                .setNegativeButton("cancel", (dialog, which) -> {

                })
                .setPositiveButton("Ok", (dialog, which) -> {
                    String balance = editTextBalance.getText().toString();
                    if (listener != null) {
                        listener.applyBalance(balance);
                    }
                });
        editTextBalance = view.findViewById(R.id.edit_text_balance);
        return builder.create();
    }

    public void setOnAdjustBalanceListener(OnAdjustBalanceListener listener) {
        this.listener = listener;
    }

    public interface OnAdjustBalanceListener {
        void applyBalance(String balance);
    }
}
