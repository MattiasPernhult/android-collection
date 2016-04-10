package com.fam.fam.bullshitapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class SpaceActivity extends FragmentActivity {

    private GoogleMap mMap;
    private Controller controller;
    private ReceiveISSPosition receiveISSPosition;
    private List<LatLng> latLngList;
    private MarkerOptions markerOptions;
    private PolylineOptions polylineOptions;
    private boolean first;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);
        setUpMapIfNeeded();

        if (latLngList == null) {
            latLngList = new ArrayList<>();
        }

        Intent intent = getIntent();

        controller = (Controller) intent.getSerializableExtra("controller");

        if (controller != null) {
            receiveISSPosition = new ReceiveISSPosition();
        }

        first = true;
    }

    private void openInformationDialog(ArrayList<String> persons) {
        InformationDialog informationDialog = new InformationDialog();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("names", persons);
        informationDialog.setArguments(bundle);
        informationDialog.show(getFragmentManager(), "InformationDialogISSPersons");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_space, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_person_ISS:
                new MyTask().execute(Constants.GET_PERSONS_ON_ISS);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        if (controller != null) {
            if (receiveISSPosition == null) {
                receiveISSPosition = new ReceiveISSPosition();
            }
            new Thread(receiveISSPosition).start();
        }
    }

    @Override
    protected void onPause() {
        receiveISSPosition.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiveISSPosition.stop();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
    }

    private void addMarker(LatLng latLng, String title, String desc) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().title(title).snippet(desc).icon(BitmapDescriptorFactory.fromResource(R.mipmap.iss_icon)).position(latLng));
        latLngList.add(latLng);
        mMap.addPolyline(new PolylineOptions().color(Color.RED).width(10).geodesic(true).addAll(latLngList));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 5);
        if (first) {
            mMap.animateCamera(update);
            first = false;
        }
    }

    private class MyTask extends AsyncTask<String, String, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String choice = strings[0];

            switch (choice) {
                case Constants.GET_PERSONS_ON_ISS:
                    return controller.getISSPersons();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            openInformationDialog(strings);
        }
    }

    private class ReceiveISSPosition implements Runnable {
        private boolean run;

        public ReceiveISSPosition() {
            this.run = true;
        }

        public void stop() {
            this.run = false;
        }

        @Override
        public void run() {

            this.run = true;
            while (this.run) {
                final LatLng latLng = controller.getISSPosition();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addMarker(latLng, "ISS", "The position of the International Space Station.");
                    }
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
