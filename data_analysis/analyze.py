#!/usr/bin/python3

import os 
import json
from datetime import datetime
from time import strptime, mktime 
from collections import namedtuple

occ_pt      = namedtuple('occ_pt', 'pt span')
garage_data = namedtuple('garage_data', 'last pts')


def main():
    garages     = {}

    collect(garages)
    merge_pts(garages)


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
                    garages[name] = garage_data(last=cur_occ, pts=[])
                else:
                    cap = item['capacity']
                    
                    if (garages[name].last == cap and cur_occ != cap) or (cur_occ <= cap * 0.85):
                        ts = item['datestamp']
                        epoch_ts = int(mktime(strptime(ts, '%Y-%m-%dT%H:%M:%S')))

                        pt = occ_pt(pt=epoch_ts, span=5*60)
                        garages[name].pts.append(pt)
                        
                garages[name]._replace(last=cur_occ)
    return


def merge_pts(g):
    for g in garages.values():
        while i < itr_max:
            cur_pt = g.pts[i]
            nxt_pt = g.pts[i + 1]
            
            overlap_pt = cur_pt.pt + cur_pt.span
            if overlap_pt >= nxt_pt.pt:
                new_span = cur_pt.span + nxt_pt.span
                g.pts[i]._replace(pt=overlap_pt, span=new_span)
                g.pts.pop(i)

                itr_max -= 1
            else:
                i += 1
            


    return

if __name__ == '__main__':
    main()
