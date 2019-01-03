package com.app.D1App;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.D1App.API.ListaToAdapter;
import com.app.D1App.API.ServiceGenerator;
import com.app.D1App.API.TiendasAppService;
import com.app.D1App.API.TiendasToAdapter;
import com.app.D1App.Adapter.ListaAdapter;
import com.app.D1App.Adapter.TeindasAdapter;
import com.app.D1App.ApiObject.GetListObject;
import com.app.D1App.ApiResponse.GetFilterElementResponse;
import com.app.D1App.ApiResponse.GetListingResponse;
import com.app.D1App.MapRoute.DirectionsJSONParser;
import com.app.D1App.MapRoute.MapAnimator;
import com.app.D1App.Utils.AppCommon;
import com.app.D1App.Utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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


public class TiendasFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.dropDown1)
    ImageView dropDown1;

    @BindView(R.id.dropDown2)
    ImageView dropDown2;

    @BindView(R.id.dropDown3)
    ImageView dropDown3;

    @BindView(R.id.departmenRecyclerView)
    RecyclerView departmenRecyclerView;

    @BindView(R.id.municipioRecyclerView)
    RecyclerView municipioRecyclerView;

    @BindView(R.id.barrioRecyclerView)
    RecyclerView barrioRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.selectedBarrioTextView)
    TextView selectedBarrioTextView;

    @BindView(R.id.selectedDepartmentTextView)
    TextView selectedDepartmentTextView;

    @BindView(R.id.selectedmunicipioTextView)
    TextView selectedmunicipioTextView;

    /* ---------------second screen Layouts--------*/

    @BindView(R.id.listaMapaLayout)
    RelativeLayout listaMapaLayout;

    @BindView(R.id.list)
    Button list;

    @BindView(R.id.map)
    Button map;

    @BindView(R.id.mapLayout)
    RelativeLayout mapLayout;

    @BindView(R.id.listRecyclerView)
    RecyclerView listRecyclerView;

    @BindView(R.id.buscarLayout)
    LinearLayout buscarLayout;

    @BindView(R.id.markerClickLayout)
    RelativeLayout markerClickLayout;

    ListaAdapter listaAdapter;
    Call call;
    double latitude;
    double longitude;
    GPSTracker gpsTracker;
    private GoogleMap googleMap;
    ArrayList<GetListObject> storeArrayList;
    ArrayList<Marker> markerArrayList = new ArrayList<>();

    String barrioSelData = "", municioSelData = "", departData = "";
    Marker selectedMarker;

    ListaToAdapter listaToAdapterInterface = new ListaToAdapter() {
        @Override
        public void respond(int position, String name, String distance, String latitude, String longitude) {
            setOnClickAdapter(position, latitude, longitude);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tiendas, container, false);
        ButterKnife.bind(this, view);
        gpsTracker = new GPSTracker(getActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);

        departmenRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        municipioRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        barrioRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        listRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        departmenRecyclerView.setNestedScrollingEnabled(false);
        municipioRecyclerView.setNestedScrollingEnabled(false);
        barrioRecyclerView.setNestedScrollingEnabled(false);
        listRecyclerView.setNestedScrollingEnabled(false);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();


        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
//        currentLocation = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
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

    boolean flag1 = false;
    boolean flag2 = false;
    boolean flag3 = false;

    @OnClick(R.id.departmentoLayout)
    void setDepartmentoLayout() {
        if (flag1) {
            dropDown1.setRotation(0);
            dropDown2.setRotation(0);
            dropDown3.setRotation(0);
            departmenRecyclerView.setVisibility(View.GONE);
            municipioRecyclerView.setVisibility(View.GONE);
            barrioRecyclerView.setVisibility(View.GONE);
            flag1 = false;
            flag2 = false;
            flag3 = false;
        } else {
            dropDown1.setRotation(90);
            dropDown2.setRotation(0);
            dropDown3.setRotation(0);
            departmenRecyclerView.setVisibility(View.VISIBLE);
            municipioRecyclerView.setVisibility(View.GONE);
            barrioRecyclerView.setVisibility(View.GONE);
            flag1 = true;
            flag2 = false;
            flag3 = false;
            if (departData.equals("")) {
                callingFilterElementAPI("", "D", departmenRecyclerView);
            }
        }
    }


    @OnClick(R.id.municipioLayout)
    void setMunicipioLayout() {
        if (flag2) {
            dropDown1.setRotation(0);
            dropDown2.setRotation(0);
            dropDown3.setRotation(0);
            departmenRecyclerView.setVisibility(View.GONE);
            municipioRecyclerView.setVisibility(View.GONE);
            barrioRecyclerView.setVisibility(View.GONE);
            flag1 = false;
            flag2 = false;
            flag3 = false;
        } else {
            dropDown1.setRotation(0);
            dropDown2.setRotation(90);
            dropDown3.setRotation(0);
            departmenRecyclerView.setVisibility(View.GONE);
            municipioRecyclerView.setVisibility(View.VISIBLE);
            barrioRecyclerView.setVisibility(View.GONE);
            if (municioSelData.equals("")) {
                callingFilterElementAPI(departData, "M", municipioRecyclerView);
            }
            flag1 = false;
            flag2 = true;
            flag3 = false;
        }
    }

    @OnClick(R.id.barrioLayout)
    void setBarrioLayout() {
        if (flag3) {
            dropDown1.setRotation(0);
            dropDown2.setRotation(0);
            dropDown3.setRotation(0);
            departmenRecyclerView.setVisibility(View.GONE);
            municipioRecyclerView.setVisibility(View.GONE);
            barrioRecyclerView.setVisibility(View.GONE);
            flag1 = false;
            flag2 = false;
            flag3 = false;
        } else {
            dropDown1.setRotation(0);
            dropDown2.setRotation(0);
            dropDown3.setRotation(90);
            departmenRecyclerView.setVisibility(View.GONE);
            municipioRecyclerView.setVisibility(View.GONE);
            barrioRecyclerView.setVisibility(View.VISIBLE);
            if (barrioSelData.equals("")) {
                callingFilterElementAPI(municioSelData, "B", barrioRecyclerView);
            }
            flag1 = false;
            flag2 = false;
            flag3 = true;
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


    void setBackBtn() {
        scrollView.setVisibility(View.VISIBLE);
        listaMapaLayout.setVisibility(View.GONE);
    }


    void setOnClickAdapter(int position, String latitude, String longitude) {
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
        latitude = storeObj.getLatitude().replace(",", ".").trim();
        longitude = storeObj.getLongitude().replace(",", ".").trim();
        LatLng markerLat = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        Marker marker = this.googleMap.addMarker(new MarkerOptions()
                .position(markerLat)
                .title(Html.fromHtml(storeObj.getTitle()).toString())
                .snippet(storeObj.getAddress())
                .icon(icon)
                .alpha(1.0f));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserLatLon));
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLon, 8.0f));

        if (currentUserLatLon != null && markerLat != null) {
            String url = getDirectionsUrl(currentUserLatLon, markerLat);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        }
    }

    void callingFilterElementAPI(String parent, final String section, final RecyclerView recyclerView) {
        AppCommon.getInstance(getActivity()).setNonTouchableFlags(getActivity());
        if (AppCommon.getInstance(getActivity()).isConnectingToInternet(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            TiendasAppService tiendasAppService = ServiceGenerator.createService(TiendasAppService.class);
            call = tiendasAppService.GetFilterElement(parent, section);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        if (response.body() != null) {
                            GetFilterElementResponse filterElementResponse = (GetFilterElementResponse) response.body();
                            assert filterElementResponse != null;
                            if (filterElementResponse.getSuccess() == 1) {
                                TeindasAdapter teindasAdapter = new TeindasAdapter(getActivity(), filterElementResponse.getResult(), tiendasToAdapter, section);
                                recyclerView.setAdapter(teindasAdapter);
                            } else {
                                AppCommon.showDialog(getActivity(), filterElementResponse.getError());
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
                        AppCommon.showDialog(getActivity(), "API error");
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

    void callingFilterAPI(double latitude, double longitude, String departmento, String municipo, String barrio) {
        AppCommon.getInstance(getActivity()).setNonTouchableFlags(getActivity());
        if (AppCommon.getInstance(getActivity()).isConnectingToInternet(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            TiendasAppService tiendasAppService = ServiceGenerator.createService(TiendasAppService.class);
            call = tiendasAppService.GetFilter(latitude, longitude, departmento, municipo, barrio);
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
                                listaAdapter = new ListaAdapter(getActivity(), storeArrayList, listaToAdapterInterface);
                                listRecyclerView.setAdapter(listaAdapter);
                                scrollView.setVisibility(View.GONE);
                                listaMapaLayout.setVisibility(View.VISIBLE);
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
                        AppCommon.showDialog(getActivity(), "API error");
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

    TiendasToAdapter tiendasToAdapter = new TiendasToAdapter() {
        @Override
        public void OnClickData(int position, String name, String comingFrom) {
            switch (comingFrom) {
                case "D":
                    if (!name.equals(departData)) {
                        municioSelData = "";
                        barrioSelData = "";
                        selectedmunicipioTextView.setText(getResources().getString(R.string.selectionOfMunicipio));
                        selectedBarrioTextView.setText(getResources().getString(R.string.selectionOfBarrio));
                        if (municipioRecyclerView.getAdapter() != null) {
                            ((TeindasAdapter) municipioRecyclerView.getAdapter()).cleanData();
                        }
                        if (barrioRecyclerView.getAdapter() != null) {
                            ((TeindasAdapter) barrioRecyclerView.getAdapter()).cleanData();
                        }
                    }
                    departData = name;
                    selectedDepartmentTextView.setText(departData);
                    break;
                case "M":
                    municioSelData = name;
                    selectedmunicipioTextView.setText(municioSelData);
                    break;
                case "B":
                    barrioSelData = name;
                    selectedBarrioTextView.setText(barrioSelData);
                    break;
            }
            if (departData.isEmpty() && municioSelData.isEmpty() && barrioSelData.isEmpty()) {
                Toast.makeText(getActivity(),
                        "Please select above options", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @OnClick(R.id.buscarLayout)
    void setOnSearchLayout() {
        if (!departData.isEmpty() && !municioSelData.isEmpty() && !barrioSelData.isEmpty()) {
            String latitude = AppCommon.getInstance(getActivity()).getLatitude();
            String longiitude = AppCommon.getInstance(getActivity()).getLongitude();
            if (!latitude.isEmpty() && !longiitude.isEmpty()) {
                callingFilterAPI(Double.parseDouble(latitude), Double.parseDouble(longiitude), departData, municioSelData, barrioSelData);
            } else {
                callingFilterAPI(gpsTracker.getLatitude(), gpsTracker.getLongitude(), departData, municioSelData, barrioSelData);
            }
        } else {
            Toast.makeText(getActivity(),
                    "Please select above options", Toast.LENGTH_SHORT).show();
        }
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

                    String latitude = storeArrayList.get(i).getLatitude().replace(",", ".").trim();
                    String longitude = storeArrayList.get(i).getLongitude().replace(",", ".").trim();

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

}