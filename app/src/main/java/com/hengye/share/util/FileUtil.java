package com.hengye.share.util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yuhy on 16/6/24.
 */
public class FileUtil extends ApplicationUtil{

    public static String readAssetsFile(String file) {
        StringBuilder sb = new StringBuilder();

        try {
            InputStream e = getResources().getAssets().open(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(e, "UTF-8"));
            String readLine = null;

            while((readLine = reader.readLine()) != null) {
                sb.append(readLine);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return sb.toString();
    }


    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return
     */
    public static boolean isFileExist(String path) {
        return new File(path).exists();
    }

    /**
     *
     * @param path
     * @return 如果保存成功, 返回保存图片的绝对路径, 否则返回null
     */
    public static String saveImage(String path) {
        return copyFile(new File(path), createImageFile());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File createImageFile() {
        File image = null;
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String imageFileName = "IMG_" + timeStamp + "_";
            File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), "Camera");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
//        Environment.getExternalStorageDirectory().
//        File image = new File(storageDir, imageFileName + ".jpg");
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    /**
     * 复制文件
     *
     * @param oldPath
     * @param newPath
     * @return 如果复制成功返回图片绝对路径, 否则返回null
     */
    public static String copyFile(String oldPath, String newPath) {
        return copyFile(new File(oldPath), new File(newPath));
    }

    public static String copyFile(File oldFile, File newFile) {
        boolean copyFinish = false;
        try {
            int byteRead;
            InputStream inStream = new FileInputStream(oldFile); // 读入原文件
            FileOutputStream fs = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            while ((byteRead = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteRead);
            }
            inStream.close();
            fs.close();
            copyFinish = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (copyFinish) {
            return newFile.getAbsolutePath();
        }
        return null;
    }
}
