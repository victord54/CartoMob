package fr.victord54.cartomob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.Building;
import fr.victord54.cartomob.models.CartoMob;
import fr.victord54.cartomob.models.Room;

public class BuildingActivity extends AppCompatActivity {
    private final static String LOG_TAG = Building.class.getSimpleName();
    public final static int RESULT_CODE_BUILDING = 2;
    private CartoMob cartoMob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        TextView name = findViewById(R.id.building_name_text);
        cartoMob = (CartoMob) getIntent().getSerializableExtra("cartoMob");
        name.setText(cartoMob.getBuilding(getIntent().getIntExtra("i", 0)).getName());

        Button addRoom = findViewById(R.id.building_addRoom_btn);
        Button showBuilding = findViewById(R.id.building_showBuilding_btn);

        addRoom.setOnClickListener(view -> {
            Intent intent = new Intent(BuildingActivity.this, RoomActivity.class);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.addRoom_header_dialog);

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", (dialog, which) -> {
                cartoMob.getBuilding(0).addRoom(new Room(input.getText().toString()));
                intent.putExtra("cartoMob", cartoMob);
                startActivity(intent);
            });
            builder.show();
        });
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