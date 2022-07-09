package burp;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.*;
import java.awt.event.ItemListener;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class BurpExtender implements IBurpExtender, ITab, IHttpListener
{
    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;
    private JSplitPane splitPane;
    public PrintWriter stdout;
    int switchs = 1; //开关 0关 1开
    int clicks_Repeater=0;//64是监听 0是关闭
    int clicks_Intruder=0;//32是监听 0是关闭
    int xiapao_count = 0;//用于判断是第几个验证码
    String api_url = "http://api.kuaishibie.cn/predict";
    String api_user = "";
    String api_password = "";


    String captcha_url_1;//验证码url
    String captcha_url_2;
    String captcha_url_3;
    String captcha_url_4;
    String captcha_url_5;
    int captcha_modular_1;//验证码模式
    int captcha_modular_2;
    int captcha_modular_3;
    int captcha_modular_4;
    int captcha_modular_5;
    JTextArea jta;//存放日志输入


    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks)
    {
        //输出
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        this.stdout.println("hello xp_CAPTCHA_api!");
        this.stdout.println("你好 欢迎使用 瞎跑api!");
        this.stdout.println("version:2.1");

        // keep a reference to our callbacks object
        this.callbacks = callbacks;

        // obtain an extension helpers object
        helpers = callbacks.getHelpers();

        // set our extension name
        callbacks.setExtensionName("xp_CAPTCHA_api V2.1");

        // create our UI
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {

                // main split pane
                splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                JSplitPane splitPanes = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

                //左边框的内容
                JPanel jp=new JPanel();
                JLabel jl_0=new JLabel("    远程收费api接口：");
                JTextField txtfield_0=new JTextField("http://www.kuaishibie.cn/",1);
                txtfield_0.setEditable(false);//不可编辑
                JLabel jl_00=new JLabel("");
                jp.setLayout(new GridLayout(28, 1));
                JLabel jl_1=new JLabel("    验证码编号：1  关键字为：@xiapao_api@1@    请在下列框中填写对应的验证码URL地址");
                JTextField txtfield_1=new JTextField(1);
                JLabel jl_2=new JLabel("");
                JLabel jl_3=new JLabel("    验证码编号：2  关键字为：@xiapao_api@2@    请在下列框中填写对应的验证码URL地址");
                JTextField txtfield_2=new JTextField(1);
                JLabel jl_4=new JLabel("");
                JLabel jl_5=new JLabel("    验证码编号：3  关键字为：@xiapao_api@3@    请在下列框中填写对应的验证码URL地址");
                JTextField txtfield_3=new JTextField(1);
                JLabel jl_6=new JLabel("");
                JLabel jl_7=new JLabel("    验证码编号：4  关键字为：@xiapao_api@4@    请在下列框中填写对应的验证码URL地址");
                JTextField txtfield_4=new JTextField(1);
                JLabel jl_8=new JLabel("");
                JLabel jl_9=new JLabel("    验证码编号：5  关键字为：@xiapao_api@5@    请在下列框中填写对应的验证码URL地址");
                JTextField txtfield_5=new JTextField(1);
                JLabel jl_10=new JLabel("");

                String[] listData = new String[]{"纯数字", "纯数字2", "纯英文", "纯英文2","数英混合", "数英混合2", "闪动GIF", "问答题","计算题", "快速计算题", "快速计算题2", "汉字"};
                JLabel rb_url_1_1=new JLabel("请选择对应的验证码类型：");
                final JComboBox<String> rb_url_1_2=new JComboBox<String>(listData);
                rb_url_1_2.setSelectedIndex(4);// 设置默认选中的条目


                JLabel rb_url_2_1=new JLabel("请选择对应的验证码类型：");
                final JComboBox<String> rb_url_2_2=new JComboBox<String>(listData);
                rb_url_2_2.setSelectedIndex(4);// 设置默认选中的条目

                JLabel rb_url_3_1=new JLabel("请选择对应的验证码类型：");
                final JComboBox<String> rb_url_3_2=new JComboBox<String>(listData);
                rb_url_3_2.setSelectedIndex(4);// 设置默认选中的条目


                JLabel rb_url_4_1=new JLabel("请选择对应的验证码类型：");
                final JComboBox<String> rb_url_4_2=new JComboBox<String>(listData);
                rb_url_4_2.setSelectedIndex(4);// 设置默认选中的条目

                JLabel rb_url_5_1=new JLabel("请选择对应的验证码类型：");
                final JComboBox<String> rb_url_5_2=new JComboBox<String>(listData);
                rb_url_5_2.setSelectedIndex(4);// 设置默认选中的条目


                //添加到面板上
                jp.add(jl_0);
                jp.add(txtfield_0);
                jp.add(jl_00);

                jp.add(jl_1);
                jp.add(txtfield_1);
                jp.add(rb_url_1_1);
                jp.add(rb_url_1_2);
                jp.add(jl_2);

                jp.add(jl_3);
                jp.add(txtfield_2);
                jp.add(rb_url_2_1);
                jp.add(rb_url_2_2);
                jp.add(jl_4);

                jp.add(jl_5);
                jp.add(txtfield_3);
                jp.add(rb_url_3_1);
                jp.add(rb_url_3_2);
                jp.add(jl_6);

                jp.add(jl_7);
                jp.add(txtfield_4);
                jp.add(rb_url_4_1);
                jp.add(rb_url_4_2);
                jp.add(jl_8);

                jp.add(jl_9);
                jp.add(txtfield_5);
                jp.add(rb_url_5_1);
                jp.add(rb_url_5_2);
                jp.add(jl_10);

                //右边框上面的内容
                JPanel jps=new JPanel();
                jps.setLayout(new GridLayout(12, 1)); //六行一列
                JLabel jls=new JLabel("插件名：瞎跑 author：算命縖子");    //创建一个标签
                JLabel jls_1=new JLabel("blog:www.nmd5.com");
                JLabel jls_2=new JLabel("版本：xp_CAPTCHA_api V2.1");
                JLabel jls_3=new JLabel("感谢名单：小白(Assassins)");
                JCheckBox chkbox1=new JCheckBox("启动插件", true);//创建指定文本和状态的复选框
                JCheckBox chkbox2=new JCheckBox("监控Intruder");//创建指定文本的复选框
                JCheckBox chkbox3=new JCheckBox("监控Repeater");
                JButton btn1=new JButton("保存配置");
                JLabel jls_5=new JLabel("修改任何配置都记得点击保存");

                JTextField user=new JTextField("账户",1);
                JTextField password=new JTextField("密码",1);
                JButton dl=new JButton("登录");


                //右边框下面的内容
                JPanel jps_2=new JPanel();
                jta=new JTextArea(18,16);
                jta.setText("由于调用的是第三方验证码识别网站，请填写 http://www.kuaishibie.cn 的账户密码，识别验证码收费为1块钱500次。");
                jta.setLineWrap(true);//自动换行
                jta.setEditable(false);//不可编辑
                JScrollPane jsp=new JScrollPane(jta);    //将文本域放入滚动窗口
                JButton btn2=new JButton("清空日志");
                JLabel jls_4=new JLabel("说明：在验证码处填写对应关键字即可");
                JLabel jls_6=new JLabel("备注：验证码返回包为json格式也支持");
                JLabel jls_7=new JLabel("注意：爆破时，线程记得设置成1！！！");
                jls_7.setForeground(Color.red);

                //添加复选框监听事件
                chkbox1.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(chkbox1.isSelected()){
                            stdout.println("插件 瞎跑 启动");
                            switchs = 1;
                        }else {
                            stdout.println("插件 瞎跑 关闭");
                            switchs = 0;
                        }

                    }
                });
                chkbox2.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (chkbox2.isSelected()){
                            stdout.println("启动 监控Intruder");
                            clicks_Intruder = 32;
                        }else {
                            stdout.println("关闭 监控Intruder");
                            clicks_Intruder = 0;
                        }
                    }
                });
                chkbox3.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(chkbox3.isSelected()) {
                            stdout.println("启动 监控Repeater");
                            clicks_Repeater = 64;
                        }else {
                            stdout.println("关闭 监控Repeater");
                            clicks_Repeater = 0;
                        }
                    }
                });

                //保存配置按钮
                btn1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        captcha_url_1 = txtfield_1.getText();
                        captcha_url_2 = txtfield_2.getText();
                        captcha_url_3 = txtfield_3.getText();
                        captcha_url_4 = txtfield_4.getText();
                        captcha_url_5 = txtfield_5.getText();
                        stdout.println(captcha_url_1);
                        stdout.println(captcha_url_2);
                        stdout.println(captcha_url_3);
                        stdout.println(captcha_url_4);
                        stdout.println(captcha_url_5);


                        jl_00.setText(api_url);
                        jl_00.setForeground(Color.red);
                        if(captcha_url_1.length()!=0){
                            captcha_modular_1 = rb_url_1_2.getSelectedIndex();
                            jl_2.setText("======》验证码1    验证码模式："+rb_url_1_2.getItemAt(rb_url_1_2.getSelectedIndex())+"    URL："+captcha_url_1);
                            jl_2.setForeground(Color.red);
                        }else {
                            jl_2.setText("");
                        }
                        if (captcha_url_2.length()!=0){
                            captcha_modular_1 = rb_url_2_2.getSelectedIndex();
                            jl_4.setText("======》验证码2    验证码模式："+rb_url_2_2.getItemAt(rb_url_2_2.getSelectedIndex())+"    URL："+captcha_url_2);
                            jl_4.setForeground(Color.red);
                        }
                        else {
                            jl_4.setText("");
                        }
                        if (captcha_url_3.length()!=0){
                            captcha_modular_1 = rb_url_3_2.getSelectedIndex();
                            jl_6.setText("======》验证码3    验证码模式："+rb_url_3_2.getItemAt(rb_url_3_2.getSelectedIndex())+"    URL："+captcha_url_3);
                            jl_6.setForeground(Color.red);
                        }else {
                            jl_6.setText("");
                        }
                        if (captcha_url_4.length()!=0) {
                            captcha_modular_1 = rb_url_4_2.getSelectedIndex();
                            jl_8.setText("======》验证码4    验证码模式：" + rb_url_4_2.getItemAt(rb_url_4_2.getSelectedIndex()) + "    URL：" + captcha_url_4);
                            jl_8.setForeground(Color.red);
                        }else {
                            jl_8.setText("");
                        }
                        if (captcha_url_5.length()!=0){
                            captcha_modular_1 = rb_url_5_2.getSelectedIndex();
                            jl_10.setText("======》验证码5    验证码模式："+rb_url_5_2.getItemAt(rb_url_5_2.getSelectedIndex())+"    URL："+captcha_url_5);
                            jl_10.setForeground(Color.red);
                        }else {
                            jl_10.setText("");
                        }
                    }
                });
                //清空按钮
                btn2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jta.setText("");
                    }
                });
                //登录按钮
                dl.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] login_data = sendGet("http://api.kuaishibie.cn/queryAccountInfo.json","username="+user.getText()+"&password="+password.getText(),"null");
                        stdout.println(login_data[0]);
                        if (login_data[0].indexOf("true") != -1){
                            api_user = user.getText();
                            api_password = password.getText();
                            jl_0.setText("    远程收费api接口：            用户名："+api_user);
                            Pattern re_p = Pattern.compile("\"balance\":\"(.*?)\",\"consumed\":\"(.*?)\",\"successNum\":\"(.*?)\"");
                            Matcher re_m = re_p.matcher(login_data[0]);
                            String balance = "";//实时余额
                            String consumed = "";//实时总消费
                            String successNum = "";//实时总识别成功的次数
                            if (re_m.find()){
                                balance = String.valueOf(re_m.group(1));//实时余额
                                consumed = String.valueOf(re_m.group(2));//实时总消费
                                successNum = String.valueOf(re_m.group(3));//实时总识别成功的次数
                                stdout.println(balance);
                                stdout.println(re_m.group(2));
                                stdout.println(re_m.group(3));

                            }
                            jta.insert("登录成功\n余额："+balance+"\n总消费:"+consumed+"\n"+"总识别成功的次数:"+successNum+"\n\n",0);//结果输出到插件界面
                        }else {
                            jl_0.setText("    远程收费api接口：            用户名或密码错误");
                            api_user = "";
                            api_password = "";
                            jta.insert("登录失败，请确认账户密码是否正确！\n\n",0);//结果输出到插件界面
                        }



                    }
                });

                jps.add(jls);
                jps.add(jls_1);
                jps.add(jls_2);
                jps.add(jls_3);
                jps.add(chkbox1);
                jps.add(chkbox2);
                jps.add(chkbox3);
                jps.add(btn1);
                jps.add(jls_5);
                jps.add(user);
                jps.add(password);
                jps.add(dl);

                jps_2.add(jsp);
                jps_2.add(btn2);
                jps_2.add(jls_4);
                jps_2.add(jls_6);
                jps_2.add(jls_7);


                //右边
                splitPanes.setLeftComponent(jps);//上面
                splitPanes.setRightComponent(jps_2);//下面

                //整体分布
                splitPane.setLeftComponent(jp);//添加在左面
                splitPane.setRightComponent(splitPanes);//添加在右面
                splitPane.setDividerLocation(1000);//设置分割的大小

                // customize our UI components
                callbacks.customizeUiComponent(splitPane);
                callbacks.customizeUiComponent(jps);
                callbacks.customizeUiComponent(jp);
                callbacks.customizeUiComponent(jps_2);

                // add the custom tab to Burp's UI
                callbacks.addSuiteTab(BurpExtender.this);

                // register ourselves as an HTTP listener
                callbacks.registerHttpListener(BurpExtender.this);

            }
        });
    }

    @Override
    public String getTabCaption()
    {
        return "xia Pao_api";
    }

    @Override
    public Component getUiComponent()
    {
        return splitPane;
    }

    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo)
    {

        if(switchs == 1){//插件开关
            if(toolFlag == clicks_Repeater || toolFlag == clicks_Intruder){//监听Repeater、Intruder
                if(messageIsRequest){//请求包
                    String Request_data = helpers.bytesToString(messageInfo.getRequest());

                    if(Request_data.indexOf("@xiapao_api@") != -1){//判断请求包中是否带有特征

                        Pattern re_p = Pattern.compile("@xiapao_api@(\\d)@");
                        Matcher re_m = re_p.matcher(Request_data);
                        if (re_m.find()){
                            xiapao_count = Integer.parseInt(re_m.group(1));//验证码url编号
                            //stdout.println(xiapao_count);
                        }
                        //stdout.println(helpers.bytesToString(messageInfo.getRequest()));
                        checkVul(messageInfo,xiapao_count);
                    }
                }

            }
        }

    }


    private void checkVul(IHttpRequestResponse baseRequestResponse, int xiapao_count){
        if(api_user.length()<=1 || api_user.length() <=1){
            jta.insert("请先登录！！\n\n",0);//验证码结果输出到插件界面
            return;
        }
        String captcha_url;//验证码url
        int captcha_type;//验证码类型
        if (xiapao_count == 1){
            captcha_url = captcha_url_1;
            captcha_type = captcha_modular_1;
        }else if (xiapao_count == 2){
            captcha_url = captcha_url_2;
            captcha_type = captcha_modular_2;
        }else if (xiapao_count == 3){
            captcha_url = captcha_url_3;
            captcha_type = captcha_modular_3;
        }else if (xiapao_count == 4){
            captcha_url = captcha_url_4;
            captcha_type = captcha_modular_4;
        }else if (xiapao_count == 5){
            captcha_url = captcha_url_5;
            captcha_type = captcha_modular_5;
        }else {
            jta.insert("验证码"+xiapao_count+"：没该验证码编号！\n\n",0);//验证码结果输出到插件界面
            return;
        }
        //1:纯数字、1001:纯数字2、2:纯英文、1002:纯英文2、3:数英混合、1003:数英混合2、4:闪动GIF、66:问答题、11:计算题、1005:快速计算题、5:快速计算题2、16:汉字
        if(captcha_type == 0){
            captcha_type = 1;
        }else if(captcha_type == 1){
            captcha_type = 1001;
        }else if(captcha_type == 2){
            captcha_type = 2;
        }else if(captcha_type == 3){
            captcha_type = 1002;
        }else if(captcha_type == 4){
            captcha_type = 3;
        }else if(captcha_type == 5){
            captcha_type = 1003;
        }else if(captcha_type == 6){
            captcha_type = 4;
        }else if(captcha_type == 7){
            captcha_type = 66;
        }else if(captcha_type == 8){
            captcha_type = 11;
        }else if(captcha_type == 9){
            captcha_type = 1005;
        }else if(captcha_type == 10){
            captcha_type = 5;
        }else if(captcha_type == 11){
            captcha_type = 16;
        }

        List<String> headers = helpers.analyzeRequest(baseRequestResponse).getHeaders();
        String request = helpers.bytesToString(baseRequestResponse.getRequest());
        
        String cookies = "";
        for(String cookie:headers){//获取cookie
            if(cookie.indexOf("Cookie")!=-1){
                cookies = cookie.split(":")[1];
            }
        }

        if(captcha_url.length()<=1){
            jta.insert("验证码"+xiapao_count+"：还未填写对应的验证码地址哟！\n\n",0);//验证码结果输出到插件界面
            return;
        }

        if (cookies.length()<=1){//判断数据包中是否有cookie
            jta.insert("验证码"+xiapao_count+"：该数据包中没找到cookie，需要有cookie的数据包才支持验证码识别！\n\n",0);//验证码结果输出到插件界面
            return;
        }

        //获取验证码
        String[] captcha_data_1 = sendGet(captcha_url,"",cookies);//发送获取验证码请求
        String[] captcha_list;//切割json
        String base64_img="";
        //BurpExtender.this.stdout.println(captcha_data_1[0]);//打印页面内容
        //判断验证码数据包是否为json格式
        String patter_1 =".*\"\\s*:\\s*.?\".*";
        boolean isMatch_1 = Pattern.matches(patter_1, captcha_data_1[0]);
        if(isMatch_1 == true){
            //json格式
            BurpExtender.this.stdout.println("json格式");
            captcha_list = captcha_data_1[0].split("\"");
            Arrays.sort(captcha_list,(a, b) -> Integer.compare(b.length(), a.length()));//按照长度排序
            captcha_list = captcha_list[0].split(",");
            Arrays.sort(captcha_list,(a, b) -> Integer.compare(b.length(), a.length()));//按照长度排序
            base64_img = captcha_list[0];
            //BurpExtender.this.stdout.println(captcha_list[0]);
        }else {
            //判断是不是base64格式
            String patter_2 =".*?data:image/\\D*;base64,.*";
            boolean isMatch_2 = Pattern.matches(patter_2, captcha_data_1[0]);
            if (isMatch_2 == true){
                //base64格式
                BurpExtender.this.stdout.println("base64格式");
                captcha_list = captcha_data_1[0].split(",");
                Arrays.sort(captcha_list,(a, b) -> Integer.compare(b.length(), a.length()));//按照长度排序
                base64_img = captcha_list[0];
                //BurpExtender.this.stdout.println(captcha_list[0]);
            }else {
                //图片格式
                BurpExtender.this.stdout.println("图片格式");
                base64_img = captcha_data_1[1];
            }
        }

        //获取识别成功后的结果
        String param = "{\"username\":\""+api_user+"\",\"password\":\""+api_password+"\",\"typeid\":\""+captcha_type+"\",\"image\":\""+base64_img+"\"}";
        //BurpExtender.this.stdout.println(param);
        String captcha_data_OK = sendPost(api_url,param);
        BurpExtender.this.stdout.println(captcha_data_OK);
        Pattern re_p = Pattern.compile("\"result\":\"(.*?)\"");
        Matcher re_m = re_p.matcher(captcha_data_OK);
        if (re_m.find()){
            captcha_data_OK = re_m.group(1);//验证码url编号
        }


        jta.insert("验证码"+xiapao_count+"："+captcha_data_OK+"\n",0);//验证码结果输出到插件界面

        IRequestInfo analyIRequestInfo = helpers.analyzeRequest(baseRequestResponse);
        int bodyOffset = analyIRequestInfo.getBodyOffset();//通过上面的analyIRequestInfo得到请求数据包体（body）的起始偏移
        byte[] body = request.substring(bodyOffset).replaceAll("@xiapao_api@\\d@",captcha_data_OK).getBytes(StandardCharsets.UTF_8);//通过起始偏移点得到请求数据包体（body）的内容,然后替换

        //把验证码替换上，修改请求包
        for(int i=0;i<=headers.size()-1;i++){
            //BurpExtender.this.stdout.println(headers.get(i));
            if (headers.get(i).indexOf("@xiapao_api@") != -1){
                String captcha_data = headers.get(i).replaceAll("@xiapao_api@\\d@",captcha_data_OK);//正则替换
                //BurpExtender.this.stdout.println(captcha_data);
                headers.set(i,captcha_data);
            }
        }

        byte[] newRequest = helpers.buildHttpMessage(headers,body);
        baseRequestResponse.setRequest(newRequest);//设置最终新的请求包
    }

    public String[] sendGet(String url, String param, String cookie) {
        String result = "";
        String base64_img="";
        String[] rp_all={};
        BufferedReader in = null;
        try {
            String urlNameString = "";
            if(param.length()<=1){
                urlNameString = url;
            }else{
                urlNameString = url + "?" + param;
            }
            URL realUrl = new URL(urlNameString);

            //-----忽略这个请求相关证书。
            trustAllHttpsCertificates();
            HostnameVerifier hv = new HostnameVerifier() {
                @Override
                public boolean verify(String urlHostName, SSLSession session) {
                    System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            //-------

            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 设置连接超时时间
            connection.setConnectTimeout(3000);
            // 建立实际的连接
            connection.connect();

            InputStream IS_connection = connection.getInputStream();


            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = IS_connection.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            //关闭输入流
            IS_connection.close();
            //对字节数组Base64编码
            base64_img = BurpExtender.this.helpers.base64Encode(outStream.toByteArray());//base64图片

            result = outStream.toString();//源码
            rp_all = new String[]{result, base64_img};




        } catch (Exception e) {
            result = url+"   error!!!"+e;
            e.printStackTrace();
            rp_all = new String[]{result,""};
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return rp_all;
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(3000);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }


    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }


}
