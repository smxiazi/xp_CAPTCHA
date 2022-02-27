#!/usr/bin/env python
#coding:gbk
from burp import IBurpExtender
from burp import IIntruderPayloadGeneratorFactory
from burp import IIntruderPayloadGenerator
import base64
import json
import re
import urllib2
import ssl

user = "" #账号
passwd = "" #密码

class BurpExtender(IBurpExtender, IIntruderPayloadGeneratorFactory):
    def registerExtenderCallbacks(self, callbacks):
        #注册payload生成器
        callbacks.registerIntruderPayloadGeneratorFactory(self)
        #插件里面显示的名字
        callbacks.setExtensionName("xp_CAPTCHA_api")
        print 'xp_CAPTCHA_api  中文名:瞎跑验证码\nblog：http://www.nmd5.com/\n团队官网：https://www.lonersec.com/ \nThe loner安全团队 by:算命縖子\n\n用法：\n在head头部添加http://www.ttshitu.com的账号密码和验证码的url还有验证码类型，用","隔开\n验证码类型：纯数字=1，纯英文=2，数字英文混合=3 \n\nxiapao:test,123456,http://www.baidu.com/get-validate-code,3\n\n如：\n\nPOST /login HTTP/1.1\nHost: www.baidu.com\nUser-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0\nAccept: text/plain, */*; q=0.01\nAccept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\nContent-Type: application/x-www-form-urlencoded; charset=UTF-8\nX-Requested-With: XMLHttpRequest\nxiapao:test,123456,http://www.baidu.com/get-validate-code,3\nContent-Length: 84\nConnection: close\nCookie: JSESSIONID=24D59677C5EDF0ED7AFAB8566DC366F0\n\nusername=admin&password=admin&vcode=8888\n\n'

    def getGeneratorName(self):
        return "xp_CAPTCHA_api"

    def createNewInstance(self, attack):
        return xp_CAPTCHA_api(attack)

class xp_CAPTCHA_api(IIntruderPayloadGenerator):
    def __init__(self, attack):
        tem = "".join(chr(abs(x)) for x in attack.getRequestTemplate()) #request内容
        cookie = re.findall("Cookie: (.+?)\r\n", tem)[0] #获取cookie
        xp_CAPTCHA = re.findall("xiapao:(.+?)\r\n", tem)[0]
        ssl._create_default_https_context = ssl._create_unverified_context #忽略证书，防止证书报错
        print xp_CAPTCHA+'\n'
        print 'cookie:' + cookie+'\n'
        self.xp_CAPTCHA = xp_CAPTCHA
        self.cookie = cookie
        self.max = 1 #payload最大使用次数
        self.num = 0 #标记payload的使用次数
        self.attack = attack

    def hasMorePayloads(self):
        #如果payload使用到了最大次数reset就清0
        if self.num == self.max:
            return False  # 当达到最大次数的时候就调用reset
        else:
            return True

    def getNextPayload(self, payload):  # 这个函数请看下文解释
        xp_CAPTCHA_list = self.xp_CAPTCHA.split(',')
        if len(xp_CAPTCHA_list) == 4:
            xp_CAPTCHA_user = xp_CAPTCHA_list[0]  # 验证码平台账号
            xp_CAPTCHA_pass = xp_CAPTCHA_list[1]  # 验证码平台密码
            xp_CAPTCHA_url = xp_CAPTCHA_list[2]  # 验证码url
            xp_CAPTCHA_type = xp_CAPTCHA_list[3] #验证码类型
        elif len(xp_CAPTCHA_list) == 3:
            xp_CAPTCHA_user = xp_CAPTCHA_list[0]  # 验证码平台账号
            xp_CAPTCHA_pass = xp_CAPTCHA_list[1]  # 验证码平台密码
            xp_CAPTCHA_url = xp_CAPTCHA_list[2]  # 验证码url
        elif len(xp_CAPTCHA_list) == 2:
            xp_CAPTCHA_url = xp_CAPTCHA_list[0]  # 验证码url
            xp_CAPTCHA_type = xp_CAPTCHA_list[1]  # 验证码类型
        elif len(xp_CAPTCHA_list) == 1:
            xp_CAPTCHA_url = xp_CAPTCHA_list[0]  # 验证码url

        #print xp_CAPTCHA_user,xp_CAPTCHA_pass,xp_CAPTCHA_url
        headers = {"User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36","Cookie":self.cookie}
        request = urllib2.Request(xp_CAPTCHA_url,headers=headers)
        CAPTCHA = urllib2.urlopen(request) #获取图片

        # 判断验证码数据包是否为json格式
        if re.findall('"\s*:\s*.?"', CAPTCHA):
            CAPTCHA = CAPTCHA.split('"')
            CAPTCHA.sort(key=lambda i: len(i), reverse=True)  # 按照字符串长度排序
            CAPTCHA = CAPTCHA[0].split(',')
            CAPTCHA.sort(key=lambda i: len(i), reverse=True)  # 按照字符串长度排序
            CAPTCHA_base64 = CAPTCHA[0]
        else:
            CAPTCHA_base64 = base64.b64encode(CAPTCHA)  # 把图片base64编码

        if len(xp_CAPTCHA_list) == 4:
            data = '{"username":"%s","password":"%s","image":"%s","typeid":"%s"}'%(xp_CAPTCHA_user,xp_CAPTCHA_pass,CAPTCHA_base64,xp_CAPTCHA_type)
        elif len(xp_CAPTCHA_list) == 3:
            data = '{"username":"%s","password":"%s","image":"%s"}'%(xp_CAPTCHA_user,xp_CAPTCHA_pass,CAPTCHA_base64)
        elif len(xp_CAPTCHA_list) == 2:
            data = '{"username":"%s","password":"%s","image":"%s","typeid":"%s"}' % (user, passwd, CAPTCHA_base64, xp_CAPTCHA_type)
        elif len(xp_CAPTCHA_list) == 1:
            data = '{"username":"%s","password":"%s","image":"%s"}' % (user, passwd, CAPTCHA_base64)

        #print data

        request = urllib2.Request('http://api.ttshitu.com/predict', data,{'Content-Type': 'application/json'})
        response = urllib2.urlopen(request).read()
        print(response)
        result = json.loads(response)
        if result['success']:
            code = result["data"]["result"]
        else:
            code = '0000'
        print code

        return code

    def reset(self):
        self.num = 0  # 清零
        return
