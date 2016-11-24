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
        s (- seconds (* 60 m))
        a (Math/abs s)
        ]
    (str (if (not (= s a)) "-") m ":"
         (if (< a 10) (str "0" a) a))))

(comment
  (seconds->ms 600)
  (seconds->ms -20)
  (seconds->ms 601)
  (Math/abs -29)
  (> 10 9)
  (str->time "2016-11-15T11:41:16.920+0100")
  (mh (str->time "2016-11-15T11:41:16.920+0100"))
  (counter (str->time "2016-11-15T11:41:16.920+0100"))
  )

;;;;;;;;;;;;
;  colors  ;
; color array taken from:
; http://gka.github.io/palettes/#colors=crimson,orange,yellow,white|steps=10|bez=1|coL=0
;;;;;;;;;;;;
; these are not used, because it's too much visual information overload for the user

(def ^:private color-string "#dc143c #c21936 #a81c30 #8f1c2a #761b24 #5f1a1e #481718 #321313 #1e0c0a #000000")
(def ^:private colors (clojure.string/split color-string #" "))

(defn minutes->color [m]
    (get colors m (last colors)))

(comment
  (minutes->color 8)
  (minutes->color 9)
  (minutes->color 10)
  (minutes->color 11)
  )
