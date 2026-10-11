#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <path> <mask>"
    exit 1
fi

DIR="$1"
EXT="$2"

if [ ! -d "$DIR" ]; then
    echo "Error: '$DIR' does not exists."
    exit 1
fi

total_lines=$(find "$DIR" -type f -name "*.$EXT" -exec wc -l {} + | awk '{sum += $1} END {print sum}')

echo "Total lines of code in .$EXT: $total_lines."