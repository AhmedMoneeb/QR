package com.ahmed_moneeb.android.qr.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.ahmed_moneeb.android.qr.R;

/**
 * Implementation of App Widget functionality.
 */
public class SavedCodesWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

/*        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.saved_codes_widget);
        // Instruct the widget manager to update the widget
        Intent intent = new Intent(context, CodesWidgetService.class);
        views.setRemoteAdapter(appWidgetId , R.id.grid_view, intent);*/

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.saved_codes_widget);
        Intent intent = new Intent(context, CodesWidgetService.class);
        views.setRemoteAdapter(R.id.grid_view, intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.grid_view);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

