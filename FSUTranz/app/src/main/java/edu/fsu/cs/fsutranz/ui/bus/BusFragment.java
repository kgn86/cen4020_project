package edu.fsu.cs.fsutranz.ui.bus;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import edu.fsu.cs.fsutranz.R;

public class BusFragment extends Fragment {

    private BusViewModel busViewModel;
    ViewPager pager;


    public BusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        busViewModel = ViewModelProviders.of(this).get(BusViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bus, container, false);

        //Initialize the view page and its adapter
        BusFragmentPagerAdapter busAdapter = new BusFragmentPagerAdapter(getChildFragmentManager());
        pager = root.findViewById(R.id.busPager);
        pager.setAdapter(busAdapter);


        return root;
    }
}

class BusFragmentPagerAdapter extends FragmentStatePagerAdapter {

    public BusFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //Creates a RouteFragment for each tab
    public Fragment getItem(int i) {
        Fragment fragment = new RouteFragment(i);
        return fragment;
    }

    //Number of tabs
    public int getCount() {
        return 2;
    }

    //Set tab texts
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "GOLD";
        }
        else if (position == 1){
            return "GARNET";
        }
        return "";
    }
}
