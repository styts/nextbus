(ns nextbus.time
  (:require [cljs-time.format :refer [parse-local unparse-local formatter]]
            [cljs-time.core :refer [now after? in-seconds in-minutes interval]]
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
      (in-seconds (interval now ts))
      (- (in-seconds (interval ts now)))
      )))

(defn seconds->ms [seconds]
  (let [m (in-minutes (cljs-time.core/seconds seconds))
        s (- seconds (* 60 m))]
    (str m ":"
         (if (< s 10) (str "0" s) s))))

(comment
  (seconds->ms 600)
  (seconds->ms 601)
  (> 10 9)
  (str->time "2016-11-15T11:41:16.920+0100")
  (mh (str->time "2016-11-15T11:41:16.920+0100"))
  (counter (str->time "2016-11-15T11:41:16.920+0100"))
  )
