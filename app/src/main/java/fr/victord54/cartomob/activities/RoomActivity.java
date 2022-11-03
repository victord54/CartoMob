package fr.victord54.cartomob.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.CartoMob;
import fr.victord54.cartomob.models.Room;
import fr.victord54.cartomob.models.Wall;
import fr.victord54.cartomob.tools.Save;

public class RoomActivity extends AppCompatActivity implements SensorEventListener {
    private final static String LOG_TAG = RoomActivity.class.getSimpleName();
    public static final int RESULT_CODE_ROOM = 123;

    private CartoMob cartoMob;
    private Room room;
    private int iRoom;
    private String nsew;

    private TextView name;
    private ImageView compass;
    private TextView direction;
    private Button addPhoto;
    private TextView infoPhoto;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magneticField;

    private final float[] gravitiy = new float[3];
    private final float[] geomagnetic = new float[3];
    private float azimuth = 0f;
    private float correctAzimuth = 0f;

    final ActivityResultLauncher<Intent> wallLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == WallActivity.RESULT_CODE_WALL) {
            if (result.getData() != null) {
                cartoMob = (CartoMob) result.getData().getSerializableExtra("cartoMob");
                Log.d(LOG_TAG, "Données reçues !");
            }
        } else {
            Log.d(LOG_TAG, "Aucune donnée reçue");
        }
        Log.d("ModelContent", cartoMob.toString());
    });

    final ActivityResultLauncher<Intent> photoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Bundle extras = null;
        if (result.getData() != null) {
            extras = result.getData().getExtras();
        }
        if (extras != null) {
            Bitmap bmp = (Bitmap) extras.get("data");
            Save.getInstance().saveBmpToStorage(this, bmp, "img_" + cartoMob.getName() + "_" + room.getName() + "_" + nsew);
            room.addWall(nsew, new Wall(nsew, "img_" + cartoMob.getName() + "_" + room.getName() + "_" + nsew));
            Intent sendData = new Intent(this, WallActivity.class);
            sendData.putExtra("cartoMob", cartoMob);
            sendData.putExtra("iRoom", iRoom);
            sendData.putExtra("orientation", nsew);
            wallLauncher.launch(sendData);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        name = findViewById(R.id.roomActivity_name_of_room);
        compass = findViewById(R.id.roomActivity_compass);
        direction = findViewById(R.id.roomActivity_orientation);
        addPhoto = findViewById(R.id.roomActivity_add_photo);
        infoPhoto = findViewById(R.id.roomActivity_info_photo);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RoomActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        cartoMob = (CartoMob) getIntent().getSerializableExtra("cartoMob");
        iRoom = getIntent().getIntExtra("iRoom", 0);
        room = cartoMob.getRoom(iRoom);

        name.setText(room.getName());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        addPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                photoLauncher.launch(intent);
            }
        });
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

            Log.d("Azimuth", "azimuth : " + azimuth);

            Animation animation = new RotateAnimation(-correctAzimuth, -azimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            correctAzimuth = azimuth;
            animation.setDuration(500);
            animation.setRepeatCount(0);
            animation.setFillAfter(true);

            compass.startAnimation(animation);

            if (correctAzimuth > 330.f || correctAzimuth < 30.f) {
                direction.setText(fr.victord54.cartomob.R.string.to_north);
                nsew = "N";
                verifyPhoto();
                addPhoto.setEnabled(true);
            } else if (correctAzimuth > 240.f && correctAzimuth < 300.f) {
                direction.setText(fr.victord54.cartomob.R.string.to_west);
                nsew = "W";
                verifyPhoto();
                addPhoto.setEnabled(true);
            } else if (correctAzimuth > 150.f && correctAzimuth < 210.f) {
                direction.setText(fr.victord54.cartomob.R.string.to_south);
                nsew = "S";
                verifyPhoto();
                addPhoto.setEnabled(true);
            } else if (correctAzimuth > 60.f && correctAzimuth < 120.f) {
                direction.setText(fr.victord54.cartomob.R.string.to_east);
                nsew = "E";
                verifyPhoto();
                addPhoto.setEnabled(true);
            } else {
                direction.setText(fr.victord54.cartomob.R.string.direction_unknown);
                infoPhoto.setText("");
                addPhoto.setEnabled(false);
            }
        }
    }

    private void verifyPhoto() {
        if (room.isWallExist(nsew))
            infoPhoto.setText(R.string.roomActivity_photo_already_added);
        else
            infoPhoto.setText("");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}