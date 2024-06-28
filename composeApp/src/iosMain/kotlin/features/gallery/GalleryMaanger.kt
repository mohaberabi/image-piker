package features.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import features.image.AppImage
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject


internal class GalleryDelegate(
    private val onResult: (AppImage?) -> Unit,
) : NSObject(), UIImagePickerControllerDelegateProtocol,
    UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo.getValue(
            UIImagePickerControllerEditedImage
        ) as? UIImage ?: didFinishPickingMediaWithInfo.getValue(
            UIImagePickerControllerEditedImage
        ) as? UIImage
        onResult.invoke(AppImage(image))
        picker.dismissViewControllerAnimated(true, null)
    }
}

class IOSGalleryManager(
    onResult: (AppImage?) -> Unit,
) : GalleryManager {
    private val delegate = GalleryDelegate(
        onResult = onResult,
    )
    private val picker = UIImagePickerController()
    override fun launch() {
        picker.setSourceType(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary)
        picker.setAllowsEditing(true)
        picker.setDelegate(delegate)
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            picker, true, null
        )
    }
}

@Composable
actual fun rememberGalleryManager(
    onResult: (AppImage?) -> Unit,
): GalleryManager {
    return remember {
        IOSGalleryManager(
            onResult = onResult,
        )
    }
}