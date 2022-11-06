package fr.victord54.cartomob.activities;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.CartoMob;
import fr.victord54.cartomob.models.Door;
import fr.victord54.cartomob.tools.Save;

public class VisitActivity extends AppCompatActivity {
    public static final int RESULT_CODE_VISIT = 789;

    private CartoMob cartoMob;

    private ConstraintLayout layout;
    private TextView name;
    private ImageView photo;
    private Button left, right;
    ArrayList<Button> doors;

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
        photo = findViewById(R.id.visitActivity_photo);
        left = findViewById(R.id.visitActivity_left_side);
        right = findViewById(R.id.visitActivity_right_side);

        left.setOnClickListener(v -> {
            minusI();
            onResume();
        });

        right.setOnClickListener(v -> {
            plusI();
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
    }

    private Paint setPaint() {
        Paint paintRectangle = new Paint();
        paintRectangle.setColor(Color.YELLOW);
        paintRectangle.setStyle(Paint.Style.STROKE);
        paintRectangle.setStrokeWidth(7);
        return paintRectangle;
    }
}