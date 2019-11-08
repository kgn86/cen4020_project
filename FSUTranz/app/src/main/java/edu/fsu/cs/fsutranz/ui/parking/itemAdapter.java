package edu.fsu.cs.fsutranz.ui.parking;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.fsu.cs.fsutranz.R;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.eViewHolder> {
    private ArrayList<createItem> itemArrayList; //Array list to store the items
    public OnItemClickListener itemListner; //Used to get the position of the items

    //Used to get position of items
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //Sets variable to listener with position items
    public void setOnItemClickListener(OnItemClickListener listener) {
        itemListner = listener;
    }

    //This is the view Holder that will link the xml and java
    public static class eViewHolder extends RecyclerView.ViewHolder {
        public TextView parkingText;
        public TextView remainingSlots;
        public ProgressBar parkingCapacity;

        public eViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            parkingText = itemView.findViewById(R.id.txtParking);
            remainingSlots = itemView.findViewById(R.id.parkingSlots);
            parkingCapacity = itemView.findViewById(R.id.progressBar);

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        eViewHolder vh = new eViewHolder(v, itemListner);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull eViewHolder holder, int position) {
        createItem currentItem = itemArrayList.get(position); //This creates the current item

        //Setting the items values based on the information passed to them
        holder.parkingText.setText(currentItem.getText1());
        holder.remainingSlots.setText(currentItem.getOpenSlots());
        holder.parkingCapacity.setProgress(currentItem.getProgressNum());

        //This is only for API levels 21 and above, need to fix this issue. Just used as an example
        //Changes the color of the progress bar based on the current percentage
        if (currentItem.getProgressNum() < 33) {
            holder.parkingCapacity.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        } else if (currentItem.getProgressNum() > 33 && currentItem.getProgressNum() < 66) {
            holder.parkingCapacity.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
        } else {
            holder.parkingCapacity.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }
    }

    //Returns array size
    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }
}
