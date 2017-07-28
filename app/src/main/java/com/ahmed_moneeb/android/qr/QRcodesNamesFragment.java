package com.ahmed_moneeb.android.qr;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmed_moneeb.android.qr.data.QRcodeDbContract;

import static android.R.attr.name;


/**
 * A simple {@link Fragment} subclass.
 */
public class QRcodesNamesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public static final int LOADER_ID = 187;
    RecyclerView recyclerView;

    public QRcodesNamesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_qrcodes_names, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.name_recycler_view);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Cursor>(getContext()) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    Cursor cur;
                    cur = getContext().getContentResolver().query(QRcodeDbContract.QRcodesTable.CODES_URI
                            , null, null, null, null);
                    return cur;
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        CodesAdapter adapter = new CodesAdapter(data, (CodesAdapter.QRcodeClickListener) getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
