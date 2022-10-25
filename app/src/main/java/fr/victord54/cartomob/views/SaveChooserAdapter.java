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

public class SaveChooserAdapter extends RecyclerView.Adapter<SaveChooserAdapter.ViewHolder> {

    public interface ItemClickListener {
        void onClick(String s);
    }

    private final ItemClickListener itemClickListener;
    private final ArrayList<String> files;
    int selectedPosition;

    public SaveChooserAdapter(ArrayList<String> files, ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.files = files;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_chooser_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fileName.setText(files.get(position));

        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedPosition = holder.getAdapterPosition();
                    itemClickListener.onClick(holder.fileName.getText().toString());
                }
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
        return files.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RadioButton radioButton;
        private final TextView fileName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.save_chooser_recycler_view_radio_btn);
            fileName = itemView.findViewById(R.id.save_chooser_recycler_view_name);
        }
    }
}
