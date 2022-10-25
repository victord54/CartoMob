package fr.victord54.cartomob.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.CartoMob;
import fr.victord54.cartomob.models.Room;
import fr.victord54.cartomob.tools.Save;
import fr.victord54.cartomob.views.CustomDialogName;
import fr.victord54.cartomob.views.CustomDialogSaveChooser;

public class MainActivity extends AppCompatActivity {
    public final static String LOG_TAG = MainActivity.class.getSimpleName();
    private CartoMob cartoMob;

    private TextView nameBuilding;
    private Button newRoom;
    private Button loadRoom;
    private Button loadFile;
    private Button writeFile;

    final ActivityResultLauncher<Intent> newRoomLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RoomActivity.RESULT_CODE_ROOM) {
            if (result.getData() != null) {
                cartoMob = (CartoMob) result.getData().getSerializableExtra("cartoMob");
                Log.d(LOG_TAG, "Données reçues !");
                Log.d(LOG_TAG, "cartoMob : "+cartoMob.toString());
            }
        } else {
            Log.d(LOG_TAG, "Aucune donnée reçue");
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameBuilding = findViewById(R.id.name_of_building);
        newRoom = findViewById(R.id.main_new_room_btn);
        loadRoom = findViewById(R.id.main_load_room_btn);
        loadFile = findViewById(R.id.main_load_save);
        writeFile = findViewById(R.id.main_write_save);

        if (cartoMob == null)
            cartoMob = new CartoMob();

        if (cartoMob.isEmpty()) {
            Log.i(LOG_TAG, "cartoMob est vide");
            loadRoom.setEnabled(false);
        }

        CustomDialogName.NameListener listenerCartoName = new CustomDialogName.NameListener() {
            @Override
            public void nameTyped(String name) {
                cartoMob.setName(name);
                nameBuilding.setText(cartoMob.getName());
            }
        };
        final CustomDialogName cartoName = new CustomDialogName(this, listenerCartoName);
        cartoName.show();

        newRoom.setOnClickListener(view -> {
            CustomDialogName.NameListener listenerRoomName = new CustomDialogName.NameListener() {
                @Override
                public void nameTyped(String name) {
                    cartoMob.addRoom(new Room(name));
                    Intent sendData = new Intent(MainActivity.this, RoomActivity.class);
                    sendData.putExtra("cartoMob", cartoMob);
                    sendData.putExtra("i", cartoMob.getSize()-1);
                    newRoomLauncher.launch(sendData);
                }
            };
            final CustomDialogName roomName = new CustomDialogName(this, listenerRoomName);
            roomName.show();
        });

        loadRoom.setOnClickListener(view -> {
            Toast.makeText(this, "On devrait ouvrir une fenêtre avec la liste des pièces", Toast.LENGTH_SHORT).show();
        });

        writeFile.setOnClickListener(view -> Save.getInstance().saveToStorage(this, cartoMob, cartoMob.getName()));

        loadFile.setOnClickListener(view -> {
//            cartoMob = /* Le résultat de la recyclerView */
            String path = getFilesDir().getPath();
            File directory = new File(path);
            ArrayList<File> files = (ArrayList<File>) Arrays.asList(Objects.requireNonNull(directory.listFiles()));
            for (File f: files) {
                Log.d("Files", f.getName());
            }

            CustomDialogSaveChooser.FileListener listenerFileName = new CustomDialogSaveChooser.FileListener() {
                @Override
                public void nameFileTyped(String name) {

                }
            };
            final CustomDialogSaveChooser dialogSaveChooser = new CustomDialogSaveChooser(this, listenerFileName);
            dialogSaveChooser.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cartoMob == null)
            cartoMob = new CartoMob();
        loadRoom.setEnabled(!cartoMob.isEmpty());
    }
}