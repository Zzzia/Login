# 安卓实现用户登录界面

这个是我们社团的作业，本人大一，也是刚刚接触安卓，很多东西都很陌生，不懂得问我们的学长，学长觉得问题太简单了不予回答O__O "…然而很多细节困扰了我很久，去网上找方法也费了好多的冤枉时间，所以总结一下这次的经验，希望能为其他初学者省下时间

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
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    <application
        android:allowBackup="true"
        ...
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        //添加在这个位置，我添加了两个
        <activity android:name=".LoginActivity" />
        <activity android:name=".MyActivity" />
    </application>
</manifest>

~~~

5.按钮用TextView来构建，这样会好看一点，用这个代码会产生水波纹效果：

~~~
android:foreground="?attr/selectableItemBackground"
~~~

6.EditView可以使用代码达到提示效果：

~~~
android:hint="输入账号"
~~~

7.设置返回键

是不是有时候进入了注册界面后习惯性的点返回，想要回到主界面时，程序关闭了？用一下方法可以解决

~~~
public class MainActivity extends Activity {  
      
        @Override  
        protected void onCreate(Bundle savedInstanceState) {  
            super.onCreate(savedInstanceState);  
            setContentView(R.layout.main);  
        }  
        /** 
         * 注意: 
         * super.onBackPressed()会自动调用finish()方法,关闭 
         * 当前Activity. 
         * 若要屏蔽Back键盘,注释该行代码即可 
         */  
        @Override  
        public void onBackPressed() {  
        //这里是重点
            super.onBackPressed();  
            System.out.println("按下了back键   onBackPressed()");         
        }  
~~~



---

### 标题栏设置

这里说的是自带的默认标题栏，我在网上找了很多方法去设置，最后只得出这一套方案实现

##### 隐藏标题栏：

```
getSupportActionBar().hide();
```

#### 设置标题：

```
getSupportActionBar().setTitle("登录");
```

##### 出现返回箭头：

空白activity默认有标题栏，在继承AppCompatActivity的Activity中，直接使用下面代码出现返回箭头

```
getSupportActionBar().setDisplayHomeAsUpEnabled(true);
```

#### 设置返回箭头：

```
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
        //...
        	//按下效果设置为返回
            onBackPressed();
        //...    
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
```

这里我设置按下效果是模拟返回键onBackPressed();

**若要自定义设置标题栏，可以百度ToolBar使用教程**

---

### Activity跳转

#### 有两种跳转方式

第一种是没有传回来信息的方式

~~~
Intent intent = new Intent();
//第一个是当前Activity，第二个是要跳转的Activity
intent.setClass(LoginActivity.this,MyActivity.class);
startActivity(intent);
LoginActivity.this.finish();
~~~

**第二种**(startActivityForResult)比较高级，可以实现业务逻辑，得到子页面传回来的信息，比如说这次我需要搜集用户注册的信息，然后把用户名在登录页面显示出来，以便用户直接输入密码登录。

在主页面里使用startActivityForResult(int requestCode, int resultCode, Intent data)，同时重写onActivityResult（与onCreate同级）,这里requestCode设置为1

在跳转页面里使用Intent，并putExtra传输数据，最后使用this.setResult(int requestCode,Intent intent)

#### 主页面代码：

```
Intent intent = new Intent();
                intent.setClass(MainActivity.this,LoginActivity.class);
                MainActivity.this.startActivityForResult(intent,1);
```

```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    ...
        switch(requestCode){
      	case (1):{...}
      	}
    ...  	
    }
```

这里switch的是requestCode，也就是设置里输入的数字标志，用于多个页面的结果返回，例如，我要再从第三个页面接收返回信息：

```
MainActivity.this.startActivityForResult(intent,2);
```

可以添加case(2)来接收第三个页面的返回信息



#### 跳转页面代码：

```
//设置regist按钮监听
regist.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view) {
            //number psw 为 EditText
            //psw =  			     //(EditText)findViewById(R.id.Regist_password);
            
                username = number.getText().toString();
                password = psw.getText().toString();
			}
                Intent intent = new Intent();
 intent.setClass(LoginActivity.this,MainActivity.class);
                //将输入传递给主界面
                intent.putExtra("username",username);
                intent.putExtra("password",password);
            //关键语句
          LoginActivity.this.setResult(1,intent);
                //传递完毕
                LoginActivity.this.finish();
            }
        });
```

这里，顺序是先执行LoginActivity.this.setResult(1,intent);

然后执行LoginActivity.this.finish();

才能将信息返回，因此，**finish是很重要的**

---

### 文件读写，实现保存账号密码

这个我想了好久好久的，我刚刚入门比较辣鸡O__O "…最后也只能想出以txt保存，然后用集合来搜集数据的解决方式

**首先是在注册页面搜集用户输入的数据并保存为txt文件**

~~~
number = (EditText)findViewById(R.id.Regist_username);
psw = (EditText)findViewById(R.id.Regist_password);
regist = (TextView)findViewById(R.id.Regist_regist);

