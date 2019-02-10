package com.atheeshproperty.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.security.Permission;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.MediaStore.Images.Media.getBitmap;

public class UpdateUserprofile extends Activity {

    DatabaseHelper mydb;
    EditText name, age, blood, weight, height, notes;
    Button update, cancel;
    ImageButton back, addimage;
    String userid;
    String imageUriString = null;
    CircleImageView userimage;
    Uri selectedImage = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.update_userprofile);

        name = findViewById(R.id.inputname);
        age = findViewById(R.id.inputage);
        blood = findViewById(R.id.inputblood);
        weight = findViewById(R.id.inputweight);
        height = findViewById(R.id.inputheight);
        notes = findViewById(R.id.inputnotes);
        update = findViewById(R.id.updatebutton);
        cancel = findViewById(R.id.updatecancelbutton);
        back = findViewById(R.id.backicon);

        userimage = findViewById(R.id.methodprofile);
        addimage = findViewById(R.id.addimage);

        mydb = new DatabaseHelper(this);

        goback();

        setExistedUserData();

        updateData();

        cancelButton();

        addimage();

    }

    public void addimage() {
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    boolean cam = (ActivityCompat.checkSelfPermission(UpdateUserprofile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED);
                    boolean galleryView = (ActivityCompat.checkSelfPermission(UpdateUserprofile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);
                    boolean galleryRead = (ActivityCompat.checkSelfPermission(UpdateUserprofile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);

                    if (cam || galleryView || galleryRead) {

                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        Log.d("Permission status", "No permissions.Permission requested.");

                        return;
                    } else {
                        Log.d("Permission status", "Have permissions");

                        final Dialog openDialog = new Dialog(UpdateUserprofile.this);
                        openDialog.setContentView(R.layout.customselectimagedialog);

                        Button usegallery = (Button) openDialog.findViewById(R.id.gallery);
                        Button takepicture = (Button) openDialog.findViewById(R.id.takephoto);
                        Button cancel = (Button) openDialog.findViewById(R.id.cancel);

                        usegallery.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 1);

                                openDialog.dismiss();


                            }
                        });


                        takepicture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                String folder_main = "Pillminder";

                                File f = new File(Environment.getExternalStorageDirectory(), folder_main);


                                if (!f.exists()) {//check whether the folder exists or not
                                    Boolean res = f.mkdirs();
                                    Log.d("Folder existence", "Folder created " + f.getAbsolutePath());
                                } else {
                                    Log.d("Folder Existence", "Already have the folder");

                                }

                                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                startActivityForResult(intent, 0);

                                openDialog.dismiss();
                            }
                        });


                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.cancel();
                            }
                        });

                        openDialog.show();
                    }
                }

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        File g = new File(Environment.getExternalStorageDirectory(), "/Pillminder/usernew" + userid + ".jpg");

        if (requestCode == 0) {

            switch (resultCode) {

                case Activity.RESULT_OK:

                    selectedImage = data.getData();//get image uri
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    assert selectedImage != null;
                    @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //file path of captured image
                    String filePath = cursor.getString(columnIndex);
                    //file path of captured image
                    File f = new File(filePath);
                    String filename = f.getName();

                    cursor.close();

                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    userimage.setImageBitmap(yourSelectedImage);

                    if (g.exists()) {
                        Log.d("Folder Existence", "Already have the folder");
                        Log.d("Folder Existence", "Already have the image");
                        g.delete();
                        Log.d("Folder Existence", "Delete the existence image");

                    }

                    savefile(filename, filePath);
                    break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "Nothing saved ", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            selectedImage = data.getData();
            Log.d("Image location", "This is the path : " + selectedImage);
            userimage.setImageURI(selectedImage);
            assert selectedImage != null;
            String b = selectedImage.toString();

            Toast.makeText(this, "path : " + selectedImage, Toast.LENGTH_LONG).show();


        }
    }

    public void savefile(String name, String path) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/Pillminder");
        File file = new File(Environment.getExternalStorageDirectory() + "/Pillminder/usernew" + userid + "8.jpg");

        if (!file.exists()) {
            try {

                FileChannel src = new FileInputStream(path).getChannel();
                FileChannel dst = new FileOutputStream(file).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void goback() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setExistedUserData() {

        Cursor received = mydb.getAllData();


        if (received.getCount() == 0) {
            Toast.makeText(UpdateUserprofile.this, "Unexpected error occured.Try again later.", Toast.LENGTH_LONG).show();
        } else
            while (received.moveToNext()) {
                userid = received.getString(0);
                name.setText(received.getString(1));
                age.setText(received.getString(2));
                blood.setText(received.getString(3));
                weight.setText(received.getString(4));
                height.setText(received.getString(5));
                notes.setText(received.getString(7));

                imageUriString = received.getString(6);

            }

        //Setting the profile image
        try {
            Uri image = Uri.parse(imageUriString);//Convert string to uri
            userimage.setImageURI(image);
        } catch (Exception e) {
            Log.d("Image not found", "This is the found url : " + imageUriString);
            userimage.setImageResource(R.drawable.ic_account_circle_white_24dp);
        }

        received.close();
        mydb.close();


    }

    public void updateData() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().trim().length() == 0) {
                    Toast.makeText(UpdateUserprofile.this, "'Name' field cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {

                    String s;
                    if (selectedImage == null) {
                        s = imageUriString;
                    } else {
                        s = selectedImage.toString();
                    }
                    boolean isupdated = mydb.updateUserProfileData(
                            userid,
                            name.getText().toString(),
                            age.getText().toString(),
                            blood.getText().toString(),
                            weight.getText().toString(),
                            height.getText().toString(),
                            s,
                            notes.getText().toString());

                    if (isupdated) {
                        Toast.makeText(UpdateUserprofile.this, "Your profile details updated.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(UpdateUserprofile.this, "Not updated.Something went wrong.Please try again later.", Toast.LENGTH_LONG).show();
                    }

                    //Intent intent = new Intent(UpdateUserprofile.this, MainActivity.class);
                    // startActivity(intent);
                    onBackPressed();
                }
            }
        });
    }

    public void cancelButton() {

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateUserprofile.this, "Changes may not saved.", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}
