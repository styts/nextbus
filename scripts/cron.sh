wget -q "http://www.wienerlinien.at/ogd_realtime/monitor?rbl=3362&rbl=3359&rbl=3363&rbl=8682&rbl=4251&rbl=4277&rbl=3365&sender=lAlQnOt2p6D8HdvL" -O tmp.json > /dev/null
cat tmp.json | python -m json.tool > ../public/data.json && rm tmp.json
