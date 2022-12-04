"""
Module to clean up the campsite
"""
from typing import Tuple, Iterable


def split_line(line: str):
    sections = line.strip().split(",")
    return sections[0], sections[1]


def split_elf(elf: str) -> Tuple[int, int]:
    bounds = elf.split("-")
    return int(bounds[0]), int(bounds[1])


def is_contained(elf1: Tuple[int, int], elf2: Tuple[int, int]) -> bool:
    if elf1[0] >= elf2[0] and elf1[1] <= elf2[1]:
        return True
    if elf2[0] >= elf1[0] and elf2[1] <= elf1[1]:
        return True
    return False


def is_overlap(one: Tuple[int, int], two: Tuple[int, int]) -> bool:
    if is_contained(one, two):
        return True
    if one[0] in range(two[0], two[1]):
        return True
    if one[1] in range(two[0], two[1]):
        return True
    if two[0] in range(one[0], one[1]):
        return True
    if two[1] in range(one[0], one[1]):
        return True
    return False


def count_full_contain(couples: Iterable) -> int:
    pairs = map(split_line, couples)
    sections = map(lambda x: (split_elf(x[0]), split_elf(x[1])), pairs)
    contained = filter(lambda x: is_contained(x[0], x[1]), sections)
    return len(list(contained))


def count_overlaps(couples: Iterable) -> int:
    pairs = map(split_line, couples)
    sections = map(lambda x: (split_elf(x[0]), split_elf(x[1])), pairs)
    overlaps = filter(lambda x: is_overlap(x[0], x[1]), sections)
    return len(list(overlaps))
