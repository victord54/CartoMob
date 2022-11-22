package fr.victord54.cartomob.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.CartoMob;
import fr.victord54.cartomob.models.Door;
import fr.victord54.cartomob.models.Room;
import fr.victord54.cartomob.tools.Save;
import fr.victord54.cartomob.views.CustomDialogName;
import fr.victord54.cartomob.views.CustomDialogRoomChooserForDoor;

public class WallActivity extends AppCompatActivity {
    private static final String LOG_TAG = WallActivity.class.getSimpleName();
    public static final int RESULT_CODE_WALL = 456;
    private CartoMob cartoMob;
    private int iRoom;
    private String key;

    private TextView name;
    private TextView orientation;
    private ImageView photo;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private Button resetDoors;
    private Rect rect;
    private Canvas canvas = new Canvas();

    private int rectL;
    private int rectR;
    private int rectT;
    private int rectB;

    // TODO: Affichage propre des portes déjà placées.
    // TODO: Possibilité d'éditer les portes déjà placées.

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        cartoMob = (CartoMob) getIntent().getSerializableExtra("cartoMob");
        iRoom = getIntent().getIntExtra("iRoom", 0);
        key = getIntent().getStringExtra("orientation");
        Log.d("DebugWall", "key = " + key);
        Log.d("DebugWall", "room = " + cartoMob.getRoom(iRoom).getName());

        name = findViewById(R.id.wallActivity_name);
        orientation = findViewById(R.id.wallActivity_orientation);
        photo = findViewById(R.id.wallActivity_photo);
        surfaceView = findViewById(R.id.wallActivity_surfaceView);
        resetDoors = findViewById(R.id.wallActivity_reset_doors);

        name.setText(cartoMob.getRoom(iRoom).getName());

        orientation.setText(cartoMob.getRoom(iRoom).getWall(key).getOrientation());
        Bitmap bitmap = Save.getInstance().loadBmpFromStorage(this, cartoMob.getRoom(iRoom).getWall(key).getNameOfPhoto() + ".photo");
        photo.setImageBitmap(bitmap);

