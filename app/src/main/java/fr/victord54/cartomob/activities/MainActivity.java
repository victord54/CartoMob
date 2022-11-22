package fr.victord54.cartomob.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.CartoMob;
import fr.victord54.cartomob.models.Room;
import fr.victord54.cartomob.tools.Save;
import fr.victord54.cartomob.views.CustomDialogName;
import fr.victord54.cartomob.views.CustomDialogRoomChooser;
import fr.victord54.cartomob.views.CustomDialogSaveChooser;

public class MainActivity extends AppCompatActivity {
    public final static String LOG_TAG = MainActivity.class.getSimpleName();
    private CartoMob cartoMob;

    private TextView nameBuilding;
    private TextView nbRooms;
    private Button newRoom;
    private Button loadRoom;
    private Button loadFile;
    private Button writeFile;
    private Button nameBuildingBtn;
    private Button visit;

    final ActivityResultLauncher<Intent> newRoomLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RoomActivity.RESULT_CODE_ROOM) {
            if (result.getData() != null) {
                cartoMob = (CartoMob) result.getData().getSerializableExtra("cartoMob");
                Log.d(LOG_TAG, "Données reçues !");
                Log.d(LOG_TAG, cartoMob.toString());
            }
        } else {
            Log.d(LOG_TAG, "Aucune donnée reçue");
        }
    });

    final ActivityResultLauncher<Intent> visitLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == VisitActivity.RESULT_CODE_VISIT) {
            if (result.getData() != null) {
                cartoMob = (CartoMob) result.getData().getSerializableExtra("cartoMob");
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameBuilding = findViewById(R.id.main_name_of_building);
        nameBuildingBtn = findViewById(R.id.main_name_of_building_btn);
        nbRooms = findViewById(R.id.main_number_of_rooms);
        newRoom = findViewById(R.id.main_new_room_btn);
        loadRoom = findViewById(R.id.main_load_room_btn);
        loadFile = findViewById(R.id.main_load_save);
        writeFile = findViewById(R.id.main_write_save);
        visit = findViewById(R.id.main_visit_building);

        if (cartoMob == null) cartoMob = new CartoMob();
        writeFile.setEnabled(false);

        if (cartoMob.isEmpty()) {
            Log.i(LOG_TAG, "cartoMob est vide");
            loadRoom.setEnabled(false);
            nbRooms.setText(R.string.main_number_of_rooms_zero);
        } else {
            loadRoom.setEnabled(true);
            nbRooms.setText(getString(R.string.main_number_of_rooms, cartoMob.getSize()));
        }

        nameBuildingBtn.setOnClickListener(view -> setBuildingName());

        newRoom.setOnClickListener(view -> {
            CustomDialogName.NameListener listenerRoomName = name -> {
                if (cartoMob.getIndiceFromRoom(name) != -1) {
                    Toast.makeText(this, "Cette pièce existe déjà", Toast.LENGTH_SHORT).show();
                    return;
                }
                cartoMob.addRoom(new Room(name, "room" + (cartoMob.getSize())));
                Intent sendData = new Intent(MainActivity.this, RoomActivity.class);
                sendData.putExtra("cartoMob", cartoMob);
                sendData.putExtra("iRoom", cartoMob.getSize() - 1);
                newRoomLauncher.launch(sendData);
            };
            final CustomDialogName roomName = new CustomDialogName(this, listenerRoomName, getText(R.string.custom_dialog_name_title_room).toString());
            roomName.show();
        });

        loadRoom.setOnClickListener(view -> {
//            Toast.makeText(this, "On devrait ouvrir une fenêtre avec la liste des pièces", Toast.LENGTH_SHORT).show();
            CustomDialogRoomChooser.RoomListener roomListener = name -> {
                Log.d("RoomChooser", "nom de la room dans main : " + name);
                Intent sendData = new Intent(MainActivity.this, RoomActivity.class);
                sendData.putExtra("cartoMob", cartoMob);
                sendData.putExtra("iRoom", cartoMob.getIndiceFromRoom(name));
                newRoomLauncher.launch(sendData);
            };
            final CustomDialogRoomChooser dialogRoomChooser = new CustomDialogRoomChooser(this, roomListener, cartoMob.getRooms());
            dialogRoomChooser.show();
        });

        writeFile.setOnClickListener(view -> Save.getInstance().saveToStorage(this, cartoMob, cartoMob.getName()));

        loadFile.setOnClickListener(view -> {
//            cartoMob = /* Le résultat de la recyclerView */
            String path = getFilesDir().getPath();
            File directory = new File(path);
            File[] files = directory.listFiles();
            ArrayList<String> fileArrayList = new ArrayList<>();
            if (files != null) {
                for (File f : files) {
                    Log.d("Files", f.getName());
                    if (!f.getName().contains("img_")) fileArrayList.add(f.getName());
                }
            }

            CustomDialogSaveChooser.FileListener listenerFileName = name -> {
                Log.d("FileChooser", "nom du fichier dans main : " + name);
                cartoMob = (CartoMob) Save.getInstance().loadFromStorage(MainActivity.this, name);
                onResume();
            };
            final CustomDialogSaveChooser dialogSaveChooser = new CustomDialogSaveChooser(this, listenerFileName, fileArrayList);
            dialogSaveChooser.show();
        });

        visit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VisitActivity.class);
            intent.putExtra("cartoMob", cartoMob);
            visitLauncher.launch(intent);
        });
    }

    private void setBuildingName() {
        CustomDialogName.NameListener listenerCartoName = name -> {
            cartoMob.setName(name);
            nameBuilding.setText(cartoMob.getName());
            Log.d("ModelContent", cartoMob.toString());
            nameBuildingBtn.setVisibility(View.INVISIBLE);
            writeFile.setEnabled(true);
        };
        final CustomDialogName cartoName = new CustomDialogName(this, listenerCartoName, getText(R.string.custom_dialog_name_title_building).toString());
        cartoName.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ModelContent", cartoMob.toString());
        if (cartoMob != null) {
            if (cartoMob.getName() != null && !cartoMob.getName().isEmpty()) {
                writeFile.setEnabled(true);
                nameBuildingBtn.setVisibility(View.INVISIBLE);
                nameBuilding.setText(cartoMob.getName());
            }
            if (cartoMob.isEmpty()) {
                Log.i(LOG_TAG, "cartoMob est vide");
                loadRoom.setEnabled(false);
                visit.setEnabled(false);
                nbRooms.setText(R.string.main_number_of_rooms_zero);
            } else {
                loadRoom.setEnabled(true);
                nbRooms.setText(getString(R.string.main_number_of_rooms, cartoMob.getSize()));
                for (Room r : cartoMob.getRooms()) {
                    visit.setEnabled(r.isComplete());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_rename) {
            setBuildingName();
        }
        return super.onOptionsItemSelected(item);
    }
}