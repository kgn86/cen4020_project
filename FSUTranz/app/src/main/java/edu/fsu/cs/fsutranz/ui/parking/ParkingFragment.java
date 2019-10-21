package edu.fsu.cs.fsutranz.ui.parking;

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

    private RecyclerView parkingRecycleView; //Connected to the fragment_phone RecyclerView
    private itemAdapter itemsAdapter; //Adapter to display items
    private RecyclerView.LayoutManager itemLayoutManager; //Layoutmanager for items

    //Percentage of slots taken up
    private int callPercent;//Call Street
    private int augustinePercent; //St. Augustine
    private int spiritPercent; //Spirit Way
    private int tradiPercent; //Traditions
    private int pensaPercent; //Pensacola
    private int woodPercent; //WoodWard

    //This inflates the fragment_phone.xml for the parking tab
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_phone, container, false);

        parkingRecycleView = root.findViewById(R.id.recyclerView);

        //Creates list of items - Parking garages
        final ArrayList<createItem> itemList = new ArrayList<>();

        //Pull data for the parking percentages. Taken slots/Number of slots - Pulling from Json that is updated
            //Examples
        callPercent = 15;
        augustinePercent = 5;
        spiritPercent = 34;
        tradiPercent = 75;
        pensaPercent = 54;
        woodPercent = 94;

        //Item picture, the percentage of used parking slots, the remaining number of parking slots, the parking garage name
        itemList.add(new createItem(R.drawable.parking_icon,
                callPercent,
                Integer.toString((int) Math.floor(786 - (786 * callPercent / 100))),
                "Call Street"));

        itemList.add(new createItem(R.drawable.parking_icon,
                augustinePercent,
                Integer.toString((int) Math.floor(834 - (834 * augustinePercent / 100))),
                "St. Augustine Street"));

        itemList.add(new createItem(R.drawable.parking_icon,
                spiritPercent,
                Integer.toString((int) Math.floor(1186 - (1186 * spiritPercent / 100))),
                "Spirit Way"));

        itemList.add(new createItem(R.drawable.parking_icon,
                tradiPercent,
                Integer.toString((int) Math.floor(795 - (795 * tradiPercent / 100))),
                "Traditions Way"));

        itemList.add(new createItem(R.drawable.parking_icon,
                pensaPercent,
                Integer.toString((int) Math.floor(1118 - (1118 * pensaPercent / 100))),
                "Pensacola"));

        itemList.add(new createItem(R.drawable.parking_icon,
                woodPercent,
                Integer.toString((int) Math.floor(928 - (928 * woodPercent / 100))),
                "WoodWard"));

        parkingRecycleView.setHasFixedSize(true); //Fixed size
        itemLayoutManager = new LinearLayoutManager(getContext()); //Layout is based on this context
        itemsAdapter = new itemAdapter(itemList); //itemAdapter to display items which loads the list of items
        parkingRecycleView.setLayoutManager(itemLayoutManager);
        parkingRecycleView.setAdapter(itemsAdapter);

        //Onclick listner for each of the parking garage items
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
}