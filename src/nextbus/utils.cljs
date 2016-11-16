(ns nextbus.utils)

; see README.md for the list of RBLs

(defn to-seestadt? [x]
  (contains? #{8682 3365} (:rbl x)))

(defn from-seestadt? [x] (not (to-seestadt? x)))

(comment
  (contains? #{8682 3365} 8682)
  (contains? #{8682 3365} 863)
  )
