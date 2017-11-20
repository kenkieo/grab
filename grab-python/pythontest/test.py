# coding=utf-8

import requests
from lxml import etree
import re
import sys
import os
import yougu
import datetime

now = datetime.datetime.now()

timeDir = now.strftime('%Y-%m-%d %H\'%M\'%S')

reload(sys)
sys.setdefaultencoding('utf-8')


class transCookie:
    def __init__(self, cookie):
        self.cookie = cookie

    def stringToDict(self):
        itemDict = {}
        items = self.cookie.split(';')
        for item in items:
            key = item.split('=')[0].replace(' ', '')
            value = item.split('=')[1]
            itemDict[key] = value
        return itemDict


class OpFile:
    def __init__(self, name):
        print 'OpFile'
        self.name = name
        self.fd = open(name, 'w')

    def writeString(self, s):
        self.fd.write(s)

    def writeStringLine(self, s):
        self.fd.write(s + '\n')

    def close(self):
        self.fd.close()


def checkDir(dir):
    a = os.path.exists(dir)
    if a == False:
        os.makedirs(dir)


class Weibo:
    username = '75023143@qq.com'
    password = '331730216'
    cookies = ''
    allDir = 'html'
    tweetDir = allDir
    followDir = allDir
    atUserDir = allDir

    def __init__(self):
        print 'weibo init'

    def login(self):
        s = requests.Session()

        r = s.get('https://passport.weibo.cn/signin/login')

        # r = s.get('https://passport.weibo.cn/sso/login')
        # r = s.get("http://httpbin.org/cookies")
        print (r.status_code)
        # with open("code.html", "wb") as code:
        # code.write(r.content)
        payload = {
            'username': self.username,
            'password': self.password,
            'savestate': '1',
            'r': '',
            'ec': '0',
            'pagerefer': '',
            'entry': 'mweibo',
            'wentry': '',
            'loginfrom': '',
            'client_id': '',
            'code': '',
            'qq': '',
            'mainpageflag': '1',
            'hff': '',
            'hfp': '',
        }
        headers = {
            'Accept': '*/*',
            'User-Agent': 'Mozilla/5.0 (compatible;MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)',
            'Referer': 'https://passport.weibo.cn/signin/login',
            'Accept - Encoding': 'gzip,deflate,br',
            'Accept-Language': 'zh-cn',
            'Connection': 'keep-alive',
            'Content-Length': '154',
            'Content-Type': 'application/x-www-form-urlencoded',
            'Host': 'passport.weibo.cn',
            # 'Origin': 'https://passport.weibo.cn',
            # 'Cookie': '_T_WM=7fd619ec6d8d51b042dbc6759e2cade7; SCF=AqQK_FqFQGeoZ_Iay3xs7meiCDBBHoDwm_wPqLwOLaA5qMQvcFKqtFNYI_dv5AygCJ8YluTK7k7Xwpu14aaCPD8.; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWP.7RURqVHHo6YOHH5dDkQ5JpX5K-hUgL.Fo2N1h.EShMfeh-2dJLoI7LeIg4r9PS0MNet; SUB=_2A2509cfeDeThGedJ41sT9CnJyzmIHXVUGemWrDV6PUJbkdANLULEkW0tu1bctF0X21n7aVwCdW-3vxqw8w..; SUHB=0WGNCw-giRxknW'
            # 'Cookie':'_T_WM=7fd619ec6d8d51b042dbc6759e2cade7; SCF=AqQK_FqFQGeoZ_Iay3xs7meiCDBBHoDwm_wPqLwOLaA5qMQvcFKqtFNYI_dv5AygCJ8YluTK7k7Xwpu14aaCPD8.; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWP.7RURqVHHo6YOHH5dDkQ5JpX5K-hUgL.Fo2N1h.EShMfeh-2dJLoI7LeIg4r9PS0MNet; SUB=_2A2509cfeDeThGedJ41sT9CnJyzmIHXVUGemWrDV6PUJbkdANLULEkW0tu1bctF0X21n7aVwCdW-3vxqw8w..; SUHB=0WGNCw-giRxknW'
        }
        r = requests.post("https://passport.weibo.cn/sso/login", data=payload, headers=headers)
        # print(r.history)
        # print(r.url)
        print (r.status_code)
        # tree = html.fromstring(r.content)
        if r.json()['retcode'] == 20000000:
            print 'login success!!!'
        else:
            print 'login error!!!'
        self.cookies = r.cookies
        return r.cookies

    def getUrl(self, url):
        r = requests.get(url, cookies=self.cookies)
        return r

    def getUrlNoCookies(self, url):
        r = requests.get(url)
        return r

    def saveUrl(self, url, save):
        print url + " ===> " + save
        with open(save, "wb") as code:
            r = self.getUrl(url)
            if r.status_code != 200:
                print url + '   error !!!'
                return -1
            code.write(r.content)
            return 0

    def saveUrlNoCookies(self, url, save):
        print url + " ===> " + save
        with open(save, "wb") as code:
            r = self.getUrlNoCookies(url)
            if r.status_code != 200:
                print url + '   error !!!'
                return -1
            code.write(r.content)
            return 0

    def getAtUserHtml(self, uid):
        'https://weibo.cn/at/weibo?uid=1772392290&page=1'
        dir = self.allDir + '/' + uid + '/' + timeDir + '/at'
        self.atUserDir = dir
        checkDir(dir)

        for i in range(1, 21):
            saveFile = dir + "/atuser" + str(i) + ".html"
            url = 'https://weibo.cn/at/weibo?uid=' + uid + '&page=' + str(i)
            r = self.saveUrl(url, saveFile)
            if r == -1:
                print url + '   error !!!'
                return

    def getFollowHtml(self, uid):

        dir = self.allDir + '/' + uid + '/' + timeDir + '/follow'
        self.followDir = dir
        checkDir(dir)

        for i in range(1, 21):
            saveFile = dir + "/follow" + str(i) + ".html"
            url = 'https://weibo.cn/' + uid + '/follow?page=' + str(i)
            r = self.saveUrl(url, saveFile)
            if r == -1:
                print url + '   error !!!'
                return

    def getTweetNums(self, saveFile):
        a = open(saveFile).read()
        selector = etree.HTML(a)
        tweete = ";".join(selector.xpath('//div[@class="tip2"]/span[@class="tc"]/text()'))
        tweete_no = int(tweete[tweete.index('[') + 1:-1])

        no = tweete_no / 10
        if tweete_no % 10 != 0:
            no += 1

        print no
        return no

    def getFollowNums(self, saveFile):
        a = open(saveFile).read()
        selector = etree.HTML(a)
        sel_follow = '//div[@class="tip2"]/a[contains(@href,"/follow")]/text()'
        follow = ";".join(selector.xpath(sel_follow))
        print follow

        tweete = ";".join(selector.xpath('//div[@class="tip2"]/span[@class="tc"]/text()'))
        follow_no = int(follow[follow.index('[') + 1:-1])

        no = follow_no / 10
        if follow_no % 10 != 0:
            no += 1
        if no > 20:
            no = 20
        print no
        return no

    def getTweetHtml(self, uid):

        dir = self.allDir + '/' + uid + '/' + timeDir + '/tweet'
        self.tweetDir = dir
        checkDir(dir)

        saveFile = dir + "/tweet1.html"
        url = 'https://weibo.cn/u/' + uid

        r = self.saveUrl(url, saveFile)
        if r == -1:
            print url + '   error !!!'
            return

        no = self.getTweetNums(saveFile)
        #
        for i in range(2, no + 1):
            url = 'https://weibo.cn/u/' + uid + '?page=' + str(i)
            saveFile = dir + "/tweet" + str(i) + ".html"

            r = self.saveUrl(url, saveFile)
            if r == -1:
                print url + '   error !!!'
                return

    def getf(self, uid):
        url = 'http://weibo.com/p/100505' + uid + '/' + timeDir + '/info?mod=pedit_more'
        saveFile = 'html/more.html'
        checkDir('html')
        header1 = {
            'Accept': 'text/html, application/xhtml+xml, */*',
            'Accept-Language': 'zh-CN',
            'User-Agent': 'Mozilla/5.0(compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)',
            'Accept-Encoding': 'gzip,deflate',
            'Host': 'weibo.com',
            'Connection': 'Keep-Alive'
        }
        r = requests.get(url)  # , headers=header1)#, cookies=self.cookies)
        self.saveUrl(url, saveFile)
        # print r.headers
        print ''
        for v, k in r.headers.items():
            print('{v}:{k}'.format(v=v, k=k))
        print r

    def parseHeadHtml(self, opf, filename):
        a = open(filename).read()
        selector = etree.HTML(a)

        tweete = ";".join(selector.xpath('//div[@class="tip2"]/span[@class="tc"]/text()'))
        opf.writeString(tweete + ' ')
        sel_follow = '//div[@class="tip2"]/a[@href="/' + uid + '/follow"]/text()'
        follow = ";".join(selector.xpath(sel_follow))
        opf.writeString(follow + ' ')
        sel_fans = '//div[@class="tip2"]/a[@href="/' + uid + '/fans"]/text()'
        fans = ";".join(selector.xpath(sel_fans))
        print fans
        opf.writeStringLine(fans + ' ')

    def parseTweetHtml(self, opf, filename):
        # sel_tw = '//div[@class="c"]/div/span[@class="ctt"]/text()'
        # tws = selector.xpath(sel_tw)
        # # print len(tw)
        # for each in tws:
        #     print each
        a = open(filename).read()
        selector = etree.HTML(a)
        sel_id = '//div[@class="c"]/@id'
        ids = selector.xpath(sel_id)

        for each in ids:
            opf.writeStringLine('')
            sel_tw = '//div[@class="c"][@id="' + each + '"]/div/span[@class="ctt"]/text()'
            tw = selector.xpath(sel_tw)
            opf.writeStringLine(tw[0])

            sel_tw = '//div[@class="c"][@id="' + each + '"]/div/a[contains(@href,"attitude")]/text()'
            tw = selector.xpath(sel_tw)
            opf.writeString(tw[0])

            sel_tw = '//div[@class="c"][@id="' + each + '"]/div/a[contains(@href,"repost")]/text()'
            tw = selector.xpath(sel_tw)
            opf.writeString(tw[0])

            sel_tw = '//div[@class="c"][@id="' + each + '"]/div/a[contains(@href,"comment")]/text()'
            # print sel_tw

            tw = selector.xpath(sel_tw)
            if len(tw) == 2:
                opf.writeStringLine(tw[1])
            else:
                opf.writeStringLine(tw[0])

            sel_tw = '//div[@class="c"][@id="' + each + '"]/div/span[@class="ct"]/text()'
            tw = selector.xpath(sel_tw)
            opf.writeStringLine(tw[0])
            # print ''
            # break

    def parseHead(self, uid):
        dir = self.allDir + '/' + uid + '/tweet'
        self.tweetDir = dir
        checkDir(dir)

    def parseTweet(self, uid):
        dir = self.allDir + '/' + uid + '/' + timeDir
        opf = OpFile(dir + '/tweet.txt')
        parse_file = dir + '/tweet' + '/tweet1.html'
        self.parseHeadHtml(opf, parse_file)
        # parseTweeteHtml(opf, parse_file)
        no = self.getTweetNums(parse_file)
        for i in range(1, no + 1):
            print str(i) + '.',
            parse_file = dir + '/tweet' + '/tweet' + str(i) + '.html'
            self.parseTweetHtml(opf, parse_file)
            print parse_file

        opf.close()

    def parseFollowHtml(self, opf, name):
        a = open(name).read()
        selector = etree.HTML(a)
        print 'parse ' + name,
        tw = '//tr/td[2]/a[1]/@href'
        follow_href = (selector.xpath(tw))
        # print len(follow_href)

        tw = '//tr/td[2]/a[1]/text()'
        follow_name = (selector.xpath(tw))

        tw = '//tr/td[2]/text()[1]'
        follow_fans = (selector.xpath(tw))

        lens = len(follow_name)
        print lens,
        lens = len(follow_href)
        print lens,
        lens = len(follow_fans)
        print lens
        # for i in range(0, lens):
        #     print(follow_href[i]),
        #     print(' '),
        #     print(follow_name[i]),
        #     print(' '),
        #     print(follow_fans[i])
        for i in range(0, lens):
            opf.writeString(follow_href[i])
            opf.writeString(' ')
            opf.writeString(follow_name[i])
            opf.writeString(' ')
            opf.writeStringLine(follow_fans[i])
        print ''

    def getAtUserNum(self, name):
        a = open(name).read()
        selector = etree.HTML(a)

        tw = '//div[@id="pagelist"]/form/div/input[@name="mp"]/@value'
        follow_href = (selector.xpath(tw))
        # print follow_href
        return int(follow_href[0])

    def parseAtUserHtml(self, opf, name):
        a = open(name).read()
        selector = etree.HTML(a)

        tw = '//div[@id="pagelist"]/form/div/input[@name="mp"]/@value'
        follow_href = (selector.xpath(tw))
        print 'TODO:'

    def parseFollow(self, uid):
        dir = self.allDir + '/' + uid + '/' + timeDir
        opf = OpFile(dir + '/follow.txt')
        parse_file = dir + '/tweet' + '/tweet1.html'
        print parse_file
        no = self.getFollowNums(parse_file)
        for i in range(1, no + 1):
            parse_file = dir + '/follow/follow' + str(i) + '.html'

            self.parseFollowHtml(opf, parse_file)
        opf.close()

    def parseAtUser(self, uid):
        dir = self.allDir + '/' + uid + '/' + timeDir
        opf = OpFile(dir + '/at.txt')
        parse_file = dir + '/at/atuser1.html'
        no = self.getAtUserNum(parse_file)
        for i in range(2, no + 1):
            parse_file = dir + '/at/atuser' + str(i) + '.html'
            self.parseAtUserHtml(opf, parse_file)


########################################################
if __name__ == "__main__":
    uid = '1772392290'
    # uid = '3373931552'

    a = Weibo()
    a.login()
    a.getf(uid)
    #
    a.getTweetHtml(uid)
    a.getFollowHtml(uid)
    a.getAtUserHtml(uid)
    a.parseTweet(uid)
    a.parseFollow(uid)
    a.parseAtUser(uid)
    # yougu.getyougu()
    print 'end'
