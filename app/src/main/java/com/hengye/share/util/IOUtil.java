package com.hengye.share.util;

import java.io.Closeable;

/**
 * Created by yuhy on 2017/2/18.
 */

public class IOUtil {

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }
}
