package edu.fsu.cs.fsutranz.ui.bus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import edu.fsu.cs.fsutranz.R;

public class BusFragment extends Fragment {

    private BusViewModel busViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        busViewModel =
                ViewModelProviders.of(this).get(BusViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bus, container, false);

        return root;
    }
}