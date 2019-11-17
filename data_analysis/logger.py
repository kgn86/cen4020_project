#!/usr/bin/python3

import requests
import os
from datetime import datetime
from time import strftime
from time import sleep

def Main():
    if not os.path.exists('log'):
        os.mkdir('log')

    while(True):
        time = datetime.now().strftime('%Y-%m-%d %H-%M')
        file_name = os.path.join('log', time + '.json')

        file = open(file_name, 'w')
        file.write(str(requests.get('https://obs-web05.bfs.fsu.edu/count2.html').json()))
        file.close()

        sleep(300)
    return


if __name__ == '__main__':
    Main()
