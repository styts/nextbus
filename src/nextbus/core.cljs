(ns nextbus.core
    (:require [reagent.core :as reagent]
              [nextbus.time :refer [seconds->ms str->time mh till]]
              [nextbus.wienerlinien :refer [transform-data]]
              [nextbus.utils :as u]
              [ajax.core :refer [GET POST]]
               ))
;;;;;;;;;;;
;  atoms  ;
;;;;;;;;;;;

(defonce monitors (reagent/atom []))
; these are not the only ones

;;;;;;;;;;
;  ajax  ;
;;;;;;;;;;

(defn receive-data [response]
  (let [mons (get (get response "data") "monitors")]
    (reset! monitors (map transform-data mons))))

(defn fetch-data []
  (GET "/data.json" {:handler receive-data}))

(defonce data-fetcher (js/setInterval fetch-data (* 2 60 1000)))

;;;;;;;;;;;;;;;;
;  components  ;
;;;;;;;;;;;;;;;;

; these two ensure that the <li>s are redrawn every second
(defonce timer (reagent/atom (js/Date.)))
(defonce time-updater (js/setInterval #(reset! timer (js/Date.)) 1000))

(defn depart-li [idx departure]
  (if (:time departure) (let [
        ts-string (:time departure)
        real (:real departure)
        ts (str->time ts-string)
        _ @timer
        till (till ts)]
    (if (<= 0 till)
      [:li.departure
       {:title ts-string}
       [:span.mh (mh ts)]
       [:span.till
        {:class (if real "real")}
        (if real (seconds->ms till) (int (divide till 60)))]
       ]))))

(defn render-monitor [m]
  [:div.monitor
   [:div.heading
    ;[:span.transport (:rbl m)]
    ;[:span " : "]
    [:span.transport (:transport m)]
    [:span.stop-name (:stop-name m)]
    [:span "->"]
    [:span.stop-name (:destination m)]]
   (into [:div.departures] (map-indexed (partial vector depart-li) (:departures m)))
   ])

(defn render-monitors [mons]
  (into [:div.monitors] (map (partial vector render-monitor) mons)))

;;;;;;;;;;;
;  views  ;
;;;;;;;;;;;

(defn home-page []
  [:div
   [:div.direction
    [:h2 "Aus Seestadt"]
    [render-monitors (u/sort-by-rbls (filter u/from-seestadt? @monitors) u/rbl-from)]
    ]
   [:div.direction
    [:h2 "Nach Seestadt"]
    [render-monitors (filter u/to-seestadt? @monitors)]
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
    (mount-root)
    ))
