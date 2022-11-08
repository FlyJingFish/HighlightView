# HighlightView
[![](https://jitpack.io/v/FlyJingFish/HighlightView.svg)](https://jitpack.io/#FlyJingFish/HighlightView)

## 支持高亮灯光移动效果的 TextView 和 ImageView

<img src="https://github.com/FlyJingFish/HighlightView/blob/master/screenshot/screenrecording-20221108-150736.gif" width="405px" height="842px" alt="show" />


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
        implementation 'com.github.FlyJingFish:HighlightView:1.0'
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
    app:highlight_text_highlightWidth="40dp"
    app:highlight_text_highlightColor="#E6ffffff"
    app:highlight_text_autoStart="true"
    app:highlight_text_duration="2500"
    app:highlight_text_repeatCount="-1"
    app:highlight_text_repeatMode="restart"
    app:highlight_text_highlightRotateDegrees="30"
    app:highlight_text_startDirection="start_left"
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
    app:highlight_image_highlightWidth="30dp"
    app:highlight_image_highlightColor="#b3ffffff"
    app:highlight_image_autoStart="true"
    app:highlight_image_duration="2500"
    app:highlight_image_repeatCount="-1"
    app:highlight_image_repeatMode="restart"
    app:highlight_image_highlightRotateDegrees="30"
    app:highlight_image_startDirection="start_left"
    android:layout_marginTop="10dp"/>
```

### HighlightImageView 属性一览

| attr                                   |     format      | description |
|----------------------------------------|:---------------:|:-----------:|
| highlight_image_highlightColor         | reference/color |    高亮颜色     |
| highlight_image_duration               |     integer     |    动画时间     |
| highlight_image_repeatCount            |     integer     |   动画循环时间    |
| highlight_image_highlightWidth         |    dimension    |   高亮区域宽度    |
| highlight_image_highlightRotateDegrees |      float      |  高亮区域旋转角度   |
| highlight_image_repeatMode             |      enum       |   动画循环模式    |
| highlight_image_startDirection         |      enum       |  高亮动画开始位置   |
| highlight_image_autoStart              |     boolean     | 是否自动开始高亮动画  |

### HighlightTextView 属性一览

| attr                                  |     format      | description |
|---------------------------------------|:---------------:|:-----------:|
| highlight_text_highlightColor         | reference/color |    高亮颜色     |
| highlight_text_duration               |     integer     |    动画时间     |
| highlight_text_repeatCount            |     integer     |   动画循环时间    |
| highlight_text_highlightWidth         |    dimension    |   高亮区域宽度    |
| highlight_text_highlightRotateDegrees |      float      |  高亮区域旋转角度   |
| highlight_text_repeatMode             |      enum       |   动画循环模式    |
| highlight_text_startDirection         |      enum       |  高亮动画开始位置   |
| highlight_text_autoStart              |     boolean     | 是否自动开始高亮动画  |

### 方法介绍

| attr                 |   description   |
|----------------------|:---------------:|
| startHighlightEffect |      开始动画       |
| stopHighlightEffect  |      结束动画       |
| addLifecycleObserver | 添加生命周期感知，自动暂停结束 |




# 最后推荐我写的另一个库，轻松实现在应用内点击小图查看大图的动画放大效果

- [OpenImage](https://github.com/FlyJingFish/OpenImage)

- [主页查看更多开源库](https://github.com/FlyJingFish)



