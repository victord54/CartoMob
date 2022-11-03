package fr.victord54.cartomob.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.Room;

public class CustomDialogRoomChooserForDoor extends Dialog {
    public interface RoomListener {
        void roomSelected(String name);
    }

    public interface RoomCreateListener {
        void create();
    }

    private final Context context;
    private final RoomListener roomListener;
    private final RoomCreateListener roomCreateListener;

    private RecyclerView recyclerView;
    private RoomChooserAdapter adapter;
    private Button ok_btn;
    private Button create;

    private final ArrayList<Room> rooms;
    private String nameOfRoom;

    public CustomDialogRoomChooserForDoor(@NonNull Context context, RoomListener rl, RoomCreateListener rcl, ArrayList<Room> rooms) {
        super(context);
        this.context = context;
        roomListener = rl;
        roomCreateListener = rcl;
        this.rooms = rooms;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_room_chooser_for_door);

        recyclerView = findViewById(R.id.custom_dialog_room_chooser_for_door_recycler_view);
        ok_btn = findViewById(R.id.custom_dialog_room_chooser_for_door_btn_ok);
        create = findViewById(R.id.custom_dialog_room_chooser_for_door_btn_create);

        @SuppressLint("NotifyDataSetChanged") RoomChooserAdapter.ItemClickListener itemClickListener = s -> {
            recyclerView.post(() -> adapter.notifyDataSetChanged());
            nameOfRoom = s;
            Log.d("RoomChooser", "nom fichier choisi : " + nameOfRoom);
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RoomChooserAdapter(rooms, itemClickListener);
        recyclerView.setAdapter(adapter);

        ok_btn.setOnClickListener(view -> {
            this.dismiss();
            if (nameOfRoom != null && !nameOfRoom.isEmpty()) {
                roomListener.roomSelected(nameOfRoom);
            }
        });

        create.setOnClickListener(v -> {
            dismiss();
            roomCreateListener.create();
        });
    }
}
