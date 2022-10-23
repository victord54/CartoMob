package fr.victord54.cartomob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.Building;
import fr.victord54.cartomob.models.CartoMob;

public class RoomActivity extends AppCompatActivity {
    private final static String LOG_TAG = RoomActivity.class.getSimpleName();
    public static final int RESULT_CODE_ROOM = 1;
    private CartoMob cartoMob;
    int iBuilding;
    int iRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        TextView name = findViewById(R.id.roomActivity_name_of_room);

        cartoMob = (CartoMob) getIntent().getSerializableExtra("cartoMob");
        iBuilding = getIntent().getIntExtra("iBuilding", 0);
        iRoom = getIntent().getIntExtra("iRoom", 0);
        name.setText(cartoMob.getBuilding(iBuilding).getRoom(iRoom).getName());
    }

    @Override
    public void onBackPressed() {
        Intent reply = new Intent();
        reply.putExtra("cartoMob", cartoMob);
        setResult(RESULT_CODE_ROOM, reply);
        Log.d(LOG_TAG, "On renvoie les données");
        super.onBackPressed();
    }
}