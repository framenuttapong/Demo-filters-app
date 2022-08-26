package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    Button buttonSelectImage;
    ImageView previewImage;
    int SELECT_PICTURE = 200;

    static {
        System.loadLibrary("NativeImageProcessor");
    }
    ImageView filter1,filter2,filter3;
    Bitmap orgBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        previewImage = findViewById(R.id.previewImage);
        filter1 = (ImageView)findViewById(R.id.filter1);
        filter2 = (ImageView)findViewById(R.id.filter2);
        filter3 = (ImageView)findViewById(R.id.filter3);

        filter1.setOnClickListener(this);
        filter2.setOnClickListener(this);
        filter3.setOnClickListener(this);

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });


    }

    void imageChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    previewImage.setImageURI(selectedImageUri);
                    BitmapDrawable drawable = (BitmapDrawable) previewImage.getDrawable();
                    orgBitmap = drawable.getBitmap();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.filter1:
                Filter filter1 = new Filter();
                filter1.addSubFilter(new BrightnessSubFilter(30));
                filter1.addSubFilter(new ContrastSubFilter(1.1f));

                Bitmap inputImage = orgBitmap.copy(Bitmap.Config.ARGB_8888,true);
                Bitmap outputImage = filter1.processFilter(inputImage);

                previewImage.setImageBitmap(outputImage);
                break;

            case R.id.filter2:
                Filter filter2 = SampleFilters.getBlueMessFilter();
                Bitmap blueMess= orgBitmap.copy(Bitmap.Config.ARGB_8888,true);
                Bitmap blueMessOutputImage = filter2.processFilter(blueMess);

                previewImage.setImageBitmap(blueMessOutputImage);
                break;

            case R.id.filter3:
                Filter filter3 = SampleFilters.getLimeStutterFilter();

                Bitmap limeStutterBitmap = orgBitmap.copy(Bitmap.Config.ARGB_8888,true);
                Bitmap limeStutterOutputImage = filter3.processFilter(limeStutterBitmap);

                previewImage.setImageBitmap(limeStutterOutputImage);
                break;
        }

    }
}