package com.example.beehives.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.beehives.R
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.viewModels.ScanViewModel
import com.example.beehives.viewModels.SharedViewModel
import com.example.beehives.viewModels.ViewModelFactory
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ScanFragment : Fragment(){

    private lateinit var options : FirebaseVisionBarcodeDetectorOptions
    private lateinit var detector: FirebaseVisionBarcodeDetector
    private lateinit var viewModel: ScanViewModel
    private lateinit var factory: ViewModelFactory
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var navController : NavController
    private var request: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = InjectorUtils.provideViewModelFactory(activity!!.application)
        viewModel = ViewModelProvider(this, factory).get(ScanViewModel::class.java)
        sharedViewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(SharedViewModel::class.java)
        request = sharedViewModel.scanRequest
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

    fun setupCamera() {
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

    fun getVisionImageFromFrame(frame: Frame): FirebaseVisionImage {
        val metadata = FirebaseVisionImageMetadata.Builder()
            .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
            .setHeight(frame.size.height)
            .setWidth(frame.size.width)
            .build()
        return FirebaseVisionImage.fromByteArray(frame.getData<ByteArray>(), metadata)
    }

    fun proccesImage(visionImageFromFrame: FirebaseVisionImage){
        detector.detectInImage(visionImageFromFrame)
            .addOnFailureListener{ e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()}
            .addOnSuccessListener { firebaseVisionBarcodes ->
                processResult(firebaseVisionBarcodes)
            }
    }

    private var counter = 0
    fun processResult(firebaseVisionBarcodes: List<FirebaseVisionBarcode>) {
        if (firebaseVisionBarcodes.isNotEmpty() && counter == 0){
            counter++
            if(request == "reed" || request == null){
                try {
                    CoroutineScope(Dispatchers.IO).launch{
                        sharedViewModel.selectedHive =
                            viewModel.getHiveIdByLabelAsync(firebaseVisionBarcodes.first().rawValue!!).await()
                    }
                    navController.navigate(R.id.action_scanFragment_to_aboutHiveFragment)
                } catch (e: Exception) {
                    Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                }
            } else if(request =="write") {
                viewModel.setLabelByHiveId(sharedViewModel.selectedHive, firebaseVisionBarcodes.first().rawValue!!)
                navController.popBackStack()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        sharedViewModel.scanRequest = "reed"
    }
}
