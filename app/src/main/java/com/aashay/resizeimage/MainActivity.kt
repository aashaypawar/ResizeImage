package com.aashay.resizeimage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.IOException

class MainActivity : AppCompatActivity() {

    // Declaring ImageView, number of images to pick from the device and a Bitmap to store the image
    private lateinit var mImageView: ImageView
    private val mPickImage = 1
    private lateinit var mYourBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Declaring and initializing the buttons
        val mUploadButton = findViewById<Button>(R.id.upload_button)
        val mResizeButton = findViewById<Button>(R.id.resize_button)

        // Initializing the image view
        mImageView = findViewById(R.id.imageView)

        // When upload button is clicked, the intent navigates to the local content in the device, where one can select the desired image
        mUploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, mPickImage)
        }

        // When resize button is clicked
        mResizeButton.setOnClickListener {
            // Generate a new Bitmap of custom dimenions and set it in the image view
            val resized = Bitmap.createScaledBitmap(mYourBitmap, 300, 300, true)
            mImageView.setImageBitmap(resized)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mPickImage && resultCode == Activity.RESULT_OK) {
            // If the image file does not exist
            if (data == null) {
                Toast.makeText(applicationContext,"Error",Toast.LENGTH_SHORT).show()
                return
            }

            // Otherwise
            try {
                // Load the image address and set it in the image view
                val imageUri: Uri = data.data!!
                val source = ImageDecoder.createSource(this.contentResolver, imageUri)
                mYourBitmap = ImageDecoder.decodeBitmap(source)
                mImageView.setImageBitmap(mYourBitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}