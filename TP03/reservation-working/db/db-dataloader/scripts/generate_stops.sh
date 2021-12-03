cat ../resources/migrations/intercites/stops.txt ../resources/migrations/tgv/stops.txt >../resources/migrations/stops.txt

sort -u stops.txt >stops.csv
