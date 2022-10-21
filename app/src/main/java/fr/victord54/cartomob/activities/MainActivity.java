package fr.victord54.cartomob.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.Building;
import fr.victord54.cartomob.models.CartoMob;
import fr.victord54.cartomob.views.BuildingAdapter;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private CartoMob cartoMob;

    private Button newBuilding;
    private TextView listBuildings;

    final ActivityResultLauncher<Intent> newBuildingLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == BuildingActivity.RESULT_CODE_BUILDING) {
            if (result.getData() != null) {
                cartoMob = (CartoMob) result.getData().getSerializableExtra("cartoMob");
                Log.d(LOG_TAG, "Données reçues");
                Log.d(LOG_TAG, "cartoMob : "+cartoMob.toString());
            }
        } else {
            Log.d(LOG_TAG, "Aucune donnée reçue");
        }
    });

    final ActivityResultLauncher<Intent> loadBuildingLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newBuilding = findViewById(R.id.main_new_btn);
        listBuildings = findViewById(R.id.main_list_btn);
        if (cartoMob == null)
            cartoMob = new CartoMob();

        if (cartoMob.isEmpty()) {
            Log.i(LOG_TAG, "CartoMob est vide");
            listBuildings.setEnabled(false);
        }

        newBuilding.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.addBuilding_header_dialog);

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", (dialog, which) -> {
                cartoMob.setBuilding(new Building(input.getText().toString()));
                Intent intent = new Intent(MainActivity.this, BuildingActivity.class);
                intent.putExtra("cartoMob", cartoMob);
                intent.putExtra("i", cartoMob.getBuildings().size()-1);

                newBuildingLauncher.launch(intent);
            });
            builder.show();
        });

        listBuildings.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.listBuilding_header_dialog);

            final RecyclerView list = new RecyclerView(this);
            list.setAdapter(new BuildingAdapter(cartoMob));
            list.setLayoutManager(new LinearLayoutManager(this));
            builder.setView(list);

            // Set up the buttons
            builder.setNegativeButton("Annuler", (dialog, which) -> {
                dialog.cancel();
            });
            builder.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cartoMob == null)
            cartoMob = new CartoMob();

        listBuildings.setEnabled(!cartoMob.isEmpty());
    }
}