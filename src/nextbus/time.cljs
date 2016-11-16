(ns nextbus.time
  (:require [cljs-time.format :refer [parse-local unparse-local formatter]]
            [cljs-time.core :refer [now after? in-minutes interval]]
            ))

;(f/show-formatters)

(defn str->time [s]
  (parse-local
    (formatter "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    s ))


(defn mh [ts]
  (unparse-local (formatter "HH:mm") ts))

(defn till [ts]
  (let [now (now)]
    (if (after? ts now)
      (in-minutes (interval now ts))
      (- (in-minutes (interval ts now)))
      )))

(comment
  (str->time "2016-11-15T11:41:16.920+0100")
  (mh (str->time "2016-11-15T11:41:16.920+0100"))
  (counter (str->time "2016-11-15T11:41:16.920+0100"))
  )
