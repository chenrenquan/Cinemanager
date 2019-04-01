package net.lzzy.cinemanager.fragments;


import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityPicker;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;


/**
 * @author Administrator
 */
public class CinemasAddFragment extends BaseFragment {
    private String province = "广西壮族自治区";
    private String city = "柳州市";
    private String area = "鱼峰区";
    private OnFragmentInteractionListener listener;
    private OnCinemaCreatedListener cinemaListener;
    private EditText edt;
    private TextView tv;



    @Override
    protected void populate() {
        listener.hideSearch();
        tv = find(R.id.dialog_add_cinema_tv_area);
        edt = find(R.id.dialog_add_cinema_edt_name);
        find(R.id.dialog_cinema_layout_area).setOnClickListener(v -> {
            JDCityPicker cityPicker = new JDCityPicker();
            cityPicker.init(getActivity());
            cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                @Override
                public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                    CinemasAddFragment.this.province = province.getName();
                    CinemasAddFragment.this.city = city.getName();
                    CinemasAddFragment.this.area = district.getName();
                    String loc = province.getName() + city.getName() + district.getName();
                    tv.setText(loc);
                }

                @Override
                public void onCancel() {
                }
            });
            cityPicker.showCityPicker();
        });
        showDialog();

    }

    private void showDialog() {

        find(R.id.dialog_add_cinema_btn_no)
                .setOnClickListener(v -> {
                    cinemaListener.cancelAddCinema();
                });

        find(R.id.dialog_add_cinema_btn_ok).setOnClickListener(v -> {
            String name = edt.getText().toString();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(getActivity(), "不能为空", Toast.LENGTH_SHORT).show();
            } else {
                Cinema cinema = new Cinema();
                cinema.setArea(area);
                cinema.setCity(city);
                cinema.setName(name);
                cinema.setProvince(province);
                cinema.setLocation(tv.getText().toString());
                edt.setText(" ");
                cinemaListener.saveCinema(cinema);
            }

        });
        find(R.id.dialog_add_cinema_btn_cancel).setOnClickListener(v ->
                cinemaListener.cancelAddCinema());
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_add_cinema;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            listener.hideSearch();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnFragmentInteractionListener) context;
            cinemaListener = (OnCinemaCreatedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "必须实现OnFragmentInteractionListener&OnCinemaCreatedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        cinemaListener = null;
    }

    public interface OnCinemaCreatedListener {
        /**
         * 取消保持数据
         */
        void cancelAddCinema();

        /**
         *
         */
        void saveCinema(Cinema cinema);
    }


}
