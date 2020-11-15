package com.dangbinh.moneymanagement.ui.category_selection;

import com.dangbinh.moneymanagement.models.Transaction;

/**
 * Created by dangbinh on 9/11/2020.
 */
public interface IFragmentToActivity {
    void selectItem(String cate, Transaction.Type type);
}
