package net.lzzy.cinemanager.fragments;


import android.content.Context;


import net.lzzy.cinemanager.R;


/**
 * @author Administrator
 */
public class OrdersAddFragment extends BaseFragment {
    private OnFragmentInteractionListener listener;

    @Override
    protected void populate() {
        listener.hideSearch();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_add_orders;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "必须实现OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
