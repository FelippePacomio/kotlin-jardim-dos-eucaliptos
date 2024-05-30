package com.example.associacao_jardim_eucaliptos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.appcompat.app.AlertDialog;

public class DetailNewsActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailDate;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        detailDesc = findViewById(R.id.detailNewsDesc);
        detailImage = findViewById(R.id.detailNewsImage);
        detailTitle = findViewById(R.id.detailNewsTitle);
        deleteButton = findViewById(R.id.deleteNewsButton);
        editButton = findViewById(R.id.editNewsButton);
        detailDate = findViewById(R.id.detailNewsDate);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailDate.setText(bundle.getString("Language"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailNewsActivity.this, UpdateNewsActivity.class)
                        .putExtra("Title", detailTitle.getText().toString())
                        .putExtra("Description", detailDesc.getText().toString())
                        .putExtra("Language", detailDate.getText().toString())  // Pass timestamp
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Deletar Notícia")
                .setMessage("Tem certeza que quer deletar essa notícia?")
                .setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNews();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteNews() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("news");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                reference.child(key).removeValue();
                Toast.makeText(DetailNewsActivity.this, "Deletado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }).addOnFailureListener(e -> Toast.makeText(DetailNewsActivity.this, "Ocorreu um erro ao deletar a noticia", Toast.LENGTH_SHORT).show());
    }
}
