# 安卓实现用户登录界面

这个是我们社团的作业，本人大一，也是刚刚接触安卓，很多东西都很陌生，不懂得问我们的学长，学长觉得问题太简单了不予回答O__O "…然而很多细节困扰了我很久，去网上找方法也费了好多的冤枉时间，所以总结一下这次的经验，希望能为其他初学者省下时间
简书传送门：http://www.jianshu.com/p/a0bf389680ad

---

### 首先布局实现用户界面

 
 ![image](https://github.com/Zzzia/Login/blob/master/app/src/main/res/raw/demo.gif)


作业要求大致布局就是这样了，一个登录界面，一个注册界面，还是很简单的，不多说了

小细节：

1.注意**RelativeLayout**和**FrameLayout**的配合使用

2.方块周围的阴影用

~~~
android:elevation="3dip"
~~~

3.之前把图片放在了drawable文件夹里，设置背景导致了停止运行，其解决方案为把图片放在**mipmap-xxxhdpi**这个文件夹下，测试1m一下的图片应该是没问题的，而drawable文件夹300k就boom了。

4.建立第二个activity后一定要记得在manifest里注册，代码：

~~~
<?xml version="1.
