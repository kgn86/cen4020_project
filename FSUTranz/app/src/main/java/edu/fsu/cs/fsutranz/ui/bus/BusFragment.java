package edu.fsu.cs.fsutranz.ui.bus;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.fsu.cs.fsutranz.Bus_Route;
import edu.fsu.cs.fsutranz.R;
import edu.fsu.cs.fsutranz.ui.parking.ParkingFragment;
import edu.fsu.cs.fsutranz.ui.parking.itemAdapter;

public class BusFragment extends Fragment {

    private BusViewModel busViewModel;
    private BusFragment.OnBusFragmentInteractionListener helper;


    public BusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper.refreshBusData();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        busViewModel = ViewModelProviders.of(this).get(BusViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bus, container, false);


        String[] stops = helper.getStopNames();
        double[] predTimes = helper.getPredTimes();
        ArrayList<createItem> items = new ArrayList<>();

        for (int i = 0; i < stops.length; i++){
            items.add(new createItem(stops[i], i, predTimes[i]));
        }

        RecyclerView recyclerView = root.findViewById(R.id.stopsRecycler);
        RecyclerView.LayoutManager itemLayoutManager = new LinearLayoutManager(getContext()); //Layout is based on this context
        edu.fsu.cs.fsutranz.ui.bus.itemAdapter itemsAdapter = new edu.fsu.cs.fsutranz.ui.bus.itemAdapter(items); //itemAdapter to display items which loads the list of items
        recyclerView.setLayoutManager(itemLayoutManager);
        recyclerView.setHasFixedSize(true); //Fixed size
        recyclerView.setAdapter(itemsAdapter);
        return root;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BusFragment.OnBusFragmentInteractionListener) {
            helper = (BusFragment.OnBusFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBusFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        helper = null;
    }

    public interface OnBusFragmentInteractionListener {
        void refreshBusData();
        String[] getStopNames();
        double[] getPredTimes();
    }
}