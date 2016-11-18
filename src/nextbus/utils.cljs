(ns nextbus.utils)

; see README.md for the list of RBLs

(def rbl-from [3362 4251 3363 3359 4277])
(def rbl-to [8682 3365])

(defn to-seestadt? [x]
  (contains? (set rbl-to) (:rbl x)))

(defn from-seestadt? [x] (not (to-seestadt? x)))

(defn sort-by-rbls
  "Given a seq of monitors, and a list of RBLs, sort the monitors by rbl"
  [monitors rbls]
    (map
      (fn [idx] (first (filter
                         (fn [m] (= (:rbl m) idx))
                         monitors)))
      rbls))
