package net.lzzy.cinemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import android.os.Bundle;
import android.view.View;
import android.view.Window;

import android.widget.SearchView;
import android.widget.TextView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.fragments.CinemasFragment;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTitle;
    private View layoutMenu;
    private SearchView search;
    private FragmentManager manager=getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitleMenu();
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
        layoutMenu.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.bar_title_tv_add_cinema:
                break;
            case R.id.bar_title_tv_view_cinema:
                tvTitle.setText("影院列表");
                manager.beginTransaction()
                        .replace(R.id.activity_main_title, new CinemasFragment())
                        .commit();
                break;
            case R.id.bar_title_tv_add_order:
                break;
            case R.id.bar_title_tv_view_order:
                tvTitle.setText("我的订单");
                manager.beginTransaction()
                        .replace(R.id.activity_main_title, new CinemasFragment())
                        .commit();
                break;
            default:
                break;

        }
    }
}
