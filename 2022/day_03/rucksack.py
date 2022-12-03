from const import LOWER_CASE_OFFSET, UPPER_CASE_OFFSET, LOWER_CASE
from typing import List, Tuple


def map_lower_case(c: chr) -> int:
    return ord(c) - LOWER_CASE_OFFSET


def map_upper_case(c: chr) -> int:
    return ord(c) - UPPER_CASE_OFFSET + LOWER_CASE


def map_to_number(item: chr):
    if item.islower():
        return map_lower_case(item)
    else:
        return map_upper_case(item)


def split_compartment(backpack: List) -> Tuple[List[int], List[int]]:
    half = int((len(backpack)) / 2)
    return backpack[:half], backpack[half:]


def find_duplicates(left: List, right: List) -> int:
    left.sort()
    right.sort()
    for item in left:
        if item in right:
            return item


def get_duplicate(items: str) -> chr:
    items = map(map_to_number, items)
    left, right = split_compartment(list(items))
    return find_duplicates(left, right)


def find_badge(s: List[str]) -> int:
    one = list(map(map_to_number, s[0]))
    two = list(map(map_to_number, s[1]))
    three = list(map(map_to_number, s[2]))
    one.sort()
    two.sort()
    three.sort()
    print(one, two, three)

    for item in one:
        if item in two and item in three:
            return item


if __name__ == "__main__":
    with open("input.txt", encoding='utf-8') as file:
        duplicates = list()

        for line in file:
            duplicate = get_duplicate(line)
            duplicates.append(duplicate)
    print(sum(duplicates))

    with open("input.txt", encoding='utf-8') as file:
        rucksacks = list()
        squads = list()
        start = 0
        stop = 3

        for line in file:
            rucksacks.append(line.strip())

        squad_count = int(len(rucksacks))

        while stop <= squad_count:
            squad = rucksacks[start:stop]
            squads.append(squad)
            start += 3
            stop += 3

        badges = list()
        print(squads)
        for squad in squads:
            badge = find_badge(squad)
            badges.append(badge)
    print(sum(badges))
