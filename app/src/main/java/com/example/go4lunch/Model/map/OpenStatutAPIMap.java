package com.example.go4lunch.Model.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OpenStatutAPIMap implements Serializable {

    @SerializedName("open_now")
    @Expose
    private Boolean open_now;

    public Boolean getOpen_now() {
        return open_now;
    }

    public void setOpen_now(Boolean open_now) {
        this.open_now = open_now;
    }


}
