#!/usr/bin/python3

import os 
import json
from datetime import datetime
from time import strptime, mktime 
from collections import namedtuple

occ_pt      = namedtuple('occ_pt', 'pt t')


def main():
    garages     = {}

    collect(garages)
    merge_pts(garages)
    print(output_json(garages))


def num_pts(garages):
    c = 0

    for g in garages.values():
        c += len(g.pts)

    return c


def print_pts(garages):
    for g in garages:
        print('iterating %s' % g)
        for pt in garages[g]:
            print('pt: %d\ttype:%s' % (pt.pt, pt.t))
        return

def collect(garages):
    for root, dirs, files in os.walk('./sample'):
        for fname in files:
            f = open(os.path.join(root, fname))
            d = f.read().replace("'", '"')
            j = json.loads(d)
            
            for item in j['occupancy2']:
                if item['factype'] != 'W':
                    continue

                cur_occ = item['occupied']
                cap     = item['capacity']
                name    = item['name']

                def ins_pt(t: str):
                    epoch_time = int(mktime(strptime(item['datestamp'], '%Y-%m-%dT%H:%M:%S')))
                    pt = occ_pt(pt=epoch_time, t=t)
                    garages[name].append(pt)

                if name not in garages:
                    garages[name] = []
                else:
                    if cur_occ >= cap * 0.99:
                        ins_pt('max')
                    elif cur_occ >= cap * 0.95:
                        ins_pt('high')
                    elif cur_occ >= cap * 0.80:
                        ins_pt('med')
                    else:
                        ins_pt('low')
    return


def merge_pts(garages):
    for g in garages.values():
        i = 0
        itr_max = len(g) - 1

        while i < itr_max:
            cur = g[i]
            nxt = g[i + 1]
            
            if cur.t == nxt.t:
                g.pop(i + 1)
                itr_max -= 1
            else:
                i += 1
 
    return

def output_json(garages):
    j = {}
    for name in garages:
        j[name] = garages[name]

    return json.dumps(j, indent=4, sort_keys=True)

if __name__ == '__main__':
    main()
