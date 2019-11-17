#!python3.7

import os 
import json
from datetime import datetime
from time import strftime
from collections import namedtuple

occ_pt      = namedtuple('occ_pt', 'point span')
garage_data = namedtuple('garage_data', 'last points')


def main():
    garages     = {}

    collect(garages)
    merge_pts(garages)

    for name in garages:
        print(garages[name].points)

def collect(garages):
    for root, dirs, files in os.walk('./sample'):
        for fname in files:
            f = open(os.path.join(root, fname))
            d = f.read().replace("'", '"')
            j = json.loads(d)
            
            for item in j['occupancy2']:
                cur_occ = item['occupied']
                name    = item['name']
                    
                if name not in garages:
                    garages[name] = garage_data(last=cur_occ, points=[])
                else:
                    # ts  = item['datestamp']
                    cap = item['capacity']

                    if (garages[name].last == cap and cur_occ != cap) or cur_occ <= cap * 0.85:
                        pt = occ_pt(point=item['datestamp'], span=5)
                        garages[name].points.append(pt)
                        
                    garages[name]._replace(last=cur_occ)
    return


def merge_pts(garages):
    for g in garages:
        continue

    return

if __name__ == '__main__':
    main()
