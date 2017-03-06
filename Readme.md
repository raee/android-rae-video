#Rae Video Player
基于[ijkPlayer](https://github.com/Bilibili/ijkplayer)的开源Android播放器,可自定义UI样式,播放速度流畅,支持广泛的视频格式。


## 开始集成
```
# required,在项目下的/build.gradle 中添加。
allprojects {
    repositories {
        jcenter()
    }
}

# 在你的APP的build.gradle中添加
dependencies {
    # required, enough for most devices.
    compile 'tv.danmaku.ijk.media:ijkplayer-java:0.6.2'
    compile 'tv.danmaku.ijk.media:ijkplayer-armv7a:0.6.2'
    // compile 'com.rae.ui:media:0.0.1' // 还在开发中，有需要可以自己编译一个库来用。
    
    # Other ABIs: 可选的,如果你想支持更多的机型可按需添加。
    compile 'tv.danmaku.ijk.media:ijkplayer-armv5:0.6.2'
    compile 'tv.danmaku.ijk.media:ijkplayer-arm64:0.6.2'
    compile 'tv.danmaku.ijk.media:ijkplayer-x86:0.6.2'
    compile 'tv.danmaku.ijk.media:ijkplayer-x86_64:0.6.2'
}
```

## 截图
![截图](http://dev.timeplay.cn/reavideo/screenshots/raevideo1.jpg?imageView2/2/h/720)

![截图](http://dev.timeplay.cn/reavideo/screenshots/raevideo2.jpg?imageView2/2/h/720)

![截图](http://dev.timeplay.cn/reavideo/screenshots/raevideo3.jpg?imageView2/2/h/720)

![截图](http://dev.timeplay.cn/reavideo/screenshots/raevideo4.jpg?imageView2/2/h/720)

![截图](http://dev.timeplay.cn/reavideo/screenshots/raevideo5.jpg?imageView2/2/h/720)

![截图](http://dev.timeplay.cn/reavideo/screenshots/raevideo6.jpg?imageView2/2/h/720)
