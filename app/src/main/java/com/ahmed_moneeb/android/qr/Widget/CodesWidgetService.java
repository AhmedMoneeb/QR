package com.ahmed_moneeb.android.qr.Widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ahmed_moneeb.android.qr.R;
import com.ahmed_moneeb.android.qr.data.QRcodeDbContract;

/**
 * Created by Ahmed on 7/21/2017.
 */

public class CodesWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CodesRemoteViewsFactory(getApplicationContext());
    }

}

class CodesRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    Cursor cursor;

    public CodesRemoteViewsFactory(Context context) {
        this.context = context;
    }

    public static RemoteViews getCodesRemoteViews(Context con) {
        RemoteViews views = new RemoteViews(con.getPackageName(), R.layout.saved_codes_widget);
        Intent intent = new Intent(con, CodesWidgetService.class);
        views.setRemoteAdapter(R.id.grid_view, intent);

        return views;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) cursor.close();
        try {
            cursor = context.getContentResolver().query(QRcodeDbContract.QRcodesTable.CODES_URI
                    , null, null, null, null);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (cursor == null) return 0;

        else {
            int x = cursor.getCount();
            return x;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (cursor == null || cursor.getCount() == 0) return null;
        cursor.moveToPosition(position);
        int nameIndex = cursor.getColumnIndex(QRcodeDbContract.QRcodesTable.COLUMN_CODE_NAME);
        String str = cursor.getString(nameIndex);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.saved_item_at_widget);
        views.setTextViewText(R.id.code_name_at_widget, str);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
