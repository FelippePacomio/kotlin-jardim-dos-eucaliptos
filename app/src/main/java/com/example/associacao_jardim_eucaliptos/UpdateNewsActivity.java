package com.example.associacao_jardim_eucaliptos;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateNewsActivity extends AppCompatActivity {

    ImageView updateImage;
    Button updateButton;
    EditText updateDesc, updateTitle;
    String title, desc, lang, imageUrl, key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_update);

        updateButton = findViewById(R.id.updateNewsButton);
        updateDesc = findViewById(R.id.updateNewsDesc);
        updateImage = findViewById(R.id.updateNewsImage);
        updateTitle = findViewById(R.id.updateNewsTitle);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            updateImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateNewsActivity.this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Glide.with(UpdateNewsActivity.this).load(bundle.getString("Image")).into(updateImage);
            updateTitle.setText(bundle.getString("Title"));
            updateDesc.setText(bundle.getString("Description"));
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("Image");
            lang = bundle.getString("Language");  // Get the timestamp
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("news").child(key);

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        // Set the OnClickListener for the update button to call saveData()
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    public void saveData() {
        if (uri != null) {
            storageReference = FirebaseStorage.getInstance().getReference().child("Android Images").child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateNewsActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
                    updateData(dialog);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        } else {
            // No new image selected, use the old image URL
            imageUrl = oldImageURL;
            updateData(null);
        }
    }

    public void updateData(AlertDialog dialog) {
        title = updateTitle.getText().toString().trim();
        desc = updateDesc.getText().toString().trim();

        DataClass dataClass = new DataClass(title, desc, lang, imageUrl);

        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (uri != null && oldImageURL != null) {
                        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                        reference.delete();
                    }
                    Toast.makeText(UpdateNewsActivity.this, "Notícia atualizada", Toast.LENGTH_SHORT).show();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateNewsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }
}
