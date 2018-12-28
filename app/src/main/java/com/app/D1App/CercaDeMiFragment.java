package com.app.D1App;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.app.D1App.API.ListaToAdapter;
import com.app.D1App.API.ServiceGenerator;
import com.app.D1App.API.TiendasAppService;
import com.app.D1App.Adapter.ListaAdapter;
import com.app.D1App.ApiObject.GetListObject;
import com.app.D1App.ApiResponse.GetListingResponse;
import com.app.D1App.MapRoute.DirectionsJSONParser;
import com.app.D1App.MapRoute.MapAnimator;
import com.app.D1App.Utils.AppCommon;
import com.app.D1App.Utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CercaDeMiFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.mapLayout)
    RelativeLayout mapLayout;

    @BindView(R.id.listRecyclerView)
    RecyclerView listRecyclerView;

    @BindView(R.id.markerClickLayout)
    RelativeLayout markerClickLayout;

    @BindView(R.id.map)
    Button map;

    @BindView(R.id.list)
    Button list;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private GoogleMap googleMap;

    ListaAdapter listaAdapter;
    GPSTracker gpsTracker;
    Call call;
    ArrayList<GetListObject> storeArrayList;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    ArrayList<Marker> markerArrayList = new ArrayList<>();

    Marker selectedMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cercademi, container, false);
        ButterKnife.bind(this, view);
        gpsTracker = new GPSTracker(getActivity());
        listRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);

        list.performClick();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
    }


    @OnClick(R.id.map)
    void setMapLayout() {
        map.setBackgroundResource(R.drawable.map_select);
        list.setBackgroundResource(R.drawable.list_unselect);
        map.setTextColor(Color.WHITE);
        list.setTextColor(Color.WHITE);
        mapLayout.setVisibility(View.VISIBLE);
        listRecyclerView.setVisibility(View.GONE);
        if (googleMap != null) {
            googleMap.clear();
            addMultipleMarkers();
        }
    }

    @OnClick(R.id.list)
    void setListLayout() {
        list.setBackgroundResource(R.drawable.list_select);
        map.setBackgroundResource(R.drawable.map_unselect);
        list.setTextColor(Color.WHITE);
        map.setTextColor(Color.WHITE);
        mapLayout.setVisibility(View.GONE);
        listRecyclerView.setVisibility(View.VISIBLE);
       // callingListingAPI(6.217660, -75.564220);
        callingListingAPI(gpsTracker.getLatitude(), gpsTracker.getLongitude());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
//        LatLng currentLocation = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
//        this.googleMap.addMarker(new MarkerOptions().position(currentLocation))
//                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.current_location));
//        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
//        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12.0f));
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                markerClickLayout.setVisibility(View.VISIBLE);
                selectedMarker = marker;
            }
        });
    }

    @OnClick(R.id.cancelButton)
    public void cancelClick(View v) {
        markerClickLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.wazeClick)
    public void wazeClick(View v) {
        String uri = "geo:" + selectedMarker.getPosition().latitude + "," + selectedMarker.getPosition().longitude;
        startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(uri)));
        markerClickLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.googleMapClick)
    public void googleMapClick(View v) {

        String url = "http://maps.google.com/maps?daddr=" + selectedMarker.getPosition().latitude + "," + selectedMarker.getPosition().longitude;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(url));
        startActivity(intent);
        markerClickLayout.setVisibility(View.GONE);
    }


    void addMultipleMarkers() {

        if (googleMap != null) {
            String currentLat = String.valueOf(gpsTracker.getLatitude());
            String curretnLon = String.valueOf(gpsTracker.getLongitude());
            LatLng currentUserLatLon = new LatLng(Double.parseDouble(currentLat), Double.parseDouble(curretnLon));
            if (currentUserLatLon != null) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.current_location);
                Marker marker = this.googleMap.addMarker(new MarkerOptions()
                        .position(currentUserLatLon)
                        .title("")
                        .icon(icon)
                        .alpha(1.0f));
            }
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserLatLon));
            this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLon, 12.0f));

            try {
                for (int i = 0; i < storeArrayList.size(); i++) {

                    Log.v("Name",storeArrayList.get(i).getTitle());
                    String latitude = storeArrayList.get(i).getLatitude().replace(",",".").trim();
                    String longitude = storeArrayList.get(i).getLongitude().replace(",",".").trim();
                    LatLng latlog = new LatLng(Double.parseDouble(latitude),
                            Double.parseDouble(longitude));

                    Marker marker = this.googleMap.addMarker(new MarkerOptions()
                            .position(latlog)
                            .title(storeArrayList.get(i).getTitle())
                            .snippet(storeArrayList.get(i).getAddress())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.other_location)));
                    markerArrayList.add(marker);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    void callingListingAPI(double latitude, double longitude) {
        AppCommon.getInstance(getActivity()).setNonTouchableFlags(getActivity());
        if (AppCommon.getInstance(getActivity()).isConnectingToInternet(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            TiendasAppService tiendasAppService = ServiceGenerator.createService(TiendasAppService.class);
            call = tiendasAppService.GetListing(latitude, longitude);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        if (response.body() != null) {
                            GetListingResponse getListingResponse = (GetListingResponse) response.body();
                            if (getListingResponse.getSuccess() == 1) {
                                storeArrayList = getListingResponse.getResult();
                                setListaAdapter();
                                addMultipleMarkers();
                            } else {
                                AppCommon.showDialog(getActivity(), getListingResponse.getError());
                            }
                        }
                    } else {
                        AppCommon.showDialog(getActivity(), getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
                    progressBar.setVisibility(View.GONE);
                    if (t instanceof JsonSyntaxException) {
                        AppCommon.showDialog(getActivity(), "Something went wrong please try Again");
                    } else {
                        AppCommon.showDialog(getActivity(), getResources().getString(R.string.network_error));
                    }
                }
            });
        } else {
            AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
            AppCommon.showDialog(getActivity(), getResources().getString(R.string.network_error));
        }
    }

    void setListaAdapter() {
        listaAdapter = new ListaAdapter(getActivity(), storeArrayList, listaToAdapterInterface);
        listRecyclerView.setAdapter(listaAdapter);
    }

    ListaToAdapter listaToAdapterInterface = new ListaToAdapter() {
        @Override
        public void respond(int position, String name, String distance, String latitude, String longitude) {
            setOnClickAdapter(position, latitude, longitude, name);
        }
    };

    void setOnClickAdapter(int position, String latitude, String longitude, String name) {
        mapLayout.setVisibility(View.VISIBLE);
        listRecyclerView.setVisibility(View.GONE);
         String currentLat = String.valueOf(gpsTracker.getLatitude());
         String curretnLon = String.valueOf(gpsTracker.getLongitude());

        //String currentLat = String.valueOf(6.217660);
        //String curretnLon = String.valueOf(-75.564220);

        LatLng currentUserLatLon = new LatLng(Double.parseDouble(currentLat), Double.parseDouble(curretnLon));

        this.googleMap.clear();
        if (currentUserLatLon != null) {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.current_location);
            Marker marker = this.googleMap.addMarker(new MarkerOptions()
                    .position(currentUserLatLon)
                    .title("")
                    .icon(icon)
                    .alpha(1.0f));
        }

        GetListObject storeObj = storeArrayList.get(position);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.other_location);
         latitude = storeObj.getLatitude().replace(",",".").trim();
         longitude = storeObj.getLongitude().replace(",",".").trim();
        LatLng markerLat = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        Marker marker = this.googleMap.addMarker(new MarkerOptions()
                .position(markerLat)
                .title(Html.fromHtml(storeObj.getTitle()).toString())
                .snippet(storeObj.getAddress())
                .icon(icon)
                .alpha(1.0f));
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLat, 12.0f));

        if (currentUserLatLon != null && markerLat != null) {
            String url = getDirectionsUrl(currentUserLatLon, markerLat);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            if (result != null && result.size() > 0) {
                ArrayList points = null;
                PolylineOptions lineOptions = null;
                MarkerOptions markerOptions = new MarkerOptions();
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList();
                    lineOptions = new PolylineOptions();

                    List<HashMap<String, String>> path = result.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    lineOptions.addAll(points);
                    lineOptions.width(8);
                    lineOptions.color(getResources().getColor(R.color.colorPrimary));
                    lineOptions.geodesic(true);
                }


                Polyline polyline = googleMap.addPolyline(lineOptions);
                List<PatternItem> pattern = Arrays.<PatternItem>asList(
                        new Dash(30), new Gap(20));
                polyline.setPattern(pattern);
            }
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyAajDW81YlRzoY0PPXBlSUxchAJ7FpBQIw";


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private void startAnim(ArrayList<LatLng> routeList) {
        if (googleMap != null && routeList.size() > 1) {
            MapAnimator.getInstance().animateRoute(googleMap, routeList);
        } else {
            Log.d("anim", "map not ready");
//            Toast.makeText(this, "Map not ready", Toast.LENGTH_LONG).show();
        }
    }
}

