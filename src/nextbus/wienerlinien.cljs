(ns nextbus.wienerlinien)

(defn stop-name [m]
  (get (get (get m "locationStop") "properties") "title"))

(defn rbl [m]
  (get (get (get (get m "locationStop") "properties") "attributes") "rbl"))

(defn transport [m]
  (get (first (get m "lines")) "name"))

(defn destination [m]
  (get (first (get m "lines")) "towards"))

(defn extract-time [departure]
  (let [real (contains? departure "timeReal")]
    {
     :time (get departure "timeReal" (get departure "timePlanned"))
     :real real }))

(defn departures [m]
  (map #(extract-time (get % "departureTime"))
       (get (get (first (get m "lines")) "departures") "departure")))

(defn transform-data [m]
  {:stop-name (stop-name m)
   :transport (transport m)
   :destination (destination m)
   :departures (departures m)
   :rbl (rbl m)
   })
