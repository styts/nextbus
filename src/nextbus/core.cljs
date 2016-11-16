(ns nextbus.core
    (:require [reagent.core :as reagent]
              [nextbus.time :refer [str->time mh till]]
              [nextbus.wienerlinien :refer [transform-data]]
              [ajax.core :refer [GET POST]]
               ))
;;;;;;;;;;;
;  atoms  ;
;;;;;;;;;;;

(defonce monitors (reagent/atom []))
(comment
  "Initial value of the monitor atom"
  [
     {:stop-name "My Stop" :transport "84A" :destination "Seestadt"
      :departures ["2016-11-15T11:41:16.920+0100"]}
     {:stop-name "My Stop" :transport "U2" :destination "Someplace"
      :departures ["2016-11-15T11:46:16.920+0100"]}
     ]
  )
;;;;;;;;;;
;  ajax  ;
;;;;;;;;;;

(defn receive-data [response]
  (let [mons (get (get response "data") "monitors")]
    (reset! monitors (map transform-data mons))
    (.info js/console mons)))

(defn fetch-data []
  (GET "/data.json" {:handler receive-data}))

;;;;;;;;;;;;;;;;
;  components  ;
;;;;;;;;;;;;;;;;

(defn depart-li [ts]
  (let [ts (str->time ts)
        till (till ts)]
    (if (<= 0 till)
      [:li.departure
       [:span.mh (mh ts)]
       [:span.till till]
       ])))

(defn render-monitor [m]
  [:div.monitor
   [:div.heading
    [:span.transport (:transport m)]
    [:span.stop-name (:stop-name m)]
    [:span "->"]
    [:span.stop-name (:destination m)]]
   (into [:div.departures] (take 5 (map (partial vector depart-li) (:departures m))))
   ])

(defn render-monitors [mons]
  (into [:div.monitors] (map (partial vector render-monitor) mons)))

;;;;;;;;;;;
;  views  ;
;;;;;;;;;;;

(defn home-page []
  [:div ;[:h2 "Next bus"]
   [render-monitors @monitors]])

;;;;;;;;;;;;;;;;;;;;
;  initialize app  ;
;;;;;;;;;;;;;;;;;;;;

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (do (mount-root)
      (fetch-data)))
