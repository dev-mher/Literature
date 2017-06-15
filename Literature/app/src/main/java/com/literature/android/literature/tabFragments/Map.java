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
        LatLng h_shiraz = new LatLng(40.826682, 43.810191);
        LatLng v_teryan = new LatLng(41.348349, 43.751403);
        LatLng a_isahakyan = new LatLng(40.754942, 43.854823);
        LatLng e_charenc = new LatLng(40.5966918,43.0667679);
        LatLng s_kaputikyan = new LatLng(40.153306,44.3484816);
        LatLng h_sahyan = new LatLng(39.4153313,46.1204145);
        Bitmap tumanyanIcon = BitmapFactory.decodeResource(getResources(), R.drawable.h_tumanyan_marker);
        BitmapDescriptor tumanyanMarker = BitmapDescriptorFactory.fromBitmap(tumanyanIcon);
        Bitmap sevakIcon = BitmapFactory.decodeResource(getResources(), R.drawable.p_sevak_marker);
        BitmapDescriptor sevakMarker = BitmapDescriptorFactory.fromBitmap(sevakIcon);
        Bitmap shirazIcon = BitmapFactory.decodeResource(getResources(), R.drawable.h_shiraz_marker);
        BitmapDescriptor shirazMarker = BitmapDescriptorFactory.fromBitmap(shirazIcon);
        Bitmap teryanIcon = BitmapFactory.decodeResource(getResources(), R.drawable.v_teryan_marker);
        BitmapDescriptor teryanMarker = BitmapDescriptorFactory.fromBitmap(teryanIcon);
        Bitmap isahakyanIcon = BitmapFactory.decodeResource(getResources(), R.drawable.a_isahakyan_marker);
        BitmapDescriptor isahakyanMarker = BitmapDescriptorFactory.fromBitmap(isahakyanIcon);
        Bitmap charencIcon = BitmapFactory.decodeResource(getResources(), R.drawable.e_charenc_marker);
        BitmapDescriptor charencMarker = BitmapDescriptorFactory.fromBitmap(charencIcon);
        Bitmap kaputikyanIcon = BitmapFactory.decodeResource(getResources(), R.drawable.s_kaputikyan_marker);
        BitmapDescriptor kaputikyanMarker = BitmapDescriptorFactory.fromBitmap(kaputikyanIcon);
        Bitmap sahyanIcon = BitmapFactory.decodeResource(getResources(), R.drawable.h_sahyan_marker);
        BitmapDescriptor sahyanMarker = BitmapDescriptorFactory.fromBitmap(sahyanIcon);
        map.addMarker(new MarkerOptions().position(h_tumanyan).title(getString(R.string.hovhannes_tumanyan)).icon(tumanyanMarker));
        map.addMarker(new MarkerOptions().position(p_sevak).title(getString(R.string.paruyr_sevak)).icon(sevakMarker));
        map.addMarker(new MarkerOptions().position(h_shiraz).title(getString(R.string.hovhannes_shiraz)).icon(shirazMarker));
        map.addMarker(new MarkerOptions().position(v_teryan).title(getString(R.string.vahan_teryan)).icon(teryanMarker));
        map.addMarker(new MarkerOptions().position(a_isahakyan).title(getString(R.string.avetiq_isahakyan)).icon(isahakyanMarker));
        map.addMarker(new MarkerOptions().position(e_charenc).title(getString(R.string.exishe_charenc)).icon(charencMarker));
        map.addMarker(new MarkerOptions().position(s_kaputikyan).title(getString(R.string.silva_kaputikyan)).icon(kaputikyanMarker));
        map.addMarker(new MarkerOptions().position(h_sahyan).title(getString(R.string.hamo_sahyan)).icon(sahyanMarker));
    }
}
