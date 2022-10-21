package fr.victord54.cartomob.views;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.victord54.cartomob.R;

public class BuildingViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public Button button;


    public BuildingViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.buildingList_item_name);
        button = itemView.findViewById(R.id.buildingList_item_btn);
    }
}
