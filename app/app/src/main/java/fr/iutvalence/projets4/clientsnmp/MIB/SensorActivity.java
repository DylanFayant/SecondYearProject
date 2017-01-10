package fr.iutvalence.projets4.clientsnmp.MIB;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;

import org.snmp4j.smi.OID;


/**
 * Created by Thundermist on 09/01/17.
 */

public class SensorActivity extends Activity implements SensorEventListener, MIBComposite, MIBElement<Float> {
    private SensorManager mSensor;
    private Sensor mTemperature;
    private float temperature;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get an instance of the sensor service, and use that to get an instance of
        // a temperature sensor.
        mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        mTemperature = mSensor.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        temperature = event.values[0];
        // Do something with this sensor data.
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensor.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensor.unregisterListener(this);
    }

    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    @Override
    public Float getValue() {
        return temperature;
    }
}
