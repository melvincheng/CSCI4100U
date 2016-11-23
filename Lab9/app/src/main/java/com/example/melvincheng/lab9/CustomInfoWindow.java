package com.example.melvincheng.lab9;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
    private LayoutInflater inflater;

    public CustomInfoWindow(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View popup = inflater.inflate(R.layout.popup, null);

        TextView textView=(TextView)popup.findViewById(R.id.snippet);
        textView.setText(marker.getSnippet());
        return(popup);
    }
}

