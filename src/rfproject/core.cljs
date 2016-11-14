(ns rfproject.core
    (:require [reagent.core :as reagent]
              [rfproject.wienerlinien :as wl]
              [ajax.core :refer [GET POST]]
               ))
;;;;;;;;;;;
;  atoms  ;
;;;;;;;;;;;

(def monitors (reagent/atom []))

;;;;;;;;;;
;  ajax  ;
;;;;;;;;;;

(defn receive-data [response]
  (let [mons (get (get response "data") "monitors")]
    (reset! monitors mons)
    (.info js/console mons)))

(GET "/data.json" {:handler receive-data})

;;;;;;;;;;;;;;;;
;  components  ;
;;;;;;;;;;;;;;;;

(defn render-monitor [monitor]
  [:div ["monitor" (str monitor)]])

(defn render-monitors [mons]
  [:div (map render-monitor mons)])

;; -------------------------
;; Views
(defn box []
  [:div.box])

;(defn boxes [] [:div (repeat 3 (box))])

(defn home-page []
  [:div [:h2 "Next bus"]
   [render-monitors @monitors]
   ])

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
