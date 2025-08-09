package com.example.nativex;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ProcessUtils {

    private ProcessUtils(){}

    public static void prepareBinaries(Context ctx) throws IOException, InterruptedException {
        File binDir = new File(ctx.getFilesDir(), "bin/arm64-v8a");
        if (!binDir.exists() && !binDir.mkdirs()) {
            throw new IOException("cannot create " + binDir);
        }
        copyIfMissing(ctx.getAssets(), "bin/arm64-v8a/xray",  new File(binDir, "xray"));
        copyIfMissing(ctx.getAssets(), "bin/arm64-v8a/tun2socks", new File(binDir, "tun2socks"));

        // give execute permission
        runChmod(new File(binDir, "xray"));
        runChmod(new File(binDir, "tun2socks"));
    }

    private static void copyIfMissing(AssetManager am, String assetPath, File out) throws IOException {
        if (out.exists() && out.length() > 0) return;
        try (InputStream in = am.open(assetPath); FileOutputStream os = new FileOutputStream(out)) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) os.write(buf, 0, n);
        }
    }

    private static void runChmod(File f) throws IOException, InterruptedException {
        Process p = new ProcessBuilder("chmod", "700", f.getAbsolutePath()).start();
        int code = p.waitFor();
        if (code != 0) throw new IOException("chmod failed: " + code);
    }
}
