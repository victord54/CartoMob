package fr.victord54.cartomob.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.victord54.cartomob.R;
import fr.victord54.cartomob.activities.BuildingActivity;
import fr.victord54.cartomob.activities.MainActivity;
import fr.victord54.cartomob.models.Building;
import fr.victord54.cartomob.models.CartoMob;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingViewHolder> {
    private final CartoMob cartoMob;
    private Context context;

    public BuildingAdapter(CartoMob l) {
        cartoMob = l;
    }

    @NonNull
    @Override
    public BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View buildingView = inflater.inflate(R.layout.activity_building_list_item, parent, false);

        BuildingViewHolder b = new BuildingViewHolder(buildingView);
        return b;
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingViewHolder holder, int position) {
        Building building = cartoMob.getBuildings().get(position);

        TextView nom = holder.name;
        nom.setText(building.getName());

        Button button = holder.button;
        button.setOnClickListener(view -> {
            Intent intent = new Intent(context, BuildingActivity.class);
            intent.putExtra("cartoMob", cartoMob);
            intent.putExtra("iBuilding", position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartoMob.getBuildings().size();
    }
}
