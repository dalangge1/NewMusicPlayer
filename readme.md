仿网易云播放器v1.0

支持功能：推荐歌单，搜索，个人中心，我的歌单，最新专辑（暂时不能点进去）。还有一些零碎的功能因为时间问题没有加入进去，做的比较粗糙。

有旋转的图片view，通知栏显示音乐控制音乐播放，随机播放等

展示图片在“pictures”文件夹下

核心知识点：四大组件，自定义view，滑动冲突（事件分发），反射（在json解析中），线程，图片缓存，部分MVVM（只有播放器部分用了MVVM架构，其他部分还是自己的fragment管自己的数据和网络请求，通过callback）。自己实现了JSonObject、JSonArray、JSonEval（类似Gson）解析，自己实现了类似于Glide的图片加载功能。

遇到问题：有时dataBinding找不到自定义view类、swipeRefreshView多recyclerView嵌套时滑动冲突导致子recyclerView无法滑动（已通过自定义recyclerView解决）。navigation因为是第一次用所以有点粗糙。

登录时可直接点击“立即登录按钮”，其他操作步骤与考核文档大致相同。

![image](https://github.com/sandyz987/NewMusicPlayer/blob/master/pictures/Screenshot_20200503_210754_com.example.newmusicpl.jpg)

![image](https://github.com/sandyz987/NewMusicPlayer/blob/master/pictures/Screenshot_20200503_210745_com.example.newmusicpl.jpg)

![image](https://github.com/sandyz987/NewMusicPlayer/blob/master/pictures/Screenshot_20200503_210754_com.example.newmusicpl.jpg)

![image](https://github.com/sandyz987/NewMusicPlayer/blob/master/pictures/Screenshot_20200503_210802_com.example.newmusicpl.jpg)

![image](https://github.com/sandyz987/NewMusicPlayer/blob/master/pictures/Screenshot_20200503_210805_com.example.newmusicpl.jpg)

![image](https://github.com/sandyz987/NewMusicPlayer/blob/master/pictures/Screenshot_20200503_221917_com.example.newmusicpl.jpg)
