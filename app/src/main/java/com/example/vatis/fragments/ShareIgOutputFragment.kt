package com.example.vatis.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.vatis.R
import com.google.firebase.firestore.DocumentReference
import android.app.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.vatis.items.TemplateItem
import kotlinx.android.synthetic.main.fragment_share_ig_output.view.*
import java.io.ByteArrayOutputStream
import java.io.File


class ShareIgOutputFragment(private val planRef: DocumentReference, private val templateItem: TemplateItem) : Fragment() {
    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_share_ig_output, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: build output image with queryied data
        val output = buildOutputImage(templateItem.name)
        view.share_ig_output_image.setImageBitmap(output)
        view.share_ig_output_generate_text.setOnClickListener {
            Toast.makeText(this.context, "sending ${templateItem.name} to ig", Toast.LENGTH_SHORT).show()
            shareToIg(output)
        }
    }

    private fun shareToIg(output: Bitmap) {
        // Define image asset URI
        val backgroundAssetUri: Uri = getUri(output)
        val sourceApplication = "com.example.vatis.app"

        // Instantiate implicit intent with ADD_TO_STORY action and background asset
        val intent = Intent("com.instagram.share.ADD_TO_STORY")
        intent.putExtra("source_application", sourceApplication)

        Log.d("ShareIgOutputFragment", "Intent instantiated")

        intent.setDataAndType(backgroundAssetUri, "image/png")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        // Instantiate activity and resolve implicit intent
        val shareActivity: Activity? = activity
        if (shareActivity!!.packageManager.resolveActivity(intent, 0) != null) {
            shareActivity.startActivityForResult(intent, 0)
        } else {
            Log.e("ShareIgOutputFragment", "*** activity not starting, no matching activity was found ***")
        }
    }

    private fun buildOutputImage(templateName: String) = run {
        when(templateName){
            "BEST_RATED" -> buildBestRated()
            "COMBINE" -> buildCombine()
            "DELICACY_MARBLE" -> buildDelicacyMarble()
            "POINT2POINT" -> buildPoint2Point()
            "POLAROID" -> buildPolaroid()
            "SIX_PHOTO" -> buildSixPhoto()
            "STAY_TIME" -> buildStayTime()
            "WHITE_COFFEE" -> buildWhiteCoffee()
            else -> buildDefault()
        }
    }

    private fun getUri(outputImage: Bitmap): Uri {
        val file = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "OUTPUT_IMAGE.png")
        file.delete()
        file.createNewFile()

        val fileOutputStream = file.outputStream()
        val byteArrayOutputStream = ByteArrayOutputStream()
        outputImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        fileOutputStream.write(byteArray)
        fileOutputStream.flush()
        fileOutputStream.close()
        byteArrayOutputStream.close()

//        return Uri.fromFile(file)
        return FileProvider.getUriForFile(requireContext(), requireContext().applicationContext?.packageName + ".provider", file)

    }

    //TODO: query logic
    //TODO: convert XML layout to Bitmap
    private fun buildBestRated(): Bitmap{
        return BitmapFactory.decodeResource(resources, R.drawable.best_rated)
    }

    private fun buildCombine(): Bitmap{
        return BitmapFactory.decodeResource(resources, R.drawable.combine_preview)
    }

    private fun buildDelicacyMarble(): Bitmap{
        return BitmapFactory.decodeResource(resources, R.drawable.delicacy_marble_preview)
    }

    private fun buildPoint2Point(): Bitmap{
        return BitmapFactory.decodeResource(resources, R.drawable.point2point_preview)
    }

    private fun buildPolaroid(): Bitmap{
        return BitmapFactory.decodeResource(resources, R.drawable.polaroid_preview)
    }

    private fun buildSixPhoto(): Bitmap{
        return BitmapFactory.decodeResource(resources, R.drawable.six_photo_preview)
    }

    private fun buildStayTime(): Bitmap{
        return BitmapFactory.decodeResource(resources, R.drawable.stay_time_preview)
    }

    private fun buildWhiteCoffee(): Bitmap{
        return BitmapFactory.decodeResource(resources, R.drawable.white_coffee_preview)
    }

    private fun buildDefault(): Bitmap{
        return BitmapFactory.decodeResource(resources, R.drawable.default_image)
    }

}