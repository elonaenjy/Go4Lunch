package com.example.go4lunch.Activity.Main.Fragments.Workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.Model.User;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.WorkmateViewModel;
import com.example.go4lunch.ViewModelFactory;

import java.util.List;


public class WorkmatesFragment extends Fragment {

    private WorkmateViewModel listWorkmatesViewModel;

    private ProgressBar progressBar;
    private TextView noWorkmates;
    private RecyclerView recyclerView;
    public static final String TAG = "WorkmatesFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        listWorkmatesViewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance()).get(WorkmateViewModel.class);

        noWorkmates = view.findViewById(R.id.workmates_no_workmates);
        progressBar = view.findViewById(R.id.workmates_progress_bar);

        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.workmates_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        progressBar.setVisibility(View.VISIBLE);

        getWorkmatesExceptCurrentUser();

        return view;

    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.setGroupVisible(R.id.main_activity_restaurants_group_sort, false);
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void getWorkmatesExceptCurrentUser() {
        listWorkmatesViewModel.getWorkmatesExceptCurrentUser().observe(
                requireActivity(),
                changedListWorkmates -> {
                    //Do something with the changed value
                    if (changedListWorkmates != null) {
                        sendResultsToAdapter(changedListWorkmates);
                    } else {
                        noWorkmatesToShow();
                    }
                });
    }

    private void noWorkmatesToShow() {
        progressBar.setVisibility(View.GONE);
        noWorkmates.setText(R.string.workmates_list_no_workmates_to_show);
        noWorkmates.setVisibility(View.VISIBLE);
    }
    private void sendResultsToAdapter(List<User> lWorkmatesExceptCurrentUser) {
        if (lWorkmatesExceptCurrentUser == null || lWorkmatesExceptCurrentUser.isEmpty()) {
            noWorkmatesToShow();
        } else {
            recyclerView.setAdapter(new WorkmatesAdapter(lWorkmatesExceptCurrentUser, this.getActivity(), TAG));        }
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

}
