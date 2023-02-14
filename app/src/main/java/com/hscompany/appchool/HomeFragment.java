package com.hscompany.appchool;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.zip.Inflater;

public class HomeFragment extends Fragment {

    View view;
    Context context;
    ListView listViewHome;
    ScrollView svHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getContext();

        view = inflater.inflate(R.layout.fragment_home, container, false);

        listViewHome = view.findViewById(R.id.listView_home);
        svHome = view.findViewById(R.id.sv_home);

        return view;
    }
}
