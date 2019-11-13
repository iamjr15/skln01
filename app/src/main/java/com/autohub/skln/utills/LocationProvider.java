package com.autohub.skln.utills;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.autohub.skln.listeners.AddressListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-24.
 */
public class LocationProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static LocationProvider sInstance;
    private GoogleApiClient mGoogleApiClient;
    //This could be list, for know i am miking is single
    private LocationListener mLocationListener;

    private LocationProvider() {

    }

    public static LocationProvider getInstance() {
        if (sInstance == null) {
            sInstance = new LocationProvider();
        }
        return sInstance;
    }

    public void start(Context context, LocationListener locationListener) {
        mLocationListener = locationListener;
        if (mGoogleApiClient == null) {
            buildGoogleApiClient(context);
            mGoogleApiClient.connect();
            return;
        }
        if (!mGoogleApiClient.isConnecting() &&
                !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    private synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        @SuppressLint("MissingPermission") Location location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLocationListener != null && location != null) {
            mLocationListener.onLocationChanged(location);
        }
        startLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void stopLocationUpdates() {
        try {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
            if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mLocationListener != null) {
            mLocationListener.onLocationChanged(location);
        }
    }

    public String getDistance(@Nullable final Location currentLocation, @NonNull final Location toLocation) {
        if (currentLocation == null) {
            return "";
        }
        float distanceTo = currentLocation.distanceTo(toLocation);
        Log.d(">>>Distance", distanceTo + "");
        if (distanceTo < 500) {
            return String.format(Locale.ENGLISH, "%.1f %s", distanceTo, "m");
        }
        float distance = distanceTo / 1000.0f;
        Log.d(">>>Distance", distanceTo + " , " + distance);
        return String.format(Locale.ENGLISH, "%.1f %s", distance, "Km");
    }


    public void getAddressFromLocation(@NonNull Context context, @Nullable final Location location, @NonNull AddressListener listener) {
        if (location == null) {
            listener.onAddressDecoded("");
            return;
        }
        new AddressProviderTask(context, location, listener).execute();
    }

    private static class AddressProviderTask extends AsyncTask<Void, Void, String> {
        private WeakReference<Context> context;
        private Location location;
        private AddressListener listener;

        AddressProviderTask(final Context context, @Nullable final Location location, final AddressListener listener) {
            this.context = new WeakReference<>(context);
            this.listener = listener;
            this.location = location;
        }

        @Override
        protected String doInBackground(Void... voids) {
            final StringBuilder builder = new StringBuilder();

            Geocoder geocoder = new Geocoder(context.get(), Locale.ENGLISH);
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0) {
                    Address fetchedAddress = addresses.get(0);
                    builder.append(fetchedAddress.getLocality());
                }
                Log.d(">>Address", builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listener.onAddressDecoded(s);
        }
    }
}
