package fr.victord54.cartomob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.Building;
import fr.victord54.cartomob.models.CartoMob;

public class RoomActivity extends AppCompatActivity implements SensorEventListener {
    private final static String LOG_TAG = RoomActivity.class.getSimpleName();
    public static final int RESULT_CODE_ROOM = 123;

    private CartoMob cartoMob;
    private int iRoom;
    private TextView name;
    private ImageView compass;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magneticField;

    private final float[] gravitiy = new float[3];
    private final float[] geomagnetic = new float[3];
    private float azimuth = 0f;
    private float correctAzimuth = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        name = findViewById(R.id.roomActivity_name_of_room);
        compass = findViewById(R.id.roomActivity_compass);

        cartoMob = (CartoMob) getIntent().getSerializableExtra("cartoMob");
        iRoom = getIntent().getIntExtra("iRoom", 0);

        name.setText(cartoMob.getRoom(iRoom).getName());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent reply = new Intent();
        reply.putExtra("cartoMob", cartoMob);
        setResult(RESULT_CODE_ROOM, reply);
        Log.d(LOG_TAG, "On renvoie les données");
        super.onBackPressed();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravitiy[0] = alpha * gravitiy[0] + (1 - alpha) * event.values[0];
            gravitiy[1] = alpha * gravitiy[1] + (1 - alpha) * event.values[1];
            gravitiy[2] = alpha * gravitiy[2] + (1 - alpha) * event.values[2];
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic[0] = alpha * geomagnetic[0] + (1 - alpha) * event.values[0];
            geomagnetic[1] = alpha * geomagnetic[1] + (1 - alpha) * event.values[1];
            geomagnetic[2] = alpha * geomagnetic[2] + (1 - alpha) * event.values[2];
        }

        float[] R = new float[9];
        float[] I = new float[9];
        if (SensorManager.getRotationMatrix(R, I, gravitiy, geomagnetic)) {
            float[] orientation = new float[3];
            SensorManager.getOrientation(R, orientation);
            azimuth = (float) Math.toDegrees(orientation[0]);
            azimuth = (azimuth + 360) % 360;

//            Log.d("Azimuth", "azimuth : " + azimuth);

            Animation animation = new RotateAnimation(-correctAzimuth, -azimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            correctAzimuth = azimuth;
            animation.setDuration(500);
            animation.setRepeatCount(0);
            animation.setFillAfter(true);

            compass.startAnimation(animation);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}