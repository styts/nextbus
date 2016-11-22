# Seestadt Vienna public transport departures

I made this to solve the need to know when exactly the next bus departs from
Seestadt, as well as to practice making a SPA with ClojureScript and Reagent.

The only server-side component is a cronjob that runs once per minute querying
the WienerLininen API and writes the result to `data.json` in the public
folder.

## Installation

If you don't have Leiningen installed, just use your system's package manager
to install it, e.g. on OSX:

    brew install leiningen

Run the dev server

    lein figwheel

Serve statics:

    cd public && python -m SimpleHTTPServer

Now you can open [http://localhost:8000/](http://localhost:8000/) in your browser.

If you want to edit styles, watch the sass stylesheets:

    fswatch -o sass/ | xargs -n1 -I{} sassc sass/app.scss public/css/site.css

You should now be able to edit the files and see the changes reloaded in the
browser on-the-fly.

## Cronjob

Edit the crontab:

    crontab -e

And add a minutely cronjob:

    * * * * * /path/to/nextbus/scripts/cron.sh

You need to edit `cron.sh` to supply a WienerLininen API key that can be
requested through [this form](https://www.wien.gv.at/formularserver2/user/formular.aspx?pid=3b49a23de1ff43efbc45ae85faee31db&pn=B0718725a79fb40f4bb4b7e0d2d49f1d1).
They have responded with the key on the next workday in my case.

## RBLs (line/direction IDs)

    JKG
    3360
    3362 -> Asp *

    HAP (to See)
    3359
    3363

    ASP (both)
    8682 84a
    4251 u2

    SEE (both)
    4277 u2
    3365 84a
