#!/usr/bin/python3

import json
import requests
from collections import namedtuple 

rect_entry = namedtuple('rect_entry', 'width color')

# fetch the data from the website here and load it into a json string
ip = "http://198.100.154.139"
j = requests.get(ip).json()


# define how long the bars should be 
bar_width = 100
garage_rects = {}

# conver the generic occupancy information to an rgb code or /we
def convert_color(s : str):
    if s == "low":
        return "blue"
    elif s == "med":
        return "green"
    elif s == "high":
        return "orange"
    elif s == "max":
        return "red"
    else:
        print("unhandled status:", s)
        exit(0)
    

# iterate the json object's garages
for g in j:
    occupied = 0
    rects = []

    # iterate each element in the garages
    # the iteration limit should be the amount of elements -1
    for i in range(len(j[g]) - 1):
        cur_entry = j[g][i]
        next_entry = j[g][i + 1]

        # entry[0] = epoch time of when the occupancy info changes
        # entry[1] = occpurancy information for this block
        cur_width = (next_entry[0] - cur_entry[0]) / bar_width
        color = convert_color(cur_entry[1])
        
        # keep track of how many pixels are currently mapped so we
        # can determine the width of the last block 
        occupied += cur_width
        rects.append(rect_entry(cur_width, color))

    # get the width and color of the last block
    last_width = bar_width - occupied
    # j[g][-1] just means we're getting the last entry of the list 
    last_color = convert_color(j[g][-1][1])

    rects.append(rect_entry(last_width, last_color))

    garage_rects[g] = rects

for e in garage_rects:
    print(e)

    for d in garage_rects[e]:
        print("\tWidth: %d, Color: %s" %(d.width, d.color))
