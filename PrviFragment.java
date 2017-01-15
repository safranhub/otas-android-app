package si.um.ietk.safran_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class PrviFragment extends Fragment {
    public PrviFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private DeskaAdapter mAdapter;
    private List<OglasnaDeska> deskaList = new ArrayList<>();

            @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PrviFragment", "onCreate");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                //Inflate the layout for this fragment
                deskaList = (List) getArguments().getSerializable("outerMap");

        View rootView = inflater.inflate(R.layout.recycler, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mAdapter = new DeskaAdapter(deskaList);
        //Log.d("PrviFragment:(values)", deskaList.toString());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return rootView;


    }

}
