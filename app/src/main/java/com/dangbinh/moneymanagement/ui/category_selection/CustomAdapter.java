package com.dangbinh.moneymanagement.ui.category_selection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.dangbinh.moneymanagement.R;

/**
 * Created by dangbinh on 9/11/2020.
 */
public class CustomAdapter extends BaseAdapter {

    String[] result;
    Context context;
    int[] imageId;
    private static LayoutInflater inflater = null;
    View rowView;
    private AdapterCallback mAdapterCallback;


    public interface AdapterCallback {
        void onMethodCallback(String cate);
    }

    public CustomAdapter(Activity activity, String[] result, int[] imageId, AdapterCallback callback) {
        // TODO Auto-generated constructor stub
        this.result = result;
        this.context = activity;
        this.imageId = imageId;
        this.inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mAdapterCallback = callback;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return result[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        rowView = inflater.inflate(R.layout.list_layout, null);
        holder.tv = rowView.findViewById(R.id.names);
        holder.img = rowView.findViewById(R.id.icon);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            mAdapterCallback.onMethodCallback(result[position]);
        });
        return rowView;
    }

    public void sendSelectedItemtoFragment() {

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
            Drawable Icon = ResourcesCompat.getDrawable(context.getResources(), value, null);
            //Drawable bg = ResourcesCompat.getDrawable(getResources(), R.drawable.oval, null);
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), value);
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
