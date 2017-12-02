# coding=utf-8

import requests


def getfollow():
    headers = {
        'Accept-Encoding': 'gzip,deflate',
        # 'sessionid': '201707141012131628502',
        # 'userid': '1628502',
        # 'ak': '0630010130001',
        # 'imei': '861414037186452',
        # 'ts': '15118348650461628503',
        'Host': 'user.youguu.com',
        'Connection': 'Keep-Alive',
        'User-Agent': 'Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1'

    }
    url = 'http://user.youguu.com/jhss/member/newQueryFollows?uid=~3LSXwNCTuLP&fromId=~5J9&reqNum=~6Gc6'  # &youguu_random=f9b0f22d7d4f1b5a8ab92f289b31fb9e'
    # url = 'http://user.youguu.com/jhss/member/newQueryFollows?uid=~0HOTsJ_PqHL&fromId=~5J9&reqNum=~5Jf9'  # &youguu_random=107815312de1656abc75c9d14c4b5c6b'
    r = requests.get(url, headers=headers)
    print r.content
    a = '{"status":"0000","message":"查询成功","result":{"followList":[{"profitRate":"2640.02%","uid":1284698},{"profitRate":"1244.71%","uid":446348},{"profitRate":"295.17%","uid":117920}],"userList":[{"stockFirmFlag":0,"sex":0,"nickName":"优顾01158264","userId":1284698,"certifySignature":"交易牛人","userName":"优顾01158264","vipType":-1,"rating":"C","headPic":"http://www.youguu.com/mncg/mncgpic/8.jpg","signature":"格物穷理，知行合一。","vType":"1"},{"stockFirmFlag":0,"sex":-1,"nickName":"橙子007","userId":446348,"certifySignature":"交易牛人","userName":"橙子007","vipType":-1,"rating":"B","headPic":"http://img.youguu.com/sync/mncg/headpic/images/20140514/20140514163432.jpg","signature":"独挡千古错，冷漠自逍遥。","vType":"1"},{"stockFirmFlag":0,"sex":-1,"nickName":"李俐锋","userId":117920,"certifySignature":"交易牛人","userName":"李俐锋","vipType":-1,"rating":"","headPic":"http://img.youguu.com/sync/mncg/headpic/images/20140305/20140305193809.jpg","signature":"一切皆有可能……………","vType":"1"}]}}'
    print a
    # result = r.json()#['result']
    # print result
    # length = len(result)
    # for i in range(1, length):
    #     print result[i]


def getyougu():
    url = 'http://mncg.youguu.com/youguu/simtrade/showuseracountinfo/~5JAVvJA9tJABvJA9sJ9/~9FMRqH8NoFJ/~4LBOzKwO0/~1MQ?youguu_random=3c5eba30220f521f1353c61a750acaf'
    header1 = {
        'Accept - Encoding': 'gzip, deflate',
        'sessionid': '201707141012131628501',
        'userid': '1628501',
        'ak': '0630010130000',
        'imei': '861414037186450',
        'ts': '15105403685161628501',
        'Host': 'mncg.youguu.com',
        'Connection': 'Keep-Alive',
        'User - Agent': 'Mozilla/5.0(Linux; U; Android 2.2.1; en-us; Nexus One Build.FRG83) AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1',
    }
    url2 = 'http://mncg.youguu.com/youguu/rating/score?userid=~9G8JuFrJw&matchid=~3LP&youguu_random=1bac81c89c3b148cd455cbfc0d0159c5'
    header2 = {
        'Accept-Encoding': 'gzip, deflate',
        'sessionid': '201707141012131628501',
        'userid': '1628501',
        'ak': '0630010130000',
        'imei': '861414037186450',
        'ts': '15105403685161628501',
        'Host': 'mncg.youguu.com',
        'Connection': 'Keep - Alive',
        'User - Agent': 'Mozilla/5.0(Linux;U;Android 2.2.1; en-us; Nexus One Build.FRG83) AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1'

    }
    url3 = 'http://mncg.youguu.com/youguu/trade/conclude/query?matchid=~9FJ&fromtid=~2D3&reqnum=~3Lh-&uid=~5KANyJvN7&version=1&youguu_random=765913f86c2276868dd334f15b793961'
    r = requests.get(url3)  # , headers=header2)
    print r.content
    result = r.json()['result']
    # print result
    length = len(result)
    for i in range(1, length):
        print result[i]  # ['shareText']
        # print result[i]['content']
