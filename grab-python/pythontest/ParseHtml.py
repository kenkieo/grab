import HTMLParser


class MyParser(HTMLParser.HTMLParser):
    def __init__(self):
        HTMLParser.HTMLParser.__init__(self)

    def handle_starttag(self, tag, attrs):
        if tag == 'span':
            for name, value in attrs:
                if name == 'class':
                    if value == 'u':
                        value = value

    def handle_data(self, data):
        if (data.isspace() == False):
            print data


if __name__ == "__main__":
    a = open('code.html').read()
    my = MyParser()
    my.feed(a)
