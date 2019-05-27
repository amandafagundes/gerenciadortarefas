package com.strider.desafio.gerenciamentotarefas.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.strider.desafio.gerenciamentotarefas.R;
import com.strider.desafio.gerenciamentotarefas.Util.Permission;
import com.strider.desafio.gerenciamentotarefas.Util.Util;
import com.strider.desafio.gerenciamentotarefas.model.Task;

public class ImageActivity extends AppCompatActivity {

    ImageView mImage;
    ImageButton mButton;
    Permission p = new Permission();
    String filePath = null;
    Task task;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mImage = findViewById(R.id.img);
        mButton = findViewById(R.id.btn);

        Bundle args = getIntent().getExtras();
        if (args != null) task = (Task) args.getSerializable(("task"));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            task.setLatitude(location.getLatitude());
                            task.setLongitude(location.getLongitude());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("a");
            }
        });


        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        task.setImage(filePath);
        intent.putExtra("task", new Task());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        task.setImage(filePath);
        intent.putExtra("task", task);
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }

    private Context getContext() {
        return this;
    }


    private void selectImage() {
        final CharSequence[] options = {"Câmera", "Galeria"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageActivity.this);
        builder.setTitle("Enviar imagem");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                if (index == 0) {
                    openCamera();
                } else if (index == 1) {
                    openGallery();
                }
            }
        });
        builder.show();
    }


    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, p.REQUEST_PERMISSION_CAMERA);
    }

    public void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, p.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                takePicture(requestCode, resultCode, data);
                break;
            case 2:
                pickImage(requestCode, resultCode, data);
                break;

        }
        if (data == null || data.getData() != null) {
            mButton.setVisibility(View.GONE);
            mImage.setVisibility(View.VISIBLE);
        } else {
            mButton.setVisibility(View.VISIBLE);
            mImage.setVisibility(View.GONE);
        }
    }

    public void pickImage(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            if (photoUri != null) {
                filePath = Util.getImagePath(getContext(), data);
                Util.compressInputImage(data, getContext(), mImage);
            }
        }
    }

    private void takePicture(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mImage.setImageBitmap(photo);
            filePath = Util.getRealPathFromURI(getContext(), Util.getImageUri(getContext(), photo));
        }
    }
}

