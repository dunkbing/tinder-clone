package com.dangbinh.moneymanagement.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.dangbinh.moneymanagement.R;
import com.dangbinh.moneymanagement.models.Transaction;
import com.dangbinh.moneymanagement.ui.category_selection.CategorySelectionMainActivity;
import com.dangbinh.moneymanagement.ui.category_selection.CategorySelectionMainActivity.CateSelectionResult;
import com.dangbinh.moneymanagement.utils.TaskRunner;
import com.dangbinh.moneymanagement.utils.UiUtils;

import java.util.Calendar;

public class AddTransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText editTextAmount;
    private int month;
    private EditText editTextCateSelection;
    private DatePicker datePicker;
    private EditText editTextDisplayDate;
    private Handler handler = new Handler();
    private EditText editTextNote;
    private String transId = null;
    private String query;
    private Button buttonDelete;
    public static final String PARENT_CLASS_SOURCE = "com.dangbinh.moneymanagement.ui.AddTransactionActivity.java";
    public static final String TITLE = "Amount";
    private static final int CATEGORY_REQUEST_CODE = 100;
    private Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);
        query = null;
        editTextAmount = findViewById(R.id.trans_amt);
        editTextCateSelection = findViewById(R.id.category_selection);
        editTextCateSelection.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Intent i = new Intent(AddTransactionActivity.this, CategorySelectionMainActivity.class);
                startActivityForResult(i, CATEGORY_REQUEST_CODE);
            }
        });
        buttonDelete = findViewById(R.id.delete);
        buttonDelete.setOnClickListener(view -> deleteEntry());
        editTextNote = findViewById(R.id.note_multi_line);
        editTextCateSelection.setKeyListener(null);
        editTextDisplayDate = findViewById(R.id.trans_date);

        editTextDisplayDate.setKeyListener(null);
        editTextDisplayDate.setText(currentDate());

        editTextDisplayDate.setOnClickListener(view -> editTextDisplayDate.setText(currentDate()));
        editTextDisplayDate.setOnClickListener(showDatePickerDialog);

        Intent i = getIntent();
        if (i.hasExtra("transId")) {
            Toast.makeText(getApplicationContext(), i.getExtras().getString("pos"), Toast.LENGTH_LONG).show();
            transId = i.getStringExtra("transId");
            query = i.getExtras().getString("pos");
            editTextCateSelection.setText(i.getExtras().getString("category"));
            editTextNote.setText(i.getExtras().getString("note"));
            editTextDisplayDate.setText(i.getExtras().getString("date"));
            editTextAmount.setText(i.getExtras().getString("amount"));
        }
        transaction = new Transaction();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CATEGORY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String cate = data.getStringExtra(CateSelectionResult.CATEGORY.toString());
                String type = data.getStringExtra(CateSelectionResult.TYPE.toString());
                transaction.setType(Transaction.Type.valueOf(type));
                transaction.setCategory(cate);
                int img = setImageToHolder(cate);
                editTextCateSelection.setText(cate);
                //Log.d("img",""+img);
                //cat_selection.setCompoundDrawablesWithIntrinsicBounds(img, 0, 0, 0);
                //cat_selection.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_location_black_36dp, 0, 0, 0);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_transaction_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                runTransaction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void runTransaction() {
        if (!TextUtils.isEmpty(editTextAmount.getText())) {
            if (!TextUtils.isEmpty(editTextCateSelection.getText())) {
                TaskRunner.run(() -> {
                    LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    boolean result = false;
                    double amount = Double.parseDouble(editTextAmount.getText().toString());
                    if (transaction.getType() == Transaction.Type.INCOME) {
                        Transaction.adjustBalance(String.valueOf(Transaction.getBalance() + amount));
                    } else if (transaction.getType() == Transaction.Type.EXPENSE) {
                        Transaction.adjustBalance(String.valueOf(Transaction.getBalance() - amount));
                    }
                    transaction.setAmount(amount);
                    transaction.setNote(editTextNote.getText().toString());
                    transaction.setDate(editTextDisplayDate.getText().toString());
                    if (transId != null) {
                        transaction.modify(transId);
                        Log.d("update transaction", transId);
                    } else {
                        result = transaction.postTransaction();
                    }
                    return result;
                });
                Intent intent = new Intent(this, TransactionsViewActivity.class);
                startActivity(intent);
                finish();
            } else {
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                Toast.makeText(getApplicationContext(), "Category cannot be empty", Toast.LENGTH_LONG).show();
            }
        } else {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            Toast.makeText(getApplicationContext(), "Amount cannot be 0", Toast.LENGTH_LONG).show();
        }
    }

    public String currentDate() {
        Calendar c = Calendar.getInstance();
        return (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DATE) + "/" + c.get(Calendar.YEAR);
    }

    public View.OnClickListener showDatePickerDialog = v -> {
        DialogFragment df = new DatePickerFragment();
        df.show(getSupportFragmentManager(), "datePicker");
    };

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // do something with the date chosen by the user.
        editTextDisplayDate.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);
    }

    public static class DatePickerFragment extends DialogFragment {
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstance) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (AddTransactionActivity) getActivity(), year, month, day);
        }
    }

    public void deleteEntry() {
        if (transId != null) {
            Transaction t = new Transaction();
            t.remove(transId);
        }
        UiUtils.startActivity(this, TransactionsViewActivity.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UiUtils.startActivity(this, TransactionsViewActivity.class);
    }

    public int setImageToHolder(String cate) {
        int value = 0;
        switch (cate) {
            case "Debt":
                value = R.mipmap.ic_debt;
                break;
            case "Education":
                value = R.mipmap.ic_education;
                break;
            case "Friends":
                value = R.mipmap.ic_friends;
                break;
            case "Health":
                value = R.mipmap.ic_health;
                break;
            case "Loan":
                value = R.mipmap.ic_loan;
                break;
            case "Shopping":
                value = R.mipmap.ic_shopping;
                break;
            case "Gifts":
                value = R.mipmap.ic_gift;
                break;
            case "Salary":
                value = R.mipmap.ic_salary;
                break;
            default:
                break;
        }
        return value;
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 50;
        int targetHeight = 50;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }
}