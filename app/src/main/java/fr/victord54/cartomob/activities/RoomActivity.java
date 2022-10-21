package fr.victord54.cartomob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.CartoMob;

public class RoomActivity extends AppCompatActivity {

    private CartoMob cartoMob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        TextView name = findViewById(R.id.roomActivity_name_of_room);
        cartoMob = (CartoMob) getIntent().getSerializableExtra("cartoMob");
        name.setText(cartoMob.getBuilding(0).getRoom(0).getName());
    }
}