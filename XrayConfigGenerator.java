package com.example.nativex;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;

public class XrayConfigGenerator {

    // کانفیگ بسیار ساده: inbound socks روی 10808 / outbound vless
    public static void writeConfigFromVless(String vlessUri, File out) throws Exception {
        // برای سادگی: مقادیر را از URI به صورت رشته‌ای می‌گذاریم (اینجا برای نمونه host/port/id پارس ساده)
        // شما می‌تونی این پارس را کامل‌تر کنی.

        // نمونهٔ شما:
        String address = "216.9.224.59";
        int port = 443;
        String id = "e3075864-27df-4d15-b181-9c94b0e6a53c";
        String sni = "onlinebazikon.ir";
        String path = "/vTlFuPRGGIPbezDZVgy1yX";

        JSONObject cfg = new JSONObject();

        // inbound socks
        JSONArray inbounds = new JSONArray();
        JSONObject inSocks = new JSONObject();
        inSocks.put("port", 10808);
        inSocks.put("listen", "127.0.0.1");
        inSocks.put("protocol", "socks");
        inSocks.put("settings", new JSONObject().put("udp", true));
        inbounds.put(inSocks);
        cfg.put("inbounds", inbounds);

        // outbound vless
        JSONArray outbounds = new JSONArray();
        JSONObject vless = new JSONObject();
        vless.put("protocol", "vless");

        JSONObject user = new JSONObject();
        user.put("id", id);
        user.put("encryption", "none");

        JSONObject stream = new JSONObject();
        stream.put("network", "httpupgrade");
        stream.put("security", "tls");
        stream.put("tlsSettings", new JSONObject().put("serverName", sni));
        stream.put("httpupgradeSettings", new JSONObject().put("path", path).put("host", sni));

        JSONObject settings = new JSONObject()
                .put("vnext", new JSONArray().put(
                        new JSONObject()
                                .put("address", address)
                                .put("port", port)
                                .put("users", new JSONArray().put(user))
                ));

        vless.put("settings", settings);
        vless.put("streamSettings", stream);

        outbounds.put(vless);
        cfg.put("outbounds", outbounds);

        try (FileWriter fw = new FileWriter(out)) {
            fw.write(cfg.toString());
        }
    }
}
