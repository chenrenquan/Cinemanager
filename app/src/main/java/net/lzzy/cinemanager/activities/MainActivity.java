package net.lzzy.cinemanager.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.fragments.BaseFragment;
import net.lzzy.cinemanager.fragments.CinemasAddFragment;
import net.lzzy.cinemanager.fragments.CinemasFragment;
import net.lzzy.cinemanager.fragments.OnFragmentInteractionListener;
import net.lzzy.cinemanager.fragments.OrdersAddFragment;
import net.lzzy.cinemanager.fragments.OrdersFragment;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.cinemanager.utils.ViewUtils;
import net.lzzy.simpledatepicker.CustomDatePicker;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.nio.FloatBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        OnFragmentInteractionListener, CinemasAddFragment.OnCinemaCreatedListener,OrdersAddFragment.OnOrderCreatedListener {
    private TextView tvTitle;
    private View layoutMenu;
    private SearchView search;
    private FragmentManager manager = getSupportFragmentManager();
    private SparseArray<Fragment> fragmentArray = new SparseArray<>();
    private SparseArray<String> titleArray = new SparseArray<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitleMenu();
        search.setOnQueryTextListener(new ViewUtils.AbstractQueryHandler() {
            @Override
            public boolean handleQuery(String kw) {
                Fragment fragment = manager.findFragmentById(R.id.activity_main_title);
                if (fragment != null) {
                    if (fragment instanceof BaseFragment) {
                        ((BaseFragment) fragment).search(kw);
                    }
                }
                return true;
            }
        });
    }

    private void setTitleMenu() {
        layoutMenu = findViewById(R.id.bar_title_layout_menu);
        layoutMenu.setVisibility(View.GONE);
        findViewById(R.id.bar_title_img_menu).setOnClickListener(v -> {
            int visible = layoutMenu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            layoutMenu.setVisibility(visible);
        });
        tvTitle = findViewById(R.id.bar_title_tv_title);
        tvTitle.setText(R.string.bar_title_menu_orders);
        search = findViewById(R.id.main_sv_search);
        findViewById(R.id.bar_title_tv_add_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_add_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_exit).setOnClickListener(v -> System.exit(0));
    }

    @Override
    public void onClick(View v) {
        search.setVisibility(View.VISIBLE);
        layoutMenu.setVisibility(View.GONE);
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = fragmentArray.get(v.getId());
        if (fragment == null) {
            fragment = createFragment(v.getId());
            fragmentArray.put(v.getId(), fragment);
            transaction.add(R.id.activity_main_title, fragment);
        }
        for (Fragment f : manager.getFragments()) {
            transaction.hide(f);
        }
        transaction.show(fragment).commit();

    }

    private Fragment createFragment(int id) {
        switch (id) {
            case R.id.bar_title_tv_add_cinema:
                return new CinemasAddFragment();
            case R.id.bar_title_tv_view_cinema:
                return new CinemasFragment();
            case R.id.bar_title_tv_add_order:
                return new OrdersAddFragment();
            case R.id.bar_title_tv_view_order:
                return new OrdersFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public void hideSearch() {
        search.setVisibility(View.VISIBLE);

    }

    @Override
    public void cancelAddCinema() {
        Fragment cinemasFragment = fragmentArray.get(R.id.bar_title_tv_view_cinema);
        Fragment addCinemaFragment = fragmentArray.get(R.id.bar_title_tv_add_cinema);
        FragmentTransaction transaction = manager.beginTransaction();
        if (cinemasFragment == null) {
            cinemasFragment = new CinemasAddFragment();
            fragmentArray.put(R.id.bar_title_tv_view_cinema, cinemasFragment);
            transaction.add(R.id.dialog_add_order_edt_name, cinemasFragment).commit();
        }
        transaction.hide(addCinemaFragment).show(cinemasFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_cinema));

    }

    @Override
    public void saveCinema(Cinema cinema) {
        Fragment addCinemaFragment = fragmentArray.get(R.id.bar_title_tv_view_cinema);
        if (addCinemaFragment == null) {
            return;
        }

        Fragment cinemasFragment = fragmentArray.get(R.id.bar_title_tv_view_cinema);
        FragmentTransaction transaction = manager.beginTransaction();
        if (cinemasFragment == null) {
            cinemasFragment = new CinemasFragment(cinema);
            fragmentArray.put(R.id.bar_title_tv_view_cinema, cinemasFragment);
            transaction.add(R.id.dialog_add_order_edt_name, cinemasFragment);

        } else {
            ((CinemasFragment) cinemasFragment).save(cinema);
        }
        transaction.hide(addCinemaFragment).show(cinemasFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_cinema));
    }

    @Override
    public void cancelAddOrder() {
        Fragment ordersFragment = fragmentArray.get(R.id.bar_title_tv_view_cinema);
        Fragment addOrdersFragment = fragmentArray.get(R.id.bar_title_tv_add_cinema);
        FragmentTransaction transaction = manager.beginTransaction();
        if (ordersFragment == null) {
            ordersFragment = new CinemasAddFragment();
            fragmentArray.put(R.id.bar_title_tv_view_cinema, ordersFragment);
            transaction.add(R.id.dialog_add_order_edt_name, ordersFragment).commit();
        }
        transaction.hide(addOrdersFragment).show(ordersFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_cinema));
    }

    @Override
    public void saveOrder(Order order) {
        Fragment addOrdersFragment = fragmentArray.get(R.id.bar_title_tv_view_cinema);
        if (addOrdersFragment == null) {
            return;
        }

        Fragment ordersFragment = fragmentArray.get(R.id.bar_title_tv_view_cinema);
        FragmentTransaction transaction = manager.beginTransaction();
        if (ordersFragment == null) {
            ordersFragment = new OrdersFragment(order);
            fragmentArray.put(R.id.bar_title_tv_view_cinema, ordersFragment);
            transaction.add(R.id.dialog_add_order_edt_name, ordersFragment);

        } else {
            ((OrdersFragment) ordersFragment).save(order);
        }
        transaction.hide(addOrdersFragment).show(ordersFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_cinema));
    }
}
