package com.dangbinh.moneymanagement.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dangbinh.moneymanagement.R;
import com.dangbinh.moneymanagement.models.Account;
import com.dangbinh.moneymanagement.models.Transaction;
import com.dangbinh.moneymanagement.utils.Constants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.Query;

import java.util.Objects;

public class TransactionsViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "TransactionsViewActivit";
    RecyclerView recycleView;
    FirebaseRecyclerAdapter<Transaction, TransactionViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent i = new Intent(TransactionsViewActivity.this, AddTransactionActivity.class);
            startActivity(i);
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //      .setAction("Action", null).show();
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recycleView = findViewById(R.id.recycler_view);
        recycleView.setHasFixedSize(true);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        fetch();
        recycleView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recycleView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Transaction t = adapter.getItem(position);
                //t.getpos();

                Toast.makeText(getApplicationContext(),   t.getAmount()+ " is selected!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(TransactionsViewActivity.this, com.dangbinh.moneymanagement.ui.AddTransactionActivity.class);
                i.putExtra("transId", ((TextView) view.findViewById(R.id.hidden_trans_id)).getText().toString());
                i.putExtra("amount", Double.toString(t.getAmount()));
                i.putExtra("category", t.getCategory());
                i.putExtra("note", t.getNote());
                i.putExtra("date", t.getDate());
                //i.putExtra("location", t.getLocation().toString());
                //i.putExtra("uid", ((TextView)view.findViewById(R.id.hidden)).getText().toString());
                i.putExtra("position", ((TextView) view.findViewById(R.id.hidden_position)).getText().toString());
                i.putExtra("state", "edit");
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void fetch() {
        Query query = Transaction.getTransRef()
                .child("transactions");
        Log.d(TAG, query.getRef().getKey());
        FirebaseRecyclerOptions<Transaction> options = new FirebaseRecyclerOptions.Builder<Transaction>()
                .setQuery(query, snapshot -> {
                    String key = snapshot.getKey();
                    return new Transaction(key,
                            Double.parseDouble(snapshot.child("amount").getValue().toString()),
                            snapshot.child("category").getValue().toString(),
                            snapshot.child("note").getValue().toString(),
                            snapshot.child("date").getValue().toString());
                })
                .build();
        adapter = new FirebaseRecyclerAdapter<Transaction, TransactionViewHolder>(options) {
            @NonNull
            @Override
            public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_layout, parent, false);
                return new TransactionViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull TransactionViewHolder holder, int position, @NonNull Transaction model) {
                Log.d(TAG, model.getAmount()+"");
                holder.setTextViewAmount("$" + Objects.requireNonNull(model).getAmount());
                holder.setTextViewCate(model.getCategory());
                holder.setHiddenPosition(String.valueOf(this.getRef(position)));
                holder.setHiddenTransId(model.getTransId());
                setImageToHolder(model.getCategory(), holder.imageView);
            }
        };
        recycleView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.transcations_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
            case R.id.nav_gallery:
                return true;
            case R.id.action_sign_out:
                Constants.AUTH_INSTANCE.signOut();
                Intent i = new Intent(this, LoginActivity.class);
                i.putExtra(Account.LOGGED_OUT, true);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(TransactionsViewActivity.this, TaxEstimatorActivity.class);
            startActivity(i);
        }
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAmount;
        TextView textViewCate;
        TextView hiddenPosition;
        TextView hiddenTransId;
        ImageView imageView;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, itemView.findViewById(R.id.text_view_amount) == null ? "null" : "not null");
            textViewAmount = itemView.findViewById(R.id.text_view_amount);
            textViewCate = itemView.findViewById(R.id.text_view_cate);
            hiddenPosition = itemView.findViewById(R.id.hidden_position);
            hiddenTransId = itemView.findViewById(R.id.hidden_trans_id);
            imageView = itemView.findViewById(R.id.cat_img);
        }

        public void setTextViewAmount(String string) {
            textViewAmount.setText(string, TextView.BufferType.NORMAL);
        }

        public void setTextViewCate(String string) {
            textViewCate.setText(string, TextView.BufferType.NORMAL);
        }

        public void setHiddenPosition(String string) {
            hiddenPosition.setText(string, TextView.BufferType.NORMAL);
        }

        public void setHiddenTransId(String string) {
            hiddenTransId.setText(string);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private TransactionsViewActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final TransactionsViewActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void setImageToHolder(String cat, ImageView v) {
        int value = 0;

        switch (cat) {
            case "Interest Received":
                value = R.mipmap.ic_loan;
                break;
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
        try {
            Drawable Icon = ResourcesCompat.getDrawable(getResources(), value, null);
            //Drawable bg = ResourcesCompat.getDrawable(getResources(), R.drawable.oval, null);
            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), value);
            v.setImageBitmap(getRoundedShape(icon));
            //v.setBackground(bg);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
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