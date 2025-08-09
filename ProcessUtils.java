package com.example.nativex;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.*;

public class ProcessUtils {

    public static void copyAssetDir(Context ctx, String assetDir, File outDir) throws IOException {
        AssetManager am = ctx.getAssets();
        if (!outDir.exists()) outDir.mkdirs();
        for (String name : am.list(assetDir)) {
            String src = assetDir + "/" + name;
            File dst = new File(outDir, name);
            try (InputStream in = am.open(src); OutputStream out = new FileOutputStream(dst)) {
                byte[] buf = new byte[8192];
                int n;
                while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
            }
        }
    }

    public static void chmodExecutable(File f) throws IOException, InterruptedException {
        new ProcessBuilder("chmod", "700", f.getAbsolutePath()).start().waitFor();
    }
}
