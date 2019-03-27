package net.lzzy.cinemanager.fragments;


import android.widget.EditText;

import net.lzzy.cinemanager.R;

/**
 *
 * @author lzzy_gxy
 * @date 2019/3/27
 * Description:
 */
public class OrdersAddFragment extends BaseFragment {

    @Override
    protected void populate() {
        EditText edt=find(R.id.fragment_add_orders_et);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_add_orders;
    }
}
