package com.ahmed_moneeb.android.qr;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class GeneratingQRcodeFragment extends Fragment {


    public generateButtonClicklistener listener;
    Button generateButton;
    EditText editText;

    public GeneratingQRcodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_generating_qrcode, container, false);
        editText = (EditText) view.findViewById(R.id.generatedTxt_id);

        generateButton = (Button) view.findViewById(R.id.generate_button);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //editText.clearFocus();
                if (editText.getText().toString().length() != 0) {
                    listener.onGenerateButtonClicked(editText.getText().toString());
                } else {
                    Toast.makeText(getActivity(), getString(R.string.enter_content_warning), Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (generateButtonClicklistener) context;
    }

    public interface generateButtonClicklistener {
        void onGenerateButtonClicked(String text);
    }
}
