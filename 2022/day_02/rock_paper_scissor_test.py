import pytest

from rock_paper_scissor import calc_score, map_move, outcome, split_line, ROCK, PAPER, SCISSOR, find_move, WIN, LOSE

predictions = [('X', 1), ('Y', 2), ('Z', 3), ('A',1), ('B',2), ('C',3)]

moves = {1,2,3}

combinations = [(ROCK, PAPER), (PAPER,SCISSOR) , (SCISSOR,ROCK)]

@pytest.mark.parametrize("prediction, expected", predictions)
def test_map_move_to_number(prediction, expected):
    result = map_move(prediction)
    assert result == expected 

@pytest.mark.parametrize("move", moves)
def test_draw(move):
    result = outcome((move, move))
    assert result == 3

@pytest.mark.parametrize("elf, me", combinations)
def test_win(elf, me):
    result = outcome((elf, me))
    assert result == 6

@pytest.mark.parametrize("me, elf", combinations)
def test_lose(elf, me):
    result = outcome((elf, me))
    assert result == 0

def test_calc_score():
    result = calc_score((1,2))
    assert result == 8

def test_split_line():
    elf, me = split_line("A X")
    assert elf == 'A' 
    assert me  == 'X' 

@pytest.mark.parametrize("move, expected", combinations)
def test_find_wins(move, expected):
    result = find_move(move,WIN)
    assert result == expected 

@pytest.mark.parametrize("expected, move", combinations)
def test_find_wins(move, expected):
    result = find_move(move,LOSE)
    assert result == expected 
