package fr.victord54.cartomob.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.models.Room;

public class RoomChooserAdapter extends RecyclerView.Adapter<RoomChooserAdapter.ViewHolder> {

    public interface ItemClickListener {
        void onClick(String s);
    }

    private final ItemClickListener itemClickListener;
    private final ArrayList<Room> rooms;
    int selectedPosition;

    public RoomChooserAdapter(ArrayList<Room> rooms, ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_chooser_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.roomName.setText(rooms.get(position).getName());

        holder.radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedPosition = holder.getAdapterPosition();
                itemClickListener.onClick(holder.roomName.getText().toString());
            }
        });

        holder.radioButton.setChecked(position == selectedPosition);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RadioButton radioButton;
        private final TextView roomName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.room_chooser_recycler_view_radio_btn);
            roomName = itemView.findViewById(R.id.room_chooser_recycler_view_name);
        }
    }
}
