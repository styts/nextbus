(ns nextbus.prod
  (:require [nextbus.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
