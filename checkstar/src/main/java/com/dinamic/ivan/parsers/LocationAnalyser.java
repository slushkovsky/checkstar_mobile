package com.dinamic.ivan.parsers;


import android.Manifest;
import android.content.Context;
import android.os.Handler;

import com.dinamic.ivan.exc.PermissionNotGrantedError;
import com.dinamic.ivan.location.GPSTracker;
import com.dinamic.ivan.entities.Location;
import com.dinamic.ivan.entities.Sale;
import com.koushikdutta.async.future.FutureCallback;

import java.util.List;


public class LocationAnalyser extends BaseParser {
    int GEOLOCATION_UPDATE_INTERVAL_MINUTES = 2;
    final Handler asyncTasksHandler = new Handler();

    FutureCallback<List<Sale>> onSalesDetected;

    public LocationAnalyser(Context context, FutureCallback<List<Sale>> onSalesDetected) {
        super(context);
        this.onSalesDetected = onSalesDetected;
    }

    final Runnable updateLocationTask = new Runnable() {
        @Override
        public void run() {
            if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION))
                throw new PermissionNotGrantedError(Manifest.permission.ACCESS_FINE_LOCATION);

            double lat = 0.0;
            double lng = 0.0;

            GPSTracker tracker = new GPSTracker(context);

            if (!tracker.canGetLocation()) {
                tracker.showSettingsAlert();
            } else {
                lat = tracker.getLatitude();
                lng = tracker.getLongitude();
            }

            if (lng == 0 || lat == 0) { // NOTE: Double equality check my produce problems
//                Toast.makeText(context, "Some problems with location", Toast.LENGTH_LONG).show(); // TODO -> MainActivity
                return;
            }

            getSalesNearLocation(new Location(lat, lng), onSalesDetected);

            asyncTasksHandler.postDelayed(updateLocationTask, 1000 * 60 * GEOLOCATION_UPDATE_INTERVAL_MINUTES);
        }
    };

    public void getSalesNearLocation(Location location, FutureCallback<List<Sale>> onResult) {
//            LogicApi.getSalesNearLocation(this.context, location, onResult);
    }

    public void analyseLocation() {
        this.updateLocationTask.run();
    }

}
