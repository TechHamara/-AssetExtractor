package com.michaelam.assetextractor;

import android.content.Context;
import android.util.Log;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


    @DesignerComponent(
            version = com.michaelam.assetextractor.AssetExtractor.VERSION,
            description =
                    "An Extension to extract an assets from app.",
            category = ComponentCategory.EXTENSION, nonVisible = true,
            iconName = "https://i.ibb.co/RQyh3Lm/iconpng.png")
    @SimpleObject(external = true)
    @UsesPermissions(permissionNames = "android.permission.READ_EXTERNAL_STORAGE")
    public class AssetExtractor extends AndroidNonvisibleComponent {
        public static final int VERSION = 1;
        private static Context context;


        public AssetExtractor(ComponentContainer container) {
            super(container.$form());
            context = container.$context();
        }

        @SimpleFunction(description = "Extract asset from the project's media list.")
        public String GetAssetAbsolutePath(String fileName) {
            File cachedAsset = new File(context.getCacheDir(), fileName);
            String assetFullFilePath = "";

            if(cachedAsset.exists()) {
              assetFullFilePath = cachedAsset.getAbsolutePath();
            }
            else {
                    try
                    {
                        assetFullFilePath = ExtractAssetToCache(fileName).getAbsolutePath();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return "file://" + assetFullFilePath;
        }


        public static File ExtractAssetToCache(String file) throws java.io.IOException {
            File cacheFile = new File(context.getCacheDir(), file);
            try {
                try (InputStream inputStream = context.getAssets().open(file)) {
                    try (FileOutputStream outputStream = new FileOutputStream(cacheFile)) {
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = inputStream.read(buf)) > 0) {
                            outputStream.write(buf, 0, len);
                        }
                    }
                }
            } catch (IOException e) {
                throw new IOException("Could not open asset/asset does not exist", e);
            }
            return cacheFile;
        }

        public Boolean isDevelopment()
        {
            return context.getFilesDir().getAbsolutePath().contains("companion");
        }
}
