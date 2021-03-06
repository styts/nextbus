(ns nextbus.core
    (:require [reagent.core :as reagent]
              [nextbus.time :refer [seconds->ms str->time mh till minutes->color]]
              [nextbus.wienerlinien :refer [transform-data]]
              [nextbus.utils :as u]
              [nextbus.prefs :as prefs]
              [ajax.core :refer [GET POST]]
               ))
;;;;;;;;;;;
;  atoms  ;
;;;;;;;;;;;
; these are not the only atoms

(defonce monitors (reagent/atom []))

;;;;;;;;;;
;  ajax  ;
;;;;;;;;;;

(defn receive-data [response]
  (let [mons (get (get response "data") "monitors")]
    (reset! monitors (map transform-data mons))))

(defn fetch-data []
  (GET "data.json" {:handler receive-data}))

(defonce data-fetcher (js/setInterval fetch-data (* 2 60 1000)))

;;;;;;;;;;;;;;;;
;  components  ;
;;;;;;;;;;;;;;;;

; these two ensure that the <li>s are redrawn every second
(defonce timer (reagent/atom (js/Date.)))
(defonce time-updater (js/setInterval #(reset! timer (js/Date.)) 1000))

(defn valid-departure [departure]
  (or (:planned departure) (:real departure)))

(defn li-class-helper [seconds-to-planned]
  (if (> 0 seconds-to-planned)
    "negative"))

(defn depart-li [idx departure]
  (if (valid-departure departure)
    (let [
          ; timer is needed to potentially trigger a redraw of the <li> every second
          _ @timer
          planned-str (:planned departure)
          real-str (:real departure)
          planned-ts (str->time planned-str)
          ; integers
          seconds-to-planned (till planned-ts)
          minutes-to-planned (int (divide seconds-to-planned 60))
          ; currently "negative" or nothing
          li-class (li-class-helper seconds-to-planned)
          ]
      (if (<= -60 seconds-to-planned)
        [:li.departure
         {:title planned-str :class li-class}
         [:span.mh (mh planned-ts)]
         (if real-str [:span.real (seconds->ms seconds-to-planned)]
         [:span.planned
          minutes-to-planned])
         ]))))

(defn render-monitor [m]
  (let [rbl (:rbl m)
        hidden (prefs/is-hidden rbl)
        icon #(str "hideme fa fa-" (if hidden "plus" "minus") "-square-o")
        ]
    [:div.monitor { :class (cond hidden "hidden") }
     [:div.heading { :class (prefs/get-color rbl)
                    :on-click #(if-not hidden (prefs/update-color! rbl)) }
      [:span.transport (:transport m)]
      [:span.stop-name (:stop-name m)]
      [:span [:i {:class "fa fa-long-arrow-right" :aria-hidden "true" }]]
      [:span.stop-name (:destination m)]
      [:i {:class (icon)
           :aria-hidden "true"
           :on-click (fn [e] (.stopPropagation e) (prefs/toggle-hide! rbl))
           }]
      ]
     (if-not hidden (into [:div.departures] (map-indexed (partial vector depart-li) (:departures m))))
     ]))

(defn render-monitors [mons]
  (into [:div.monitors] (map (partial vector render-monitor) mons)))

;;;;;;;;;;;
;  views  ;
;;;;;;;;;;;

(defn home-page []
  [:div.content
   [:div.container
    [:h2 "Aus Seestadt"]
    [render-monitors (u/sort-by-rbls (filter u/from-seestadt? @monitors) u/rbl-from)]
    ]
   [:div.container
    [:h2 "Nach Seestadt"]
    [render-monitors (filter u/to-seestadt? @monitors)]
    ]
   [:div.container.help
    [:h2 "Hilfe"]
    [:p
     "Die Abfahrtszeiten in " [:span {:class "rot"} "rot"]
     " sind die aktuellen, in " [:span {:class "schwarz"} "schwarz"] " die geplanten."
     ]
    [:p "Sie können die einzelnen Fahrtrichtungen mittels Klick farblich markieren oder mit "
     [:i {:class "fa fa-minus-square-o" :aria-hidden "true" }]
     " verstecken."]
    [:p "Der Code für diese app ist auf "
     [:a {:href "https://github.com/styts/nextbus"} "github"] "."]
    [:p "Datenquelle: Wiener Linien"]
    ]
   ])

;;;;;;;;;;;;;;;;;;;;
;  initialize app  ;
;;;;;;;;;;;;;;;;;;;;

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (do
    (fetch-data)
    (mount-root)))
