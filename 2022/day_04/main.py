from camp_cleanup.camp import is_contained, split_line, split_elf, count_full_contain, count_overlaps


def main():
    with open("input.txt", encoding='utf-8') as file:
        print(count_full_contain(file))
    with open("input.txt", encoding='utf-8') as file:
        print(count_overlaps(file))


if __name__ == "__main__":
    main()
