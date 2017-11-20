import requests


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
    r = requests.get(url3)#, headers=header2)
    print r.content
    result = r.json()['result']
    # print result
    length = len(result)
    for i in range(1, length):
        print result[i]#['shareText']
        # print result[i]['content']
