package com.example.comisariaapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ConfirmarUbicacionDialog extends DialogFragment implements
        android.view.View.OnClickListener, OnMapReadyCallback {
    public Dialog d;
    private GoogleMap mMap;
    private Double Lat,Long;
    private String Address;
    private TextView myAddress;
    private Button btnSeleccionarUbicacion,btnCambiarUbicacion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Lat = getArguments().getDouble("lat");
        Long = getArguments().getDouble("long");
        Address = getArguments().getString("address");

    }

    private MapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_confirmar_ubicacion, container, false);
        myAddress = (TextView) v.findViewById(R.id.myAddress);
        btnSeleccionarUbicacion = (Button) v.findViewById(R.id.btnSeleccionarUbicacion);
        btnCambiarUbicacion = (Button) v.findViewById(R.id.btnCambiarUbicacion);


        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapp);
        mapFragment.getMapAsync(this);
        // Toast.makeText(getActivity(),mNum,Toast.LENGTH_LONG).show();

        btnSeleccionarUbicacion.setOnClickListener(v1 -> {
            Toast.makeText(getActivity(), myAddress.getText().toString(), Toast.LENGTH_LONG).show();
            Intent i = new Intent();
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
            dismiss();
            i.putExtra("latitud", this.Lat);
            i.putExtra("longitud", this.Long);
            i.putExtra("address", this.Address);
            this.getActivity().setResult(Activity.RESULT_OK, i);
            this.getActivity().finish();
        });
        btnCambiarUbicacion.setOnClickListener(v12 -> {
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
            dismiss();
        });
        getDialog().setCanceledOnTouchOutside(true);
        return v;

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        getFragmentManager().beginTransaction().remove(mapFragment).commit();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dismiss();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myAddress.setText(Address);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(Lat, Long));

        markerOptions.title(Address);
        mMap.clear();
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                new LatLng(Lat, Long), 16f);
        mMap.animateCamera(location);
        mMap.addMarker(markerOptions);
        Log.d("status", "success");
    }
}
