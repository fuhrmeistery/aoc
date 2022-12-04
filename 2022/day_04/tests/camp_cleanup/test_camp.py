import pytest

from camp_cleanup.camp import split_line, split_elf, is_contained, count_full_contain, count_overlaps, is_overlap

sections = [
    ((2, 4), (6, 8), False),
    ((2, 8), (3, 7), True),
]

overlaps = [
    ((2, 4), (6, 8), False),
    ((2, 3), (4, 5), False),
    ((5, 7), (7, 9), True),
    ((2, 8), (3, 7), True),
    ((6, 6), (4, 6), True),
    ((2, 6), (4, 8), True),
]


def test_split_line():
    line = "2-4,6-8"
    left, right = split_line(line)
    assert left == "2-4"
    assert right == "6-8"


def test_split_elf():
    elf = "2-4"
    lower, upper = split_elf(elf)
    assert lower == 2
    assert upper == 4


@pytest.mark.parametrize("elf1, elf2, expected", sections)
def test_is_contained_in_two(elf1, elf2, expected):
    result = is_contained(elf1, elf2)
    assert result == expected


@pytest.mark.parametrize("elf2, elf1, expected", sections)
def test_is_contained_in_one(elf1, elf2, expected):
    result = is_contained(elf1, elf2)
    assert result == expected


def test_count_full_contains():
    with open("input_test.txt", encoding='utf-8') as file:
        result = count_full_contain(file)
        assert result == 2


@pytest.mark.parametrize("one, two, exp", overlaps)
def test_is_overlap(one, two, exp):
    result = is_overlap(one, two)
    assert result == exp


def test_count_overlap():
    with open("input_test.txt", encoding='utf-8') as file:
        result = count_overlaps(file)
        assert result == 4
