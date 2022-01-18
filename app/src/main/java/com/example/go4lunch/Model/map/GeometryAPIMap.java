package com.example.go4lunch.Model.map;

import com.google.geo.type.Viewport;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GeometryAPIMap implements Serializable {
    @SerializedName("location")
    @Expose
    private LocationAPIMap location;

    @SerializedName("viewport")
    @Expose
    private Viewport viewport;

    public void setLocation(LocationAPIMap location) {
        this.location = location;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public LocationAPIMap getLocation() {
        return location;
    }

    public Viewport getViewport() {
        return viewport;
    }
}