        //注册按钮监听
        regist.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view) {
                    username = number.getText().toString();
                    password = psw.getText().toString();
                //判断输入是否为空
                if(username.isEmpty()||password.isEmpty()||username.contains("#")){
                    Toast.makeText(LoginActivity.this, "用户名密码不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    //若不为空，保存信息，传递信息，跳转页面
                    save();
                    //sava函数一定要看一下
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,MainActivity.class);
                    //将输入传递给主界面
                    intent.putExtra("username",username);
                    intent.putExtra("password",password);
                    LoginActivity.this.setResult(1,intent);
                    //传递完毕
                    LoginActivity.this.finish();
                }
            }
        });
~~~

sava函数：

~~~
    //用户信息保存
    //将用户名和密码分别放入两个不同的txt文件中
    public void save(){
    String registfile = "registfile.txt";
    String pswfile = "pswfile.txt";
        try {
        //以#号方式分割用户的信息
            String space = "#";
            FileOutputStream outputUsername = openFileOutput(registfile, Activity.MODE_APPEND);
            FileOutputStream outputPsw = openFileOutput(pswfile, Activity.MODE_APPEND);
            //保存数据并添加#号，以便一会儿用split()分割
            String name = username+space;
            String psw = password+space;
            outputUsername.write(name.getBytes());
            outputPsw.write(psw.getBytes());
            outputPsw.flush();
            outputUsername.flush();
            outputPsw.close();
            outputUsername.close();
            //提示保存txt成功
            Toast.makeText(this, "OK!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
~~~

MainActivity代码：

~~~
//在onCreate之前，我写了两组集合用于临时存放账号密码
//ArrayList<String> ListUser = new ArrayList<String>();
//ArrayList<String> ListPassword = new ArrayList<String>();
        //登录按钮监听
        login.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view) {
                try{
                    FileInputStream inputUsername = openFileInput(registfile);
                    byte[] bytes = new byte[1024];
                    ByteArrayOutputStream userOutputStream = new ByteArrayOutputStream();
                    while(inputUsername.read(bytes)!=-1){
                        userOutputStream.write(bytes,0,bytes.length);
                    }
                    inputUsername.close();
                    userOutputStream.close();
                    String username = new String(userOutputStream.toByteArray());
                    System.out.println(username);
                    //读取密码
                    FileInputStream inputPassword = openFileInput(pswfile);
                    ByteArrayOutputStream pswOutputStream = new ByteArrayOutputStream();
                    while(inputPassword.read(bytes)!=-1){
                        pswOutputStream.write(bytes,0,bytes.length);
                    }
                    inputPassword.close();
                    pswOutputStream.close();
                    String password = new String(pswOutputStream.toByteArray());

                    //将账号密码写入List
                    String[] tokenName = username.split("#");
                    for(int i = 0;i<tokenName.length-1;i++){
                        ListUser.add(tokenName[i]);
                    }
                    String[] tokenPsw = password.split("#");
                    for(int i = 0;i<tokenPsw.length-1;i++){
                        ListPassword.add(tokenPsw[i]);
                    }
                    //写入完成

                    //账号密码匹配
                    String username1 = Euesrname.getText().toString();
                    String password1 = Epassword.getText().toString();
                    
                    int i;
                    for(i=0;i<ListUser.size();i++){
                        //先检查是否有输入的账号
                        if(ListUser.get(i).equals(username1)){
                            //匹配密码
                            if(ListPassword.get(i).equals(password1)){
         Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                           //跳转到登录成功后的界面
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this,MyActivity.class);
                                startActivity(intent);
                                MainActivity.this.finish();
                            }
                            else
                                Toast.makeText(MainActivity.this, "请检查密码是否正确", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(i==ListUser.size()-1){
                        Toast.makeText(MainActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
~~~

---

#### 到此也就结束了，能够实现图片上的功能了

**在这次作业中，我添加了很多有趣的元素，与大家分享下**

---



### 实现再次按下返回键结束程序

这种方案无疑是很人性友好的，不必弹出烦人的对话框，也不会手滑关掉，代码如下，添在onCreate后面

~~~
private long exitTime = 0;
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
        if((System.currentTimeMillis()-exitTime) > 2000){  
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
            exitTime = System.currentTimeMillis();   
        } else {
            finish();
            System.exit(0);
        }
        return true;   
    }
    return super.onKeyDown(keyCode, event);
}
~~~

---

### 最简单的播放音乐方案

~~~
Mediaplayer = new MediaPlayer();
Mediaplayer = MediaPlayer.create(MyActivity.this, R.raw.bravesong);
Mediaplayer.start();
~~~

这东西可以后台，但是我并不知道后台时间长短，貌似各个安卓版本不一样？

停止播放的方法是

~~~
Mediaplayer.pause();
~~~



------

### 启动默认浏览器打开想要打开的网址

```
//设置跳转按钮启动浏览器
        go_web.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);
                MyActivity.this.onPause();
            }
        });
    }
```