        surfaceView.setZOrderOnTop(true);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                canvas = holder.lockCanvas();
                Log.d("Rectangle", "Les rectangles (creation canvas)" + cartoMob.getRoom(iRoom).getWall(key).getDoors());
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                for (Door d : cartoMob.getRoom(iRoom).getWall(key).getDoors()) {
                    if (d.getDst() == null)
                        cartoMob.getRoom(iRoom).getWall(key).getDoors().remove(d);
                    canvas.drawRect(d.getRectangle(), setPaint());
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });


        photo.setOnTouchListener((view, motionEvent) -> {
            if ((motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE) && motionEvent.getPointerCount() == 2) {
                rectL = (int) (motionEvent.getX(0) + photo.getX());
                rectR = (int) (motionEvent.getX(1) + photo.getX());
                rectT = (int) (motionEvent.getY(0) + photo.getY());
                rectB = (int) (motionEvent.getY(1) + photo.getY());

                if (rectT < photo.getY()) rectT = (int) photo.getY();
                if (rectB > photo.getY() + photo.getHeight())
                    rectB = (int) (photo.getY() + photo.getHeight());

                if (rectB < photo.getY()) rectB = (int) photo.getY();
                if (rectT > photo.getY() + photo.getHeight())
                    rectT = (int) (photo.getY() + photo.getHeight());

                rect = new Rect(rectL, rectT, rectR, rectB);
                rect.sort();
                if (rect.width() <= 0) {
                    rect.left = (int) photo.getX();
                    rect.right = (int) photo.getX() + photo.getWidth();
                }
                if (rect.height() <= 0) {
                    rect.top = (int) photo.getY();
                    rect.bottom = (int) photo.getY() + photo.getHeight();
                }

//                Log.d("Rectangle", "le rectangle : " + rect.toShortString());

                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                for (Door d: cartoMob.getRoom(iRoom).getWall(key).getDoors()) {
                    canvas.drawRect(d.getRectangle(), setPaint());
                }
                canvas.drawRect(rect, setPaint());
                surfaceHolder.unlockCanvasAndPost(canvas);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (rect != null) {
                    CustomDialogRoomChooserForDoor.RoomListener roomListener = name -> {
                        Door tmp = new Door(rect, cartoMob.getRoom(iRoom));
                        tmp.setDst(cartoMob.getRoomFromName(name));
                        cartoMob.getRoom(iRoom).getWall(key).addDoor(tmp);

                        // On rafraichit l'affichage avec le nouveau rectangle (porte)
                        canvas = surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        for (Door d : cartoMob.getRoom(iRoom).getWall(key).getDoors()) {
                            canvas.drawRect(d.getRectangle(), setPaint());
                        }
                        surfaceHolder.unlockCanvasAndPost(canvas);
//                        rect = null; // On a plus besoin et comme ça la prochaine porte (rectangle) sera recréé.
                    };

                    CustomDialogRoomChooserForDoor.RoomCreateListener roomCreateListener = () -> {
                        CustomDialogName.NameListener listenerRoomName = name -> {
                            if (cartoMob.getIndiceFromRoom(name) != -1) {
                                Toast.makeText(WallActivity.this, "Cette pièce existe déjà", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            cartoMob.addRoom(new Room(name, "room" + (cartoMob.getSize())));
                            cartoMob.getRoom(iRoom).getWall(key).getDoor(cartoMob.getRoom(iRoom).getWall(key).nbDoors() - 1).setDst(cartoMob.getRoomFromName(name));
                            Intent sendData = new Intent(WallActivity.this, RoomActivity.class);
                            sendData.putExtra("cartoMob", cartoMob);
                            sendData.putExtra("iRoom", cartoMob.getSize() - 1);
                            newRoomLauncher.launch(sendData);
                        };
                        final CustomDialogName roomName = new CustomDialogName(WallActivity.this, listenerRoomName, getText(R.string.custom_dialog_name_title_room).toString());
                        roomName.show();
                    };

                    CustomDialogRoomChooserForDoor.RoomDismissListener roomDismissListener = () -> {
                        // TODO: Bug à vérifier !!
                        canvas = surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        for (Door d : cartoMob.getRoom(iRoom).getWall(key).getDoors()) {
                            if (d.getDst() == null) {
                                cartoMob.getRoom(iRoom).getWall(key).getDoors().remove(d);
                                Log.d("ResetDoors", "Effacement des portes menant à rien");
                            }
                            canvas.drawRect(d.getRectangle(), setPaint());
                        }
                        Log.d("ResetDoors", cartoMob.getRoom(iRoom).getWall(key).getDoors().toString());
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    };
                    ArrayList<Room> rooms = new ArrayList<>(cartoMob.getRooms());
                    rooms.remove(cartoMob.getRoom(iRoom));
                    CustomDialogRoomChooserForDoor customDialogRoomChooserForDoor = new CustomDialogRoomChooserForDoor(this, roomListener, roomCreateListener, roomDismissListener, rooms);
                    customDialogRoomChooserForDoor.show();
                    Log.d("Debug_door", cartoMob.toString());
                }
                Log.d("Rectangle", "Les rectangles : " + cartoMob.getRoom(iRoom).getWall(key).getDoors());
            }
            return true;
        });

        resetDoors.setOnClickListener(view -> {
            cartoMob.getRoom(iRoom).getWall(key).getDoors().clear();
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            for (Door d : cartoMob.getRoom(iRoom).getWall(key).getDoors()) {
                canvas.drawRect(d.getRectangle(), setPaint());
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        });
    }

    private Paint setPaint() {
        Paint paintRectangle = new Paint();
        paintRectangle.setColor(Color.GRAY);
        paintRectangle.setAlpha(128);
        paintRectangle.setStyle(Paint.Style.FILL);
        paintRectangle.setStrokeWidth(7);
        return paintRectangle;
    }

    @Override
    public void onBackPressed() {
        Intent reply = new Intent();
        for (Door d : cartoMob.getRoom(iRoom).getWall(key).getDoors()) {
            if (d.getDst() == null) cartoMob.getRoom(iRoom).getWall(key).getDoors().remove(d);
        }
        reply.putExtra("cartoMob", cartoMob);
        setResult(RESULT_CODE_WALL, reply);
        Log.d("LOG_TAG", "On renvoie les données");
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        canvas = surfaceHolder.lockCanvas();
//        Log.d("Canvas", canvas.toString());
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        for (Door d: cartoMob.getRoom(iRoom).getWall(key).getDoors()) {
//            if (d.getDst() == null)
//                cartoMob.getRoom(iRoom).getWall(key).getDoors().remove(d);
//            canvas.drawRect(d.getRectangle(), setPaint());
//        }
    }
}