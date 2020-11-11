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
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.dangbinh.moneymanagement.models.Transaction;
import com.dangbinh.moneymanagement.R;
import com.dangbinh.moneymanagement.ui.category_selection.CategorySelectionMainActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddTransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText amt;
    int month;
    EditText cat_selection;
    public static final String PARENT_CLASS_SOURCE = "com.dangbinh.moneymanagement.ui.AddTransactionActivity.java";
    public static final String TITLE = "Amount";
    DatePicker datePicker;
    EditText displayDate;
    Handler handler = new Handler();
    EditText note;
    EditText loc;
    boolean status_flag = false;
    String query;
    Button destruct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);
        query = null;
        loc = findViewById(R.id.trans_location);
        amt = findViewById(R.id.trans_amt);
        cat_selection = (EditText) findViewById(R.id.category_selection);
        cat_selection.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Intent i = new Intent(com.dangbinh.moneymanagement.ui.AddTransactionActivity.this, CategorySelectionMainActivity.class);
                    startActivityForResult(i, 100);
                }
            }
        });
        destruct = (Button) findViewById(R.id.delete);
        destruct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEntry();
            }
        });
        note = (EditText) findViewById(R.id.note_multi_line);
        cat_selection.setKeyListener(null);
        displayDate = (EditText) findViewById(R.id.trans_date);

        displayDate.setKeyListener(null);
        displayDate.setText(currentDate());

        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDate.setText(currentDate());
            }
        });

        displayDate.setOnClickListener(showDatePickerDialog);

        Intent i = getIntent();
        if (i.hasExtra("state")) {
            status_flag = true;
            Toast.makeText(getApplicationContext(), i.getExtras().getString("pos"), Toast.LENGTH_LONG).show();
            query = i.getExtras().getString("pos");
            cat_selection.setText(i.getExtras().getString("category"));
            note.setText(i.getExtras().getString("note"));
            displayDate.setText(i.getExtras().getString("date"));
            loc.setText(i.getExtras().getString("location"));
            amt.setText(i.getExtras().getString("amount"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                int img = setImageToHolder(result);
                cat_selection.setText(result);
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
                run_transaction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void run_transaction() {

        if (!TextUtils.isEmpty(amt.getText())) {
            if (!TextUtils.isEmpty(cat_selection.getText())) {
                finish();

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            //LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                            if (status_flag) {


                                //handler.post(this);
                                Transaction t = new Transaction(Double.parseDouble(amt.getText().toString()), cat_selection.getText().toString(), note.getText().toString(), displayDate.getText().toString(), null);
                                // t.modify(query);
                            } else {
                                Map location = new HashMap();
                                double lon = 00;//location.getLongitude();
                                double lat = 00;//location.getLatitude();
                                location.put("lat", Double.toString(lat));
                                location.put("lon", Double.toString(lon));

                                //handler.post(this);
                                Transaction t = new Transaction(Double.parseDouble(amt.getText().toString()), cat_selection.getText().toString(), note.getText().toString(), displayDate.getText().toString(), location);
                                boolean result = t.postTransaction();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                thread.start();
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
        displayDate.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);
    }

    public static class DatePickerFragment extends DialogFragment {
        public Dialog onCreateDialog(Bundle savedInstance) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (com.dangbinh.moneymanagement.ui.AddTransactionActivity) getActivity(), year, month, day);
        }
    }

    public void deleteEntry() {
        if (status_flag) {
            Transaction t = new Transaction(Double.parseDouble(amt.getText().toString()), cat_selection.getText().toString(), note.getText().toString(), displayDate.getText().toString(), null);
            // t.remove(query);
            finish();
        } else {
            finish();
        }
    }

    public int setImageToHolder(String cat) {
        int value = 0;

        switch (cat) {
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