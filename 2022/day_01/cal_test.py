import unittest
from cal import *

def test_should_count_cal():
    items = [1000, 2000, 3000]
    result = count_cal(items)
    assert result == 6000


def test_should_find_max():
    cals = [1000, 399, 3399]
    result = find_max_cal(cals)
    assert result == 3399

def test_should_read_elfs():
    elfs = read_file("test_input.txt")
    assert len(elfs) == 5
