"""
This is a Module docstring
"""
from typing import List

def count_cal(items: List[int]) -> int:
    """
    Counts the calories of a given list
    """
    result = sum(items)
    return result

def find_max_cal(calories: List) -> int:
    return max(calories)

def read_file(filename: str)-> List[List[int]]:
    elfs = []
    elf = []
    with open(filename, encoding='utf-8') as calories:
        for calorie in calories:
            if calorie.strip() == "":
                elfs.append(elf)
                elf = []
            else:
                elf.append(int(calorie))
        elfs.append(elf)
    return elfs

if __name__ == "__main__":
    elfs = read_file("input.txt")
    elf_calories = list(map(count_cal, elfs))
    max_cal = find_max_cal(elf_calories)
    print(max_cal)
    three = sorted(elf_calories)[-3:]
    print(sum(three))
