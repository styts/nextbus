# change this to your key
WIENERLINIEN_API_KEY=$(pass api/wienerlinien)

wget -q \
    "http://www.wienerlinien.at/ogd_realtime/monitor?\
rbl=3362&rbl=3363&rbl=3359&rbl=8682&rbl=4251&rbl=4277&rbl=3365\
&sender=$WIENERLINIEN_API_KEY" \
    -O /Users/kirill/Projects/nextbus/public/data.json >> /dev/null
