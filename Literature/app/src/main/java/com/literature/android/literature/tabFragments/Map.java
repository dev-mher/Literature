package com.literature.android.literature.tabFragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.literature.android.literature.R;
import com.literature.android.literature.adapters.PagerAdapter;

/**
 * Created by mher on 3/24/17.
 */

public class Map extends Fragment implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap map;

    public static Map newInstance(String title) {
        Map mapFragment = new Map();
        Bundle args = new Bundle();
        args.putString(PagerAdapter.TAB_FRAGMENT_PAGE_TITLE, title);
        mapFragment.setArguments(args);
        return mapFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_tab_layout, null);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng armenia = new LatLng(39.800000, 45.000000);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(armenia, (float) 7.3));
        LatLng h_tumanyan = new LatLng(40.9599027, 44.6315716);
        LatLng p_sevak = new LatLng(39.8210616, 45.0324869);
        Bitmap tumanyanIcon = BitmapFactory.decodeResource(getResources(), R.drawable.h_tumanyan_marker);
        BitmapDescriptor tumanyanMarker = BitmapDescriptorFactory.fromBitmap(tumanyanIcon);
        Bitmap sevakIcon = BitmapFactory.decodeResource(getResources(), R.drawable.p_sevak_marker);
        BitmapDescriptor sevakMarker = BitmapDescriptorFactory.fromBitmap(sevakIcon);
        map.addMarker(new MarkerOptions().position(h_tumanyan).title("Հովհաննես Թումանյան").icon(tumanyanMarker));
        map.addMarker(new MarkerOptions().position(p_sevak).title("Պարույր Սևակ").icon(sevakMarker));
    }
}
