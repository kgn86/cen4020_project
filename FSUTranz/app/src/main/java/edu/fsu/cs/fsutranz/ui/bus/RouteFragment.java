package edu.fsu.cs.fsutranz.ui.bus;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.math.BigDecimal;
import edu.fsu.cs.fsutranz.R;

//Represents a tab in BusFragment's view pager
public class RouteFragment extends Fragment {

    private String[] stopNames;
    private double[] stopTimes;
    private String route = "";

    private OnRouteFragmentInteractionListener helper;

    public RouteFragment(int i) {
        if (i == 0){
            route = "gold";
        }
        else if (i == 1){
            route = "garnet";
        }
        else if (i == 2){
            route = "renegade";
        }
        else if (i == 3){
            route = "heritage";
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_route, container, false);

        if (route == "gold"){
            helper.refreshGoldData();
            stopNames = helper.getGoldStopNames();
            stopTimes = helper.getGoldPredTimes();

        } else if (route == "garnet"){
            helper.refreshGarnetData();
            stopNames = helper.getGarnetStopNames();
            stopTimes = helper.getGarnetPredTimes();
        } else if (route == "renegade"){
            helper.refreshRenegadeData();
            stopNames = helper.getRenegadeStopNames();
            stopTimes = helper.getRenegadePredTimes();
        } else if (route == "heritage"){
            helper.refreshHeritageData();
            stopNames = helper.getHeritageStopNames();
            stopTimes = helper.getHeritagePredTimes();
        }

        //Creating items for recycler view
        ArrayList<createItem> items = new ArrayList<>();
        for (int i = 0; i < stopNames.length; i++){

            //Rounds double to two places
            BigDecimal bd = new BigDecimal(Double.toString(stopTimes[i]));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            double roundedDouble = bd.doubleValue();

            items.add(new createItem(stopNames[i], i, roundedDouble));
        }

        //Setting up recycler view and its adapter
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
        if (context instanceof OnRouteFragmentInteractionListener) {
            helper = (OnRouteFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRouteFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        helper = null;
    }


    public interface OnRouteFragmentInteractionListener {
        void refreshGoldData();
        void refreshGarnetData();
        void refreshRenegadeData();
        void refreshHeritageData();
        String[] getGoldStopNames();
        double[] getGoldPredTimes();
        String[] getGarnetStopNames();
        double[] getGarnetPredTimes();
        String[] getRenegadeStopNames();
        double[] getRenegadePredTimes();
        String[] getHeritageStopNames();
        double[] getHeritagePredTimes();
    }
}
