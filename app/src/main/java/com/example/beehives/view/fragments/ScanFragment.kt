package com.example.beehives.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.beehives.R
import com.example.beehives.viewModel.BaseViewModel
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.otaliastudios.cameraview.frame.Frame
import kotlinx.android.synthetic.main.fragment_scan.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

private const val ARG_REQUEST = "request"
private const val ARG_CALLER = "caller_id"

class ScanFragment : Fragment(){

    private lateinit var options : FirebaseVisionBarcodeDetectorOptions
    private lateinit var detector: FirebaseVisionBarcodeDetector
    private lateinit var viewModel: BaseViewModel
    private lateinit var navController : NavController
    private var request: String? = "reed"
    private var callerId : Int? = 1

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScanFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        arguments?.let {
            request = it.getString(ARG_REQUEST)
            callerId = it.getInt(ARG_CALLER)
        }
        viewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(cameraView)

        Dexter.withActivity(activity)
            .withPermission(android.Manifest.permission.CAMERA)
            .withListener(object: PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    setupCamera()
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    Toast.makeText(context, "Permission rationale should be shown", Toast.LENGTH_LONG).show()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_LONG).show()
                }
            }).check()
    }

    private fun setupCamera() {
        options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(
                FirebaseVisionBarcode.FORMAT_QR_CODE,
                FirebaseVisionBarcode.FORMAT_AZTEC,
                FirebaseVisionBarcode.FORMAT_CODE_128)
            .build()
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)
        cameraView.setLifecycleOwner(this)
        cameraView.addFrameProcessor{
            proccesImage(getVisionImageFromFrame(it))
        }
    }

    private fun getVisionImageFromFrame(frame: Frame): FirebaseVisionImage {
        val metadata = FirebaseVisionImageMetadata.Builder()
            .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
            .setHeight(frame.size.height)
            .setWidth(frame.size.width)
            .build()
        return FirebaseVisionImage.fromByteArray(frame.getData<ByteArray>(), metadata)
    }

    private fun proccesImage(visionImageFromFrame: FirebaseVisionImage){
        detector.detectInImage(visionImageFromFrame)
            .addOnFailureListener{ e -> Toast.makeText(context, "Exception", Toast.LENGTH_LONG).show()}
            .addOnSuccessListener { firebaseVisionBarcodes ->
                processResult(firebaseVisionBarcodes)
            }
    }

    var counter = 0
    private fun processResult(firebaseVisionBarcodes: List<FirebaseVisionBarcode>) {
        if (firebaseVisionBarcodes.isNotEmpty() && counter == 0){
            counter++
            if(request == "reed"){
                try {
                    GlobalScope.launch {
                        val id = viewModel.getHiveIdByLabel(firebaseVisionBarcodes.first().rawValue!!).await()
                        withContext(Dispatchers.Main) {
                            navController.navigate(R.id.aboutHiveFragment, bundleOf("hive_id" to id))
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                }
            } else
                if(request =="write") {
                    viewModel.setLabelByHiveId(callerId!!, firebaseVisionBarcodes.first().rawValue!!)
                    navController.popBackStack()
            }
        }
    }
}
