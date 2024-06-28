package features.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import features.image.AppImage
import features.permission.PermissionCallBack
import features.permission.PermissionStatus
import features.utils.mapCameraPermission
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerCameraCaptureMode
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject


private class CameraDelegate(
    private val onResult: (AppImage?) -> Unit,
) : NSObject(),
    UIImagePickerControllerDelegateProtocol,
    UINavigationControllerDelegateProtocol {
    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image =
            didFinishPickingMediaWithInfo.getValue(UIImagePickerControllerEditedImage) as? UIImage
                ?: didFinishPickingMediaWithInfo.getValue(
                    UIImagePickerControllerOriginalImage
                ) as? UIImage
        onResult.invoke(AppImage(image))
        picker.dismissViewControllerAnimated(true, null)
    }
}

class IOSCameraManager(
    private val onResult: (AppImage?) -> Unit,
) : CameraManager {
    override fun launch() {
        val picker = UIImagePickerController()
        val delegate = CameraDelegate(
            onResult = onResult,
        )
        picker.setSourceType(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)
        picker.setAllowsEditing(true)
        picker.setCameraCaptureMode(UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModePhoto)
        picker.setDelegate(delegate)
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            picker, true, null
        )
    }
}

@Composable
actual fun rememberCameraManager(
    onResult: (AppImage?) -> Unit,
): CameraManager {
    return remember { IOSCameraManager(onResult) }
}