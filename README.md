[![Release](https://jitpack.io/v/ashLikun/EasyPopupWindow.svg)](https://jitpack.io/#ashLikun/EasyPopupWindow)

EasyPopup项目简介
        PopupWindows的工具
## 使用方法

build.gradle文件中添加:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
并且:

```gradle
dependencies {
    compile 'com.github.ashLikun:EasyPopupWindow:{latest version}'
}
```


##详细介绍
EasyPopup 是对官方的PopupWindows的封装，使得使用方便

```java
    EasyPopup popup = new EasyPopup(this, R.layout.popup_view);
    popup.setHeight(500);
    popup.create();
    popup.setBackgroundAlpha(true,.4f);
    popup.showAsDropDown(view);
```