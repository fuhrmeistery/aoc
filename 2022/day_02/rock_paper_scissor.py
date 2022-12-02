"""
Day two of advent of code
"""
from typing import Tuple
ROCK = 1
PAPER = 2
SCISSOR = 3

LOSE = 1
DRAW = 2
WIN = 3

def map_move(action: str) -> int:
    if action in ('X', 'A'):
        return ROCK
    if action in ('Y', 'B'):
        return PAPER
    return SCISSOR

def outcome(combination: Tuple[int, int]) -> int:
    left, right = combination
    if left == right:
        return 3

    if left == SCISSOR and right == ROCK:
        return 6

    if left == ROCK and right == PAPER:
        return 6

    if left == PAPER and right == SCISSOR:
        return 6
    return 0

def calc_score(runde: Tuple[int, int]) -> int:
    """
    calculates the score for a prediction and a move
    """
    left, right = runde
    out = outcome((left, right))
    return out + right

def split_line(row: str) -> Tuple[str, str]:
    con =  row.split(' ')
    return (con[0], con[1])

def win(action: int) -> int:
    return SCISSOR if action == PAPER else PAPER if action == ROCK else ROCK

def lose(action: int) -> int:
    return PAPER if action == SCISSOR else ROCK if action == PAPER else SCISSOR

def find_move(action: int, res: int) -> int:
    if res == DRAW:
        return action
    if res == WIN:
        return win(action)
    return lose(action)

if __name__ == "__main__":
    with open("input_test.txt", encoding= 'utf-8') as file:
        x = 0
        for line in file:
            elf, me = split_line(line.strip())
            elf = map_move(elf)
            me = map_move(me)
            x += calc_score((elf, me))

        print(x)

    with open("input.txt", encoding= 'utf-8') as file:
        x = 0
        for line in file:
            elf, out = split_line(line.strip())
            out = map_move(out)
            elf = map_move(elf)
            move = find_move(elf, out)
            x += calc_score((elf, move))

        print(x)
