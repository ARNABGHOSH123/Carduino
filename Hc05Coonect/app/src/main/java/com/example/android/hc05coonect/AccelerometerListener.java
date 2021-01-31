package com.example.android.hc05coonect;

/**
 * Created by ARNAB on 4/9/2018.
 */

public interface AccelerometerListener {

    public void onAccelerationChanged(float x, float y, float z);

    public void onShake(float force);
}
