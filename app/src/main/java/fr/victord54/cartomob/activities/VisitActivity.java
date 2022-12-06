package fr.victord54.cartomob.activities;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.CartoMob;
import fr.victord54.cartomob.models.Door;
import fr.victord54.cartomob.models.Room;
import fr.victord54.cartomob.tools.Save;
import fr.victord54.cartomob.views.CustomDialogRoomChooser;

public class VisitActivity extends AppCompatActivity {
    public static final int RESULT_CODE_VISIT = 789;

    private CartoMob cartoMob;

    private ConstraintLayout layout;
    private TextView name;
    private TextView shortPathText;
    private ImageView photo;
    private Button left, right;
    ArrayList<Button> doors;

    private DijkstraShortestPath<Room, DefaultEdge> dijkstraShortestPath;
    private GraphPath<Room, DefaultEdge> path;
    private Room nextRoom;
    private Room endRoom;
    private boolean arrived = true;


    private int iRoom;
    private final String[] orientation = {"N", "E", "S", "W"};
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);

        cartoMob = (CartoMob) getIntent().getSerializableExtra("cartoMob");
        doors = new ArrayList<>();

        layout = findViewById(R.id.visitActivity_layout);
        name = findViewById(R.id.visitActivity_room_name);
        shortPathText = findViewById(R.id.visit_shortpath_text);
        photo = findViewById(R.id.visitActivity_photo);
        left = findViewById(R.id.visitActivity_left_side);
        right = findViewById(R.id.visitActivity_right_side);

        if (arrived)
            shortPathText.setVisibility(View.INVISIBLE);

        left.setOnClickListener(v -> {
            minusI();
            shortPath();
            onResume();
        });

        right.setOnClickListener(v -> {
            plusI();
            shortPath();
            onResume();
        });
    }

    private void plusI() {
        i = (i+1)%4;
//        Log.d("ValI", "i = " + i);
    }

    private void minusI() {
        i = (i+3)%4;
//        Log.d("ValI", "i = " + i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        name.setText(cartoMob.getRoom(iRoom).getName());
        photo.setImageBitmap(Save.getInstance().loadBmpFromStorage(this, cartoMob.getRoom(iRoom).getWall(orientation[i]).getNameOfPhoto() + ".photo"));
        for (Button b: doors)
            layout.removeView(b);
        doors.clear();
        for (Door d: cartoMob.getRoom(iRoom).getWall(orientation[i]).getDoors()) {
            Log.d("Doors", d.toString());
            Button tmp = new Button(this);
            tmp.setText(d.getDst().getName());
            tmp.setX(d.getRectangle().left);
            tmp.setY(d.getRectangle().top);
            tmp.setWidth(d.getRectangle().right - d.getRectangle().left);
            tmp.setHeight(d.getRectangle().bottom - d.getRectangle().top);
            tmp.setAlpha(0.5F);
            doors.add(tmp);
            layout.addView(tmp);
        }

        for (Button b: doors) {
            b.setOnClickListener(v -> {
                Log.d("Door", "Door dst : " + b.getText());
                iRoom = cartoMob.getIndiceFromRoom((String) b.getText());
                onResume();
            });
        }
        shortPath();
    }

    private Paint setPaint() {
        Paint paintRectangle = new Paint();
        paintRectangle.setColor(Color.YELLOW);
        paintRectangle.setStyle(Paint.Style.STROKE);
        paintRectangle.setStrokeWidth(7);
        return paintRectangle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_visit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_shortpath) {
            CustomDialogRoomChooser.RoomListener roomListener = name -> {
                dijkstraShortestPath = new DijkstraShortestPath<>(cartoMob.modelToGraph());
                endRoom = cartoMob.getRoomFromName(name);
                arrived = false;
                shortPath();
            };
            CustomDialogRoomChooser customDialogRoomChooser = new CustomDialogRoomChooser(this, roomListener, cartoMob.getRooms());
            customDialogRoomChooser.show();
        }

        return super.onOptionsItemSelected(item);
    }

    void shortPath() {
        if (!arrived) {
            if (endRoom.equals(cartoMob.getRoom(iRoom))) {
                shortPathText.setVisibility(View.INVISIBLE);
                arrived = true;
                Toast.makeText(this, getString(R.string.short_path_arrived), Toast.LENGTH_SHORT).show();
            } else {
                path = dijkstraShortestPath.getPath(cartoMob.getRoom(iRoom), endRoom);
                nextRoom = path.getVertexList().get(1);
                shortPathText.setVisibility(View.VISIBLE);
                shortPathText.setText(getString(R.string.short_path_next_room, nextRoom.getName()));
            }
        }
    }
}