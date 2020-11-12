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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dangbinh.moneymanagement.models.Transaction;
import com.dangbinh.moneymanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class TransactionsView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recycleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(com.dangbinh.moneymanagement.ui.TransactionsView.this, com.dangbinh.moneymanagement.ui.AddTransactionActivity.class);
                startActivity(i);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String FIREBASE_URL = "https://managemymoney.firebaseio.com/";
        // Firebase trans_ref = new Firebase(FIREBASE_URL);
        // Firebase ref = trans_ref.child("Transactions/" + trans_ref.getAuth().getUid());

        recycleview = (RecyclerView) findViewById(R.id.recycler_view);
        recycleview.setHasFixedSize(true);
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        /*final FirebaseRecyclerAdapter<Transaction, TransactionViewHolder> fadapter = new FirebaseRecyclerAdapter<Transaction, TransactionViewHolder>(Transaction.class, R.layout.recycler_layout, TransactionViewHolder.class, ref) {

            @Override
            public void populateViewHolder(TransactionViewHolder tv, Transaction t, int position) {

                tv.nameText2.setText("$" + Double.toString(t.getAmount()), TextView.BufferType.NORMAL);
                tv.nameText4.setText(t.getCategory(), TextView.BufferType.NORMAL);
                tv.nameTextHidden.setText(String.valueOf(this.getRef(position)), TextView.BufferType.NORMAL);
                setImageToHolder(t.getCategory(), tv.iv);
            }

            @Override
            public Transaction getItem(int position) {
                return super.getItem(position);
            }

        };*/

        // recycleview.setAdapter(fadapter);
        /*recycleview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recycleview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Transaction t = fadapter.getItem(position);
                //t.getpos();

                //Toast.makeText(getApplicationContext(),   t.getAmount()+ " is selected!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(com.dangbinh.moneymanagement.Ui.TransactionsView.this, com.dangbinh.moneymanagement.Ui.AddTransactionActivity.class);
                i.putExtra("amount", Double.toString(t.getAmount()));
                i.putExtra("category", t.getCategory());
                i.putExtra("note", t.getNote());
                i.putExtra("date", t.getDate());
                //i.putExtra("location", t.getLocation().toString());
                //i.putExtra("uid", ((TextView)view.findViewById(R.id.hidden)).getText().toString());
                i.putExtra("pos", ((TextView) view.findViewById(R.id.hidden)).getText().toString());
                i.putExtra("state", "edit");
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.nav_gallery) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(com.dangbinh.moneymanagement.ui.TransactionsView.this, TaxEstimatorActivity.class);
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


        TextView nameText2;
        TextView nameText4;
        TextView nameTextHidden;
        ImageView iv;


        public TransactionViewHolder(View itemView) {
            super(itemView);
            nameText2 = (TextView) itemView.findViewById(R.id.amt);
            nameText4 = (TextView) itemView.findViewById(R.id.cat);
            nameTextHidden = (TextView) itemView.findViewById(R.id.hidden);
            iv = (ImageView) itemView.findViewById(R.id.cat_img);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private com.dangbinh.moneymanagement.ui.TransactionsView.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final com.dangbinh.moneymanagement.ui.TransactionsView.ClickListener clickListener) {
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