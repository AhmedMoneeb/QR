package com.ahmed_moneeb.android.qr;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmed_moneeb.android.qr.data.QRcodeDbContract;

/**
 * Created by Ahmed on 7/20/2017.
 */

public class CodesAdapter extends RecyclerView.Adapter<CodesAdapter.CodeNameViewHolder> {

    Cursor cursor;


    QRcodeClickListener listener;

    CodesAdapter(Cursor c, QRcodeClickListener listener) {
        this.listener = listener;
        cursor = c;
    }

    @Override
    public CodeNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int id = R.layout.saved_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(id, parent, false);
        CodeNameViewHolder viewHolder = new CodeNameViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CodeNameViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        int count = cursor.getCount();
        return count;
    }

    public interface QRcodeClickListener {
        void onNameClicked(String name);
    }

    public class CodeNameViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView listItem;

        //CardView cardView;
        public CodeNameViewHolder(View itemView) {
            super(itemView);
            //cardView = (CardView) itemView.findViewById(R.id.cardView123);
            listItem = (TextView) itemView.findViewById(R.id.saved_QRCode_name_textView);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            int nameIndex = cursor.getColumnIndex(QRcodeDbContract.QRcodesTable.COLUMN_CODE_NAME);
            cursor.moveToPosition(listIndex);
            String str = cursor.getString(nameIndex);
            listItem.setText(str);
        }

        @Override
        public void onClick(View v) {
            TextView t = (TextView) v.findViewById(R.id.saved_QRCode_name_textView);
            String str = t.getText().toString();
            listener.onNameClicked(str);
        }
    }
}
