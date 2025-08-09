import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ProcessUtils {

    public static boolean ensureBinariesReady(Context ctx) {
        try {
            // نام باینری را مطابق چیزی که ساختی بگذار: مثلا xray یا tun2socks
            String binName = "xray"; // یا "tun2socks"
            File dst = new File(ctx.getFilesDir(), binName);

            if (!dst.exists() || dst.length() == 0) {
                copyAsset(ctx, binName, dst);
            }
            // اجرای chmod
            dst.setExecutable(true);

            return true;
        } catch (Throwable t) {
            Log.e("ProcessUtils", "binary prepare failed", t);
            Toast.makeText(ctx, "خطا در آماده‌سازی باینری: " + t.getClass().getSimpleName(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private static void copyAsset(Context ctx, String assetName, File outFile) throws Exception {
        try (InputStream in = ctx.getAssets().open(assetName);
             FileOutputStream out = new FileOutputStream(outFile)) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
        }
    }
}
