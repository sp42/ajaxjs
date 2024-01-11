/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.net.http;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 批量下载
 *
 * @author sp42 frank@ajaxjs.com
 */
public class BatchDownload {
    /**
     * 闭锁。另外可参考栅栏 CyclicBarrier
     */
    private final CountDownLatch latch;

    /**
     * 下载列表
     */
    private final String[] arr;

    /**
     * 保存目录
     */
    private final String saveFolder;

    /**
     * 如何命名文件名的函数。若为 null 则使用原文件名
     */
    private final Supplier<String> newFileNameFn;

    /**
     * 创建图片批量下载
     *
     * @param arr           下载列表
     * @param saveFolder    保存目录
     * @param newFileNameFn 如何命名文件名的函数。若为 null 则使用原文件名
     */
    public BatchDownload(String[] arr, String saveFolder, Supplier<String> newFileNameFn) {
        latch = new CountDownLatch(arr.length);

        this.arr = arr;
        this.saveFolder = saveFolder;
        this.newFileNameFn = newFileNameFn;
    }

    /**
     * 单个下载
     *
     * @param url 下载地址
     * @param i   索引
     */
    private void exec(String url, int i) {
        String newFileName;

        try {
            if (newFileNameFn == null)
                newFileName = Get.download(url, saveFolder);
            else
                newFileName = Get.download(url, null, saveFolder, newFileNameFn.get());

            String[] _arr = newFileName.split("\\\\");
            String f = _arr[_arr.length - 1];
            arr[i] = f;
        } finally {
            latch.countDown();// 每个子线程中，不管是否成功，是否有异常
        }
    }

    /**
     * 开始下载
     */
    public void start() {
        for (int i = 0; i < arr.length; i++) {
            final int j = i;
            new Thread(() -> exec(arr[j], j)).start();
        }

        try {
            latch.await(20, TimeUnit.SECONDS); // 给主线程设置一个最大等待超时时间 20秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
