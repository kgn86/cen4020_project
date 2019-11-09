package edu.fsu.cs.fsutranz.ui.bus;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.fsu.cs.fsutranz.R;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.eViewHolder> {
    private ArrayList<createItem> itemArrayList; //Array list to store the items
    public OnItemClickListener itemListener; //Used to get the position of the items

    //Used to get position of items
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //Sets variable to listener with position items
    public void setOnItemClickListener(OnItemClickListener listener) {
        itemListener = listener;
    }

    //This is the view Holder that will link the xml and java
    public static class eViewHolder extends RecyclerView.ViewHolder {
        public TextView stopName;
        public TextView stopNum;

        public eViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            stopName = itemView.findViewById(R.id.txtStop);
            stopNum = itemView.findViewById(R.id.txtStopNum);

            //Used to get items positions and make them clickable
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    //Adapter to print items on to the screen
    public itemAdapter(ArrayList<createItem> itemList) {
        itemArrayList = itemList;
    }

    //Initialize view holder
    @Override
    public eViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_item, parent, false);
        eViewHolder vh = new eViewHolder(v, itemListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull eViewHolder holder, int position) {
        createItem currentItem = itemArrayList.get(position); //This creates the current item

        //Setting the items values based on the information passed to them
        holder.stopName.setText(currentItem.getBusStopName());
        holder.stopNum.setText(Integer.toString(currentItem.getBusStopNum()));

    }

    //Returns array size
    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }
}
