(ns jobesque.core
  (:import (it.sauronsoftware.cron4j Scheduler)))

(def ^:dynamic *scheduler* (atom nil))

(defn init
  "Create a new scheduler instance."
  ([]
     (init (Scheduler.)))
  ([scheduler]
     (reset! *scheduler* scheduler)))

(defn start
  "Starts the scheduler."
  []
  (.start ^Scheduler @*scheduler*))

(defn stop
  "Stops the scheduler."
  []
  (.stop ^Scheduler @*scheduler*))

(defn schedule
  "Schedule a job given a scheduling pattern."
  [pattern job]
  (.schedule ^Scheduler @*scheduler* pattern job))
