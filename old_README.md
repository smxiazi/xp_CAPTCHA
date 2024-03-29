# xp_CAPTCHA
## 简介
* xp_CAPTCHA burp验证码识别插件，中文名：瞎跑验证码
* 调用 http://www.kuaishibie.cn 网站的验证码识别接口
* 如果github图片加载不出来，请去博客看使用教程：http://www.nmd5.com/posts/2021-01-25-25/

********
[使用教程](https://github.com/smxiazi/xp_CAPTCHA#%E4%BD%BF%E7%94%A8%E6%95%99%E7%A8%8B)

********

### 2022-06-12 更新

修复了个bug

********

### 2022-02-23 更新可把账号密码填写在插件脚本中

配置详情如下

```
http://www.kuaishibie.cn 处注册账号，用这个平台的原因是因为注册方便。
在http头部填入以下格式
xiapao:账号,密码,验证码url,验证码类型
验证码类型：纯数字=1，纯英文=2，数字英文混合=3

如：
xiapao:test,123456,http://www.xxx.coom/get-validate-code,3

或（如果不输入验证码类型则默认 数字英文混合 ）
xiapao:test,123456,http://www.xxx.coom/get-validate-code

或（需要填写账号密码在脚本的第12行与第13行）
xiapao:http://www.xxx.coom/get-validate-code,3


或（如果不输入验证码类型则默认 数字英文混合 ）
xiapao:http://www.xxx.coom/get-validate-code

```

备注：如果脚本中已填写账号密码，又在头部中xiapao字段也填写了账号密码，则`优先使用xiapao字段中的账号密码`

![image](https://user-images.githubusercontent.com/30351807/155261762-d40c7b3c-4aa3-4409-97bb-ffeb9d3b670a.png)

![image](https://user-images.githubusercontent.com/30351807/155261261-ca830cac-bcc5-4fe0-91bd-30308adbbae3.png)

![image](https://user-images.githubusercontent.com/30351807/155261414-a96c0dc2-107c-4006-bd85-8fadb84ac90a.png)




********

## 使用教程

* 导入插件

![file](http://www.nmd5.com/wp-content/uploads/2021/01/600edb3d8331e.png)

![file](http://www.nmd5.com/wp-content/uploads/2021/01/600efaa4375be.png)

* Attack type处选择 Pitchfork,在http头部位置插入配置信息,选中需要跑字段和验证码。

#### 配置详情如下

```
http://www.kuaishibie.cn 处注册账号，用这个平台的原因是因为注册方便。
在http头部填入以下格式
xiapao:账号,密码,验证码url,验证码类型
验证码类型：纯数字=1，纯英文=2，数字英文混合=3
如：
xiapao:test,123456,http://www.xxx.coom/get-validate-code,3
```
*****

![file](http://www.nmd5.com/wp-content/uploads/2021/01/600ee04b44979.png?time=111)

*****

![file](http://www.nmd5.com/wp-content/uploads/2021/01/600efb371b7b7.png)

* 导入字典

![file](http://www.nmd5.com/wp-content/uploads/2021/01/600edf10c571c.png)

![file](http://www.nmd5.com/wp-content/uploads/2021/01/600edf2b2ce28.png)

![image](https://user-images.githubusercontent.com/30351807/155262556-9f8c3ef3-d0fb-4415-b918-6fd9f5c59d12.png)


******

* 开始跑他

![file](http://www.nmd5.com/wp-content/uploads/2021/01/600efc8944640.png)

![file](http://www.nmd5.com/wp-content/uploads/2021/01/600efc9a0a69d.png)

### 就是钱烧的有点快

![file](http://www.nmd5.com/wp-content/uploads/2021/01/600edff956db7.png)

*****

### 注意事项

* 我使用默认线程跑是正常的，但是如果出现验证码失效或者不对的问题，可以调成单线程试试
* 数据包一定要带cookie

*******

### 更新

* 2021-1-25 添加指定验证码类型
* 2021-1-26 修复部分ssl证书错误问题
* 2022-02-23 更新可把账号密码填写在插件脚本中
* 2022-02-27 支持验证码返回包为json格式

*********

### 其他

* 博客：http://www.nmd5.com/
* 干货集中营：http://www.nmd5.com/test/index.php
