package edu.fsu.cs.fsutranz.ui.parking;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.fsu.cs.fsutranz.R;

public class ParkingFragment extends Fragment {

    private OnParkingFragmentInteractionListener helper;

    private class Garage{

        Garage(String garage){
            name = garage;
            occupied = 0;
            capacity = 0;
            threshold = 0;
            percentage = 0;
        }

        String name;
        int occupied;
        int capacity;
        int threshold;
        int percentage;
    }

    public ParkingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper.refreshData();
    }

    //This inflates the fragment_parking.xml for the parking tab
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_parking, container, false);

        String[] data = helper.getData();
        helper.setActionBarTitle();

        Garage call = new Garage("Call Street");
        Garage stAug = new Garage("Saint Augustine Street");
        Garage spirit = new Garage("Spirit Way");
        Garage traditions = new Garage("Traditions");
        Garage pensacola = new Garage("Pensacola Street");
        Garage woodward = new Garage("Woodward Avenue");

        Garage[] garages = new Garage[]{call, stAug, spirit, traditions, pensacola, woodward};

        int dataIndex = 0;

        if (!data[0].equals("NO RECORDS")) {
            for (int i = 0; i < garages.length; i++) {
                //name is already set
                //garages[i].name = data[dataIndex].substring(10, data[dataIndex++].length()-1);

                dataIndex++;

                //ask Robby if you're confused about the substring indexes
                garages[i].occupied = Integer.parseInt(data[dataIndex].substring(13, data[dataIndex++].length()));
                garages[i].capacity = Integer.parseInt(data[dataIndex].substring(13, data[dataIndex++].length()));
                garages[i].threshold = Integer.parseInt(data[dataIndex].substring(14, data[dataIndex++].length()));
                garages[i].percentage = garages[i].occupied * 100 / garages[i].capacity;
            }
        }
        else{
            Toast.makeText(getContext(), "No data available :(", Toast.LENGTH_LONG).show();
        }

            RecyclerView parkingRecycleView = root.findViewById(R.id.recyclerView); //Connected to the fragment_parking RecyclerView

            //Creates list of items - Parking garages
            final ArrayList<createItem> itemList = new ArrayList<>();

            //Item picture, the percentage of used parking slots, the remaining number of parking slots, the parking garage name
            for (int i = 0; i < 6; i++) {
                itemList.add(new createItem(
                        garages[i].percentage,
                        Integer.toString((int) Math.floor(garages[i].capacity - (garages[i].capacity * garages[i].percentage / 100))),
                        garages[i].name));
            }

            parkingRecycleView.setHasFixedSize(true); //Fixed size
            RecyclerView.LayoutManager itemLayoutManager = new LinearLayoutManager(getContext()); //Layout is based on this context
            itemAdapter itemsAdapter = new itemAdapter(itemList); //itemAdapter to display items which loads the list of items
            parkingRecycleView.setLayoutManager(itemLayoutManager);
            parkingRecycleView.setAdapter(itemsAdapter);

            //Onclick listener for each of the parking garage items
            itemsAdapter.setOnItemClickListener(new itemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    //Need to get the progress to change
                    itemList.get(position).setProgress(100);

                    switch (position) {
                        case 0:
                            Toast.makeText(getContext(), "CALL", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            Toast.makeText(getContext(), "AUGUSTINE", Toast.LENGTH_LONG).show();
                            break;
                        case 2:
                            Toast.makeText(getContext(), "SPIRIT", Toast.LENGTH_LONG).show();
                            break;
                        case 3:
                            Toast.makeText(getContext(), "TRADITIONS", Toast.LENGTH_LONG).show();
                            break;
                        case 4:
                            Toast.makeText(getContext(), "PENSACOLA", Toast.LENGTH_LONG).show();
                            break;
                        case 5:
                            Toast.makeText(getContext(), "WOODWARD", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            });

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnParkingFragmentInteractionListener) {
            helper = (OnParkingFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnParkingFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        helper = null;
    }

    public interface OnParkingFragmentInteractionListener {
        void refreshData();
        String[] getData();
        void setActionBarTitle();
    }
}