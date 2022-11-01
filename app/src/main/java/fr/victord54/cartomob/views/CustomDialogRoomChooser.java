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

public class CustomDialogRoomChooser extends Dialog {
    public interface RoomListener {
        void roomSelected(String name);
    }

    private final Context context;
    private final RoomListener roomListener;

    private RecyclerView recyclerView;
    private RoomChooserAdapter adapter;
    private Button ok_btn;

    private final ArrayList<Room> rooms;
    private String nameOfRoom;

    public CustomDialogRoomChooser(@NonNull Context context, RoomListener rl, ArrayList<Room> rooms) {
        super(context);
        this.context = context;
        roomListener = rl;
        this.rooms = rooms;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_room_chooser);

        recyclerView = findViewById(R.id.custom_dialog_room_chooser_recycler_view);
        ok_btn = findViewById(R.id.custom_dialog_room_chooser_btn_ok);

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
    }
}
