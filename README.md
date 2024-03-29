# HighlightView
[![](https://jitpack.io/v/FlyJingFish/HighlightView.svg)](https://jitpack.io/#FlyJingFish/HighlightView)
[![GitHub stars](https://img.shields.io/github/stars/FlyJingFish/HighlightView.svg)](https://github.com/FlyJingFish/HighlightView/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/FlyJingFish/HighlightView.svg)](https://github.com/FlyJingFish/HighlightView/network)
[![GitHub issues](https://img.shields.io/github/issues/FlyJingFish/HighlightView.svg)](https://github.com/FlyJingFish/HighlightView/issues)
[![GitHub license](https://img.shields.io/github/license/FlyJingFish/HighlightView.svg)](https://github.com/FlyJingFish/HighlightView/blob/master/LICENSE)

## 支持高亮灯光移动效果的 TextView、ImageView、FrameLayout、LinearLayout、RelativeLayout

<img src="https://github.com/FlyJingFish/HighlightView/blob/master/screenshot/screenrecording-20221109-134351.gif" width="405px" height="842px" alt="show" />


## 第一步，根目录build.gradle

```gradle
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
## 第二步，需要引用的build.gradle （最新版本[![](https://jitpack.io/v/FlyJingFish/HighlightView.svg)](https://jitpack.io/#FlyJingFish/HighlightView)）

```gradle
    dependencies {
        implementation 'com.github.FlyJingFish:HighlightView:1.2.1'
    }
```
## 第三步，使用说明

**设置 HighlightTextView 示例**

```xml
 <com.flyjingfish.highlightviewlib.HighlightTextView
    android:id="@+id/highlightTextView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text=" Hello World! "
    android:gravity="center"
    android:textStyle="bold"
    android:textColor="@color/black"
    android:layout_marginTop="10dp"
    app:highlight_view_highlightWidth="40dp"
    app:highlight_view_highlightColor="#E6ffffff"
    app:highlight_view_autoStart="true"
    app:highlight_view_duration="2500"
    app:highlight_view_repeatCount="-1"
    app:highlight_view_repeatMode="restart"
    app:highlight_view_highlightRotateDegrees="30"
    app:highlight_view_startDirection="from_left"
    android:textSize="30sp"/>
```

**设置 HighlightImageView 示例**

```xml

<com.flyjingfish.highlightviewlib.HighlightImageView
    android:id="@+id/highlightImageView"
    android:layout_width="154dp"
    android:layout_height="64dp"
    android:scaleType="fitXY"
    android:src="@mipmap/e_bank"
    app:highlight_view_highlightWidth="30dp"
    app:highlight_view_highlightColor="#b3ffffff"
    app:highlight_view_autoStart="true"
    app:highlight_view_duration="2500"
    app:highlight_view_repeatCount="-1"
    app:highlight_view_repeatMode="restart"
    app:highlight_view_highlightRotateDegrees="30"
    app:highlight_view_startDirection="from_left"
    android:layout_marginTop="10dp"/>
```

**设置 ViewGroup 示例**

```xml

<com.flyjingfish.highlightviewlib.HighlightLinearLayout
    android:id="@+id/highlightLinearLayout"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:gravity="center"
    app:highlight_view_highlightWidth="40dp"
    app:highlight_view_highlightColor="#E6ffffff"
    app:highlight_view_autoStart="true"
    app:highlight_view_duration="2500"
    app:highlight_view_repeatCount="-1"
    app:highlight_view_repeatMode="restart"
    app:highlight_view_highlightRotateDegrees="30"
    app:highlight_view_startDirection="from_left">
    <ImageView
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:scaleType="fitXY"
        android:src="@mipmap/camera"
        android:layout_marginTop="10dp"/>
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="视频连接中"
        android:gravity="center"
        android:textStyle="bold"
        android:textColor="@color/black"
        android:textSize="16sp"/>

</com.flyjingfish.highlightviewlib.HighlightLinearLayout>
```

### 属性一览

| attr                                  |     format      |                                 description                                 |
|---------------------------------------|:---------------:|:---------------------------------------------------------------------------:|
| highlight_view_highlightColor         | reference/color |                                  高亮中心区域颜色                                   |
| highlight_view_highlightWidth         |    dimension    |                                   高亮区域宽度                                    |
| highlight_view_highlightRotateDegrees |      float      |                                  高亮区域旋转角度                                   |
| highlight_view_startDirection         |      enum       | 高亮动画开始位置（from_left 从左边开始/from_top 从上边开始/from_right 从右边开始/from_bottom 从下边开始） |
| highlight_view_repeatCount            |     integer     |                                   动画循环次数                                    |
| highlight_view_repeatMode             |      enum       |           动画循环模式（restart 动画结束后从开始位置循环下一次/reverse 动画结束后从结束位置循环下一次）           |
| highlight_view_duration               |     integer     |                                  动画循环一次的时间                                  |
| highlight_view_autoStart              |     boolean     |                             开始显示控件时，是否自动开始高亮动画                              |

### HighlightAnimHolder 部分方法介绍(更多方法自行探索吧～～)

| attr                  |     description     |
|-----------------------|:-------------------:|
| startHighlightEffect  | 开始动画(动画相关参数设置后在此起效) |
| stopHighlightEffect   |        结束动画         |
| resumeHighlightEffect |        继续动画         |
| pauseHighlightEffect  |        暂停动画         |
| isPaused              |       是否暂停动画        |
| setInterpolator       |        设置插值器        |

**例如**

```java

 highlightTextView.getHighlightAnimHolder().stopHighlightEffect()

```



## 最后推荐我写的另外一些库

- [OpenImage 轻松实现在应用内点击小图查看大图的动画放大效果](https://github.com/FlyJingFish/OpenImage)

- [AndroidAOP 一个注解就可请求权限，禁止多点，切换线程等等，更可定制出属于你的 Aop 代码](https://github.com/FlyJingFish/AndroidAOP)

- [主页查看更多开源库](https://github.com/FlyJingFish)



