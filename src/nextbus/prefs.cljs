(ns nextbus.prefs
  (:require
    [reagent.core :as reagent]
    [alandipert.storage-atom :refer [local-storage]]
    ))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;  user preferences: colors, hiding  ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defonce hidden-monitors (local-storage (reagent/atom {}) :hidden-monitors))

(defonce colors (local-storage (reagent/atom {3359 "violet", 4251 "green",
                                              4277 "violet", 3365 "violet",
                                              8682 "green", 3362 "green",
                                              3363 "orange"}) :colors))
(def possible-colors ["red" "violet" "green" "orange"])
;;;;;;;;;;;;;;;;;;;
;  hide monitors  ;
;;;;;;;;;;;;;;;;;;;

(defn is-hidden [rbl]
  (= true (get @hidden-monitors rbl false)))

(defn toggle-hide! [rbl]
  (swap! hidden-monitors assoc rbl (not (is-hidden rbl))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;  color of monitor headings  ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-color [rbl]
  (get @colors rbl (first possible-colors)))

(defn update-color! [rbl]
  (let [current (get-color rbl)
        possible (take (inc (count possible-colors)) (cycle possible-colors))
        idx (.indexOf possible current)
        next-color (nth possible (inc idx))
        ]
    (swap! colors assoc rbl next-color)))
