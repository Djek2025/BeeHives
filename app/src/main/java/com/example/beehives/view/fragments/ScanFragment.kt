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
import com.example.beehives.App
import com.example.beehives.R
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.viewModels.ScanViewModel
import com.example.beehives.viewModels.SharedViewModel
import com.example.beehives.utils.ViewModelFactory
import com.google.mlkit.vision.barcode.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.InputImage.IMAGE_FORMAT_NV21
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

    private lateinit var viewModel: ScanViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var navController : NavController
    private var request: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).getComponent().inject(this)
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
        cameraView.setLifecycleOwner(this)
        cameraView.addFrameProcessor{
            scanBarcodes(getVisionImageFromFrame(it))
        }
    }

    private fun getVisionImageFromFrame(frame: Frame): InputImage {
        return InputImage.fromByteArray(frame.getData<ByteArray>(), frame.size.width,frame.size.height, frame.rotationToUser, IMAGE_FORMAT_NV21)
    }

    private fun scanBarcodes(image: InputImage) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC,
                Barcode.FORMAT_CODE_128)
            .build()

        val scanner = BarcodeScanning.getClient(options)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                processResult(barcodes)
            }
            .addOnFailureListener {
                    e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
    }


    private var counter = 0
    private fun processResult(barcodes: List<Barcode>) {
        if (barcodes.isNotEmpty() && counter == 0){
            counter++
            if(request == "reed" || request == null){
                try {
                    CoroutineScope(Dispatchers.IO).launch{
                        sharedViewModel.selectedHiveId =
                            viewModel.getHiveIdByLabelAsync(barcodes.first().rawValue!!).await()
                    }
                    navController.navigate(R.id.action_scanFragment_to_aboutHiveFragment)
                } catch (e: Exception) {
                    Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                }
            } else if(request =="write") {
                viewModel.setLabelByHiveId(sharedViewModel.selectedHiveId, barcodes.first().rawValue!!)
                navController.popBackStack()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        sharedViewModel.scanRequest = "reed"
    }
}
