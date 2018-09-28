package com.covert.verify360.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.covert.verify360.R;

/**
 * Created by user on 3/27/2018.
 */

public class FragmentDashboard extends Fragment {
    private LinearLayout newcases,pendingCases;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        newcases = v.findViewById(R.id.newCases);
        pendingCases=v.findViewById(R.id.pendingCases);
        newcases.setOnClickListener(v1 -> getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment,new NewCasesFragment(),"new cases")
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit());
        pendingCases.setOnClickListener(vl ->
                getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment,new PendingCasesFragment(),"Pending cases")
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit());
        return v;
    }
}
