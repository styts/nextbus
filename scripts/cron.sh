wget -q "http://www.wienerlinien.at/ogd_realtime/monitor?rbl=3360&rbl=3362&sender=lAlQnOt2p6D8HdvL" -O tmp.json > /dev/null
cat tmp.json | python -m json.tool > ../public/data.json && rm tmp.json
