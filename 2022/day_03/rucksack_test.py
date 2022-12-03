import pytest
from rucksack import map_lower_case, map_upper_case, split_compartment

lowercase = [('a', 1), ('z', 26)]
uppercase = [('A', 27), ('Z', 52)]


@pytest.mark.parametrize("c, ordinal", lowercase)
def test_lower_case_map(c, ordinal):
    result = map_lower_case(c)
    assert result == ordinal


@pytest.mark.parametrize("c, ordinal", uppercase)
def test_upper_case_map(c, ordinal):
    result = map_upper_case(c)
    assert result == ordinal


def test_split_compartment():
    backpack = [1, 2, 3, 4]
    left, right = split_compartment(backpack)
    assert left == [1, 2]
    assert right == [3, 4]