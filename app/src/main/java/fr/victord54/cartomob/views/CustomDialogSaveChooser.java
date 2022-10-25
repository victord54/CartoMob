package fr.victord54.cartomob.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.SplittableRandom;

import fr.victord54.cartomob.R;

public class CustomDialogSaveChooser extends Dialog {
    public interface FileListener {
        void nameFileTyped(String name);
    }

    private final Context context;
    private final FileListener fileListener;

    private RecyclerView recyclerView;
    private SaveChooserAdapter adapter;
    private Button ok_btn;

    private final ArrayList<String> files;
    private String nameOfFile;

    public CustomDialogSaveChooser(@NonNull Context context, FileListener fl, ArrayList<String> files) {
        super(context);
        this.context = context;
        fileListener = fl;
        this.files = files;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_save_chooser);

        recyclerView = findViewById(R.id.custom_dialog_save_chooser_recycler_view);
        ok_btn = findViewById(R.id.custom_dialog_name_btn_ok);

        SaveChooserAdapter.ItemClickListener itemClickListener = new SaveChooserAdapter.ItemClickListener() {
            @Override
            public void onClick(String s) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                nameOfFile = s;
                Log.d("FileChooser", "nom fichier choisi : " + nameOfFile);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new SaveChooserAdapter(files, itemClickListener);
        recyclerView.setAdapter(adapter);

        ok_btn.setOnClickListener(view -> {
            this.dismiss();
            if (nameOfFile != null && !nameOfFile.isEmpty()) {
                fileListener.nameFileTyped(nameOfFile);
            }
        });
    }
}
