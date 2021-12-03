awk -v FS="," 'NR>1{$9=sprintf("%04d-%02d-%02d",substr($9,0,4),substr($9,4,2),substr($9,6,2));$10=sprintf("%04d-%02d-%02d",substr($10,0,4),substr($10,4,2),substr($10,6,2))}1' OFS="," calendar.txt
awk -v FS="," 'NR>1{$2=sprintf("%04d-%02d-%02d",substr($2,0,4),substr($2,4,2),substr($2,6,2))}1' OFS="," calendar_dates.txt
