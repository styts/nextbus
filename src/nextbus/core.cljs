(ns nextbus.core
    (:require [reagent.core :as reagent]
              [nextbus.wienerlinien :as wl]
              [ajax.core :refer [GET POST]]
               ))
;;;;;;;;;;;
;  atoms  ;
;;;;;;;;;;;

(def monitors
  (reagent/atom
    [
     {:stop-name "My Stop" :transport "84A" :destination "Seestadt"
      :departures ["12 minutes" "whenever"]}
     {:stop-name "My Stop" :transport "U2" :destination "Someplace"
      :departures ["3 minutes" "someday"]}
     ]))

;;;;;;;;;;
;  ajax  ;
;;;;;;;;;;

(defn receive-data [response]
  (let [mons (get (get response "data") "monitors")]
    (reset! monitors (map wl/transform-data mons))
    (.info js/console mons)))

;;;;;;;;;;;;;;;;
;  components  ;
;;;;;;;;;;;;;;;;

(defn departures [m]
  (map #(with-meta (vec [:li %]) {:key %}) (:departures m)))

(defn render-monitor [m]
  [:div.monitor
   [:div.heading
    [:span.transport (:transport m)]
    [:span.stop-name (:stop-name m)]
    [:span "->"]
    [:span.stop-name (:destination m)]]
   [:ul (departures m)]
   ])

(defn render-monitors [mons]
  (into [:div.monitors] (map (partial vector render-monitor) mons)))

;;;;;;;;;;;
;  views  ;
;;;;;;;;;;;

(defn home-page []
  [:div [:h2 "Next bus"]
   [render-monitors @monitors]])

;;;;;;;;;;;;;;;;;;;;
;  initialize app  ;
;;;;;;;;;;;;;;;;;;;;

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (do
    (mount-root)
    (GET "/data.json" {:handler receive-data})))
