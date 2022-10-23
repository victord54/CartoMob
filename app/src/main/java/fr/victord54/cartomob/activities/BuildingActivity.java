package fr.victord54.cartomob.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.CartoMob;
import fr.victord54.cartomob.models.Room;

public class BuildingActivity extends AppCompatActivity {
    private final static String LOG_TAG = BuildingActivity.class.getSimpleName();
    public final static int RESULT_CODE_BUILDING = 0;
    private CartoMob cartoMob;
    private TextView name;
    private TextView list;
    private Button addRoom;
    private Button showBuilding;
    private int iBuilding;

    final ActivityResultLauncher<Intent> newRoomLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RoomActivity.RESULT_CODE_ROOM) {
            if (result.getData() != null) {
                cartoMob = (CartoMob) result.getData().getSerializableExtra("cartoMob");
                Log.d(LOG_TAG, "Données reçues");
                Log.d(LOG_TAG, "cartoMob : " + cartoMob.toString());
            }
        } else {
            Log.d(LOG_TAG, "Aucune donnée reçue");
        }
    });

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        name = findViewById(R.id.building_name_text);
        cartoMob = (CartoMob) getIntent().getSerializableExtra("cartoMob");
        iBuilding = getIntent().getIntExtra("iBuilding", 0);
        Log.d(LOG_TAG, "iBuilding = " + iBuilding);
        name.setText(cartoMob.getBuilding(iBuilding).getName());
        addRoom = findViewById(R.id.building_addRoom_btn);
        showBuilding = findViewById(R.id.building_showBuilding_btn);
        list = findViewById(R.id.room_list);
        list.setText(cartoMob.getBuilding(iBuilding).toString());

        addRoom.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.addRoom_header_dialog);

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", (dialog, which) -> {
                Intent intent = new Intent(BuildingActivity.this, RoomActivity.class);
                cartoMob.getBuilding(iBuilding).addRoom(new Room(input.getText().toString()));
                intent.putExtra("cartoMob", cartoMob);
                intent.putExtra("iBuilding", iBuilding);
                intent.putExtra("iRoom", cartoMob.getBuilding(iBuilding).getNbRooms() - 1);
                newRoomLauncher.launch(intent);
            });
            builder.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showBuilding.setEnabled(!cartoMob.getBuilding(iBuilding).isEmpty());
        //list.setText(cartoMob.getBuilding(iBuilding).toString());
    }

    @Override
    public void onBackPressed() {
        Intent reply = new Intent();
        reply.putExtra("cartoMob", cartoMob);
        setResult(RESULT_CODE_BUILDING, reply);
        Log.d(LOG_TAG, "On renvoie les données");
        super.onBackPressed();
    }
}