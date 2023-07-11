package com.ajaxjs.net.download;


import com.ajaxjs.util.logger.LogHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 线程池实现批量下载文件
 *
 * @author shucheng
 */
public class DownloadUtil {
    private static final LogHelper LOGGER = LogHelper.getLog(DownloadUtil.class);

    /**
     * 下载线程数
     */
    private static final int DOWNLOAD_THREAD_NUM = 14;

    /**
     * 下载线程池
     */
    private static final ExecutorService downloadExecutorService = ThreadUtil.buildDownloadBatchThreadPool(DOWNLOAD_THREAD_NUM);

    /**
     * 文件下载
     *
     * @param fileUrl 文件url,如:<a href="https://img3.doubanio.com//view//photo//s_ratio_poster//public//p2369390663.webp">...</a>
     * @param path    存放路径,如: /opt/img/douban/my.webp
     */
    public static void download(String fileUrl, String path) {
// 判断存储文件夹是否已经存在或者创建成功
        if (!createFolderIfNotExists(path)) {
            LOGGER.warning("We can't create folder:{0}", getFolder(path));
            return;
        }

        InputStream in = null;
        FileOutputStream out = null;

        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            in = conn.getInputStream();
            out = new FileOutputStream(path);
            int len;
            byte[] arr = new byte[1024 * 1000];

            while (-1 != (len = in.read(arr)))
                out.write(arr, 0, len);

            out.flush();
            conn.disconnect();
        } catch (Exception e) {

            LOGGER.warning("Fail to download: {} by {}", fileUrl, e.getMessage());

        } finally {

            try {

                if (null != out) {

                    out.close();

                }

                if (null != in)
                    in.close();
            } catch (Exception e) {
// do nothing

            }
        }
    }

    /**
     * 创建文件夹,如果文件夹已经存在或者创建成功返回true
     *
     * @param path 路径
     * @return boolean
     */

    private static boolean createFolderIfNotExists(String path) {
        String folderName = getFolder(path);

        if (folderName.equals(path))
            return true;

        File folder = new File(getFolder(path));

        if (!folder.exists()) {
            synchronized (DownloadUtil.class) {
                if (!folder.exists())
                    return folder.mkdirs();
            }
        }

        return true;
    }

    /**
     * 获取文件夹
     *
     * @param path 文件路径
     * @return String
     */
    private static String getFolder(String path) {
        int index = path.lastIndexOf("/");
        return -1 != index ? path.substring(0, index) : path;
    }

    /**
     * 下载资源
     * issue: 线程池创建过多 最大批量下载为5,请知悉
     *
     * @param resourceMap 资源map, key为资源下载url,value为资源存储位置
     */
    public static void batch(Map<String, String> resourceMap) {
        if (resourceMap == null || resourceMap.isEmpty())
            return;

        try {
            List<String> keys = new ArrayList<>(resourceMap.keySet());
            int size = keys.size();
            int pageNum = getPageNum(size);

            for (int index = 0; index < pageNum; index++) {
                int start = index * DOWNLOAD_THREAD_NUM;
                int last = getLastNum(size, start + DOWNLOAD_THREAD_NUM);
                final CountDownLatch latch = new CountDownLatch(last - start);
                List<String> urlList = keys.subList(start, last);// 获取列表子集

                for (String url : urlList) {// 提交任务
                    Runnable task = new DownloadWorker(latch, url, resourceMap.get(url));
                    downloadExecutorService.submit(task);
                }

                latch.await();
            }
        } catch (Exception e) {
            LOGGER.warning(e);
        }

        LOGGER.info("Download resource map is all done");
    }

    /**
     * 获取最后一个元素
     *
     * @param size  列表长度
     * @param index 下标
     * @return int
     */
    private static int getLastNum(int size, int index) {
        return Math.min(index, size);
    }

    /**
     * 获取划分页面数量
     *
     * @param size 列表长度
     * @return int
     */
    private static int getPageNum(int size) {
        int tmp = size / DOWNLOAD_THREAD_NUM;
        return size % DOWNLOAD_THREAD_NUM == 0 ? tmp : tmp + 1;
    }

    /**
     * 下载线程
     */
    static class DownloadWorker implements Runnable {
        private final CountDownLatch latch;

        private final String url;

        private final String path;

        DownloadWorker(CountDownLatch latch, String url, String path) {
            this.latch = latch;
            this.url = url;
            this.path = path;
        }

        @Override
        public void run() {
            LOGGER.warning("Start batch:[{}] into: [{}]", url, path);
            DownloadUtil.download(url, path);
            LOGGER.debug("Download:[{}] into: [{}] is done", url, path);
            latch.countDown();
        }

    }

}