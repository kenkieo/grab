# coding=utf-8

import requests
from lxml import etree

import sys
import os
import json
import datetime
import yougu
import time

Debuggable = False
now = datetime.datetime.now()

timeDir = now.strftime('%Y-%m-%d %H\'%M\'%S')
if Debuggable == True:
    timeDir = 'test'
reload(sys)
sys.setdefaultencoding('utf-8')


def zsleep(mytime=''):
    time.sleep(mytime)


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
    username = "75023143@qq.com"  # raw_input('input sina weibo id: ')
    password = "331730216"  # raw_input('input secret: ')
    cookies = ''
    allDir = 'html'
    tweetDir = allDir
    followDir = allDir
    atUserDir = allDir

    def __init__(self):
        print 'weibo init'

    def login(self):
        s = requests.Session()

        # r = s.get('https://passport.weibo.cn/signin/login')

        # r = s.get('https://passport.weibo.cn/sso/login')
        # r = s.get("http://httpbin.org/cookies")
        # print (r.status_code)
        # with open("code.html", "wb") as code:
        # code.write(r.content)
        payload = {
            'username': self.username,
            'password': self.password,
            'savestate': '1',
            'mainpageflag': '1',
            'ec': '0',
            'pagerefer': '',
            'entry': 'mweibo',
            'r': '',
            'wentry': '',
            'loginfrom': '',
            'client_id': '',
            'code': '',
            'qq': '',
            'hff': '',
            'hfp': '',
        }
        headers = {
            'Accept': '*/*',
            'User-Agent': 'Mozilla/5.0 (compatible;MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)',
            'Referer': 'https://passport.weibo.cn/signin/login',
            'Accept - Encoding': 'gzip,deflate',
            'Accept-Language': 'zh-cn',
            'Connection': 'keep-alive',
            'Content-Length': str(len(payload)),
            'Content-Type': 'application/x-www-form-urlencoded',
            'Host': 'passport.weibo.cn',
            'Cache-Control': 'no-cache',
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
        print r.cookies
        return r.cookies

    def getUrl(self, url):
        r = requests.get(url, cookies=self.cookies)
        return r

    def getPage(self):
        headers = {
            'Accept': 'text/html, application/xhtml+xml, image/jxr, */*',
            'Accept-Encoding': 'gzip, deflate, br',
            'Accept-Language': 'zh-CN',
            'Connection': 'Keep-Alive',
            'Host': 'weibo.com',
            'User-Agent': 'Mozilla / 5.0(Windows NT 10.0; Win64; x64) AppleWebKit / 537.36(KHTML, like Gecko) Chrome / 58.0.3029.110 Safari / 537.36 Edge / 16.16299'
        }

        payload = {'from': 'page_100505_profile', 'mod': 'like', 'wvr': '6'}
        url = 'https://weibo.com/1772392290/like'
        r = requests.get(url, headers=headers, params=payload, allow_redirects=False,cookies=self.cookies)
        print r
        print r.headers.get('Set-Cookie', '')

        location = r.headers.get('Location', '')

        print 'Location: ', location
        headers = {
            'Accept': 'text/html, application/xhtml+xml, image/jxr, */*',
            'Accept-Encoding': 'gzip, deflate, br',
            'Accept-Language': 'zh-CN',
            'Connection': 'Keep-Alive',
            'Host': 'passport.weibo.com',
            'User-Agent': 'Mozilla / 5.0(Windows NT 10.0; Win64; x64) AppleWebKit / 537.36(KHTML, like Gecko) Chrome / 58.0.3029.110 Safari / 537.36 Edge / 16.16299'
        }

        r = requests.get(location, headers=headers, allow_redirects=False)
        print r
        return r


    def getUrlNoCookies(self, url):
        payload = {'Accept': 'text/html,application/xhtml+xml,*/*',
                   'Accept-Encoding': 'gzip, deflate',
                   'Accept-Language': 'zh-CN',
                   'Connection': 'Keep-Alive', 'Host': 'weibo.com',
                   'Referer': 'https://passport.weibo.com/visitor/visitor?entry=miniblog&a=enter&url=https%3A%2F%2Fweibo.com%2F1772392290%2Flike%3Ffrom%3Dpage_100505_profile%26wvr%3D6%26mod%3Dlike&domain=.weibo.com&ua=php-sso_sdk_client-0.6.23&_rand=1517455713.6972',
                   'User-Agent': 'Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)'
                   }

        r = requests.get(url, params=payload, cookies=self.cookies)
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


    def saveUrlNoCookies(self, url, save):
        print url + " ===> " + save
        with open(save, "wb") as code:
            r = self.getUrlNoCookies(url)
            if r.status_code != 200:
                print url + '   error !!!'
                return -1
            code.write(r.content)
            return 0


    def getUidLikeHtml(self, uid):
        'https://weibo.com/1772392290/like?from=page_100505_profile&wvr=6&mod=like'
        dir = self.allDir + '/' + uid + '/' + timeDir + '/like'
        self.atUserDir = dir
        checkDir(dir)

        for i in range(1, 2):
            saveFile = dir + "/like" + str(i) + ".html"
            url = 'https://weibo.com/' + uid + '/like?from=page_100505_profile&wvr=6&mod=like'
            print url
            r = self.saveUrlNoCookies(url, saveFile)
            # r = -1
            if r == -1:
                print url + '   error !!!'
                return


    def getUidAtUserHtml(self, uid):
        'https://weibo.cn/at/weibo?uid=xxxxxxxxx&page=1'
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


    def getUidFollowHtml(self, uid):
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


    def getUidTweetHtml(self, uid):
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
            zsleep(5)
            r = self.saveUrl(url, saveFile)
            if r == -1:
                print url + '   error !!!'
                return


    def test(self):
        # url = 'https://weibo.com/p/1005051772392290/info?mod=pedit_more'
        url = 'https://weibo.com/p/1005051772392290/info?mod=pedit_more'
        # url = urllib.quote(url)

        header1 = {
            'Accept': 'text/html, application/xhtml+xml, */*',
            'Accept-Language': 'zh-CN',
            'User-Agent': 'Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)',
            'Accept-Encoding': 'gzip, deflate',
            'Host': 'weibo.com',
            'Connection': 'Keep-Alive',
        }
        r = requests.get(url, headers=header1)
        print r


    def test7e(self):
        url = 'https://passport.weibo.com/visitor/genvisitor'

        header1 = {
            'Accept': '*/*',
            'Accept-Language': 'zh-CN',
            'Referer': 'https://passport.weibo.com/visitor/visitor?entry=miniblog&'
                       'a=enter&'
                       'url=https%3A%2F%2Fweibo.com%2Fp%2F1005051772392290%2Finfo%3Fmod%3Dpedit_more&'
                       'domain=.weibo.com&'
                       'ua=php-sso_sdk_client-0.6.23&'
                       '_rand=1511417677.5506',
            'User-Agent': 'Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)',
            'Accept-Encoding': 'gzip, deflate',
            'Host': 'passport.weibo.com',
            'Content-Type': 'application/x-www-form-urlencoded',
            'Connection': 'Keep-Alive',
            'If-Modified-Since': '0',
            'Cache-Control': 'no-cache',
            'Content-Length': '5657',
        }
        cookies = {
            'TC-Page-G0': '9183dd4bc08eff0c7e422b0d2f4eeaec'
        }
        r = requests.get(url, headers=header1, cookies=cookies)
        print r.content


    def test8(self):
        url = 'https://passport.weibo.com/visitor/visitor?a=incarnate&' \
              't=G3gNXtaosiTZKLmzmi8KLdYwKSVzRVEYGfXdE9YXqlQ%3D&' \
              'w=2&' \
              'c=095&' \
              'gc=&' \
              'cb=cross_domain&' \
              'from=weibo&' \
              '_rand=0.9728172218271613'

        header1 = {
            'Accept': 'application/javascript, */*;q=0.8',
            'Accept-Language': 'zh-CN',
            'User-Agent': 'Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)',
            'Accept-Encoding': 'gzip, deflate',
            'Host': 'passport.weibo.com',
            # 'Upgrade-Insecure-Requests': '1',
            'Connection': 'Keep-Alive'
        }
        cookies = {
            'tid': 'G3gNXtaosiTZKLmzmi8KLdYwKSVzRVEYGfXdE9YXqlQ=__095',
            'TC-Page-G0': '9183dd4bc08eff0c7e422b0d2f4eeaec'
        }
        r = requests.get(url, headers=header1, cookies=cookies)
        subp = r.cookies.get_dict(domain='.weibo.com', path='/')['SUBP']
        sub = r.cookies.get_dict(domain='.weibo.com', path='/')['SUB']
        print subp
        print sub


    def test9(self):
        sub = '_2AkMtSuurf8NxqwJRmPEWzGLnbY1wwwzEieKbFhpwJRMxHRl-yT9jqlEYtRB6XNyh2BGCWNY08ICCAHshfTwl1d1fsWoa&'
        subp = '0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5U7lA6__aRkAmiaClWdh7R'
        url = 'https://login.sina.com.cn/visitor/visitor?a=crossdomain&' \
              'cb=return_back&' \
              's=' + sub + '&' \
                           'sp=' + subp + '&' \
                                          'from=weibo&' \
                                          '_rand=0.9969922026682345'
        header1 = {
            'Accept': 'application/javascript, */*;q=0.8',
            'Accept-Language': 'zh-CN',
            'User-Agent': 'Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)',
            'Accept-Encoding': 'gzip, deflate',
            'Host': 'login.sina.com.cn',
            # 'Upgrade-Insecure-Requests': '1',
            'Connection': 'Keep-Alive'
        }
        saveFile = 'html/more.html'
        r = requests.get(url, headers=header1)
        subp = r.cookies.get_dict(domain='.sina.com.cn', path='/')['SUBP']
        sub = r.cookies.get_dict(domain='.sina.com.cn', path='/')['SUB']
        # ret = self.saveUrl(url, saveFile)
        # if ret == -1:
        #     return

        # def getUserInfo(self, uid):
        url = 'http://weibo.com/p/100505' + uid + '/info?mod=pedit_more'
        saveFile = 'html/more.html'
        checkDir('html')
        header1 = {
            'Accept': 'text/html, application/xhtml+xml, */*',
            'Accept-Language': 'zh-CN',
            'User-Agent': 'Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)',
            'Accept-Encoding': 'gzip, deflate',
            # 'Host':'weibo.com',
            # 'Upgrade-Insecure-Requests': '1',
            # 'Connection': 'Keep-Alive'
        }
        self.cookies = {'TC-Page-G0': 'cdcf495cbaea129529aa606e7629fea7',
                        'SUB': sub,
                        'SUBP': subp}
        # 'SUB': '_2AkMtSdP5f8NxqwJRmPEWzGLnbY1wwwzEieKbFSIiJRMxHRl-yT9kqkNdtRB6XNyh2FdIWjmqWTf58pvCfy6A-XMdRAwJ',
        # 'SUBP': '0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5U7lA6__aRkAmiaClWdh7R'}
        r = requests.get(url, headers=header1, cookies=self.cookies)
        # self.cookies = {'TC-Page-G0':'cdcf495cbaea129529aa606e7629fea7','SUB':'_2AkMtSdP5f8NxqwJRmPEWzGLnbY1wwwzEieKbFSIiJRMxHRl-yT9kqkNdtRB6XNyh2FdIWjmqWTf58pvCfy6A-XMdRAwJ', 'SUBP':'0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5U7lA6__aRkAmiaClWdh7R'}
        # r = requests.get(url, headers=header1)
        ret = self.saveUrl(url, saveFile)
        if ret == -1:
            return
        # print r.headers
        # print ''
        # for v, k in r.headers.items():
        #     print('{v}:{k}'.format(v=v, k=k))
        # print r

        a = open(saveFile).read()
        selector = etree.HTML(a)
        tweete = "^".join(selector.xpath('//script/text()'))
        # print tweete
        t = tweete.split("^")
        # t = tweete
        for tw in t:
            if (tw.find("FM.view(") != -1):
                tw = tw[tw.find("{"):tw.find("}") + 1]
                # print (tw)
                # jw = json.loads((tw))
                # print jw
                if (tw.find('"domid":"Pl_Official_RightGrowNew__57"') != -1):
                    # print '----'
                    # print tw
                    jw = json.loads((tw))
                    selector = etree.HTML(jw['html'])
                    # s = selector.xpath('//a[@class="W_icon_level"]/text()')
                    s = selector.xpath(
                        '//div[@class="level_box S_txt2"]/p[@class="level_info"]/span[@class="info"]/span[@class="S_txt1"]/text()')
                    print s[1]

                if (tw.find('"domid":"Pl_Core_T8CustomTriColumn__55"') != -1):
                    # print '++++'
                    # print tw
                    jw = json.loads((tw))
                    selector = etree.HTML(jw['html'])
                    # print jw['html']
                    s = selector.xpath('//strong[@class="W_f18"]/text()')
                    # print s
                    v = selector.xpath('//span[@class="S_txt2"]/text()')
                    # print v

                    print v[0], ":", s[0], v[1], ":", s[1], v[2], ":", s[2]


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

            sel_tw = '//div[@class="c"][@id="' + each + '"]/div/span[contains(@class,"cmt")]/text()'
            tw = selector.xpath(sel_tw)

            if tw:
                sel_tw = '//div[@class="c"][@id="' + each + '"]/div[2]/a/text()'
                tw = selector.xpath(sel_tw)
                opf.writeString('转发理由:')
                for i in range(0, len(tw) - 4):
                    # print tw[i].decode("utf-8")
                    opf.writeString(tw[i].decode("utf-8"))
                opf.writeStringLine('')
            else:
                # print 'no word'

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
            tw = selector.xpath(sel_tw)
            if len(tw) == 2:
                opf.writeStringLine(tw[1])
            else:
                opf.writeStringLine(tw[0])

            sel_tw = '//div[@class="c"][@id="' + each + '"]/div/span[@class="ct"]/text()'
            tw = selector.xpath(sel_tw)
            opf.writeString(tw[0])

            sel_tw = '//div[@class="c"][@id="' + each + '"]/div/span[@class="ct"]/a/text()'

            tw = selector.xpath(sel_tw)
            if tw:
                # print str(len(tw)) + "++++++++++"
                opf.writeStringLine(tw[0])
            else:
                opf.writeStringLine("")
            # print ''
            # break


    def parseHead(self, uid):
        dir = self.allDir + '/' + uid + '/tweet'
        self.tweetDir = dir
        checkDir(dir)


    def parseDirTweet(self, mdir):
        dir = mdir
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


    def parseUidTweet(self, uid):
        dir = self.allDir + '/' + uid + '/' + timeDir
        self.parseDirTweet(dir)


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
        print name
        selector = etree.HTML(a)
        tw = '//div[@id="pagelist"]/form/div/input[@name="mp"]/@value'
        follow_href = (selector.xpath(tw))
        print int(follow_href[0])
        return int(follow_href[0])


    def parseAtUserHtml(self, opf, name):
        a = open(name).read()
        print name
        selector = etree.HTML(a)
        tw = '//span[@class="cmt"]/@value'
        # tw = '//div[@class="c"]/div[2]/text()[1]'
        # follow_href = (selector.xpath(tw))
        # l = len(follow_href)
        # for i in range(0, l):
        #     print follow_href[i].decode("utf-8")

        sel_id = '//div[@class="c"]/@id'
        ids = selector.xpath(sel_id)
        # print len(ids)
        for each in ids:
            # print each
            sel_tw = '//div[@class="c"][@id="' + each + '"]/div[1]/a[@class="nk"]/text()'
            '//*[@id="M_Fi1vrpynz"]/div[1]/a[1]'

            ww = selector.xpath(sel_tw)

            sel_tw = '//div[@class="c"][@id="' + each + '"]/div[last()]/text()[1]'

            tw = selector.xpath(sel_tw)
            l = len(tw)
            sel_tw = '//div[@class="c"][@id="' + each + '"]/div[last()]/a/text()'

            lw = selector.xpath(sel_tw)

            sel_tw = '//div[@class="c"][@id="' + each + '"]/div[last()]/span[@class="ct"]/text()'

            rw = selector.xpath(sel_tw)
            opf.writeString(ww[0].decode("utf-8") + " : ")
            for i in range(0, l):
                opf.writeStringLine(tw[i].decode("utf-8"))
                ll = len(lw)
                for j in range(0, ll):
                    opf.writeString(lw[j].decode("utf-8"))
                    # print rw.decode("utf-8")
                    # for k in range(0, lll):
                    #     print lw[j][k].decode("utf-8")
            opf.writeStringLine('')
            opf.writeStringLine(rw[0].decode("utf-8"))
            opf.writeStringLine('')

        # print 'TODO:'


    def parseDirFollow(self, dir):
        opf = OpFile(dir + '/follow.txt')
        parse_file = dir + '/tweet' + '/tweet1.html'
        print parse_file
        no = self.getFollowNums(parse_file)
        for i in range(1, no + 1):
            parse_file = dir + '/follow/follow' + str(i) + '.html'
            self.parseFollowHtml(opf, parse_file)
        opf.close()


    def parseUidFollow(self, uid):
        dir = self.allDir + '/' + uid + '/' + timeDir
        self.parseDirFollow(dir)


    def parseDirAtUser(self, dir):
        opf = OpFile(dir + '/at.txt')
        parse_file = dir + '/at/atuser1.html'
        no = self.getAtUserNum(parse_file)
        for i in range(1, no + 1):
            parse_file = dir + '/at/atuser' + str(i) + '.html'
            self.parseAtUserHtml(opf, parse_file)


    def parseUidAtUser(self, uid):
        dir = self.allDir + '/' + uid + '/' + timeDir
        self.parseDirAtUser(dir)


########################################################


if __name__ == "__main__":
    uid = '1772392290'
    # uid = '3373931552'

    a = Weibo()
    # a.login()
    if Debuggable == False:
        a.login()
        a.getUidTweetHtml(uid)
        a.getUidFollowHtml(uid)
        a.getUidAtUserHtml(uid)
    # a.getPage()
    # a.getUidLikeHtml(uid)
    a.parseUidTweet(uid)
    a.parseUidFollow(uid)
    a.parseUidAtUser(uid)

if __name__ == "__main1__":
    yougu.getfollow()
    print 'end'
