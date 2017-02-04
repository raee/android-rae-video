package com.rae.videodemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LargestLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by ChenRui on 16/10/5 下午4:10.
 */
public class DemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();

    }

    // 初始化图片加载
    private void initImageLoader() {

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(this)
                .threadPoolSize(5)//线程池
                .threadPriority(Thread.NORM_PRIORITY - 2)//线程优先级
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LargestLimitedMemoryCache(2 * 1024 * 1024))//内存缓存
                .memoryCacheSize(2 * 1024 * 1024)//内存缓存大小
                .diskCacheSize(50 * 1024 * 1024)//存储卡缓存大小
                .diskCacheFileCount(500)//存储卡文件个数
                .memoryCacheSizePercentage(13) // default
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // default
                .defaultDisplayImageOptions(new DisplayImageOptions.Builder()
                        //.displayer(new FadeInBitmapDisplayer(300))
                        .showImageOnLoading(R.mipmap.rae_video_placeholder)
                        .showImageForEmptyUri(R.mipmap.rae_video_placeholder)
                        .showImageOnFail(R.mipmap.rae_video_placeholder)
                        .cacheOnDisk(true)
                        .cacheInMemory(true).build()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(configuration);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
