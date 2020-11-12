package com.dangbinh.moneymanagement.ui.category_selection;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dangbinh.moneymanagement.R;


public class CategorySelectionView3 extends Fragment implements CustomAdapter.AdapterCallback {

    ListView list;
    String[] names = {"Loan", "Debt"};
    CustomAdapter.AdapterCallback mAdapterCallback;
    IFragmentToActivity mCallback;

    int[] img_list = {
            R.mipmap.ic_loan,
            R.mipmap.ic_debt
    };

    public CategorySelectionView3() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (IFragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_selection_page3, container, false);
        list = (ListView) view.findViewById(R.id.list3);
        list.setAdapter(new CustomAdapter(getActivity(), names, img_list, this));

        return view;
    }

    @Override
    public void onMethodCallback(String msg) {
        // do something
        mCallback.selectedItem(msg);
    }
}
