(ns jobesque.core
  (:import (it.sauronsoftware.cron4j Scheduler SchedulingPattern)))

(def all-jobs (ref {}))

(defn clear-jobs
  "Clears jobs collection."
  {:added "0.0.2"}
  []
  (dosync
    (ref-set all-jobs {})))

(def ^:dynamic *scheduler* (atom nil))

(defn on-reset
  "Calls clear-jobs on scheduler reset."
  {:added "0.0.2"}
  [the-key the-ref old-value new-value]
  (clear-jobs))

(add-watch *scheduler* :reset-watcher on-reset)

(defn initialized?
  "Returns true if a scheduler instance exists, false otherwise."
  {:added "0.0.1"}
  []
  (instance? Scheduler @*scheduler*))

(defn initialize
  "Creates a new scheduler instance if not exists."
  {:added "0.0.1"}
  ([]
   (initialize (Scheduler.)))
  ([scheduler]
   (when (not (initialized?))
     (reset! *scheduler* scheduler))))

(defmacro when-initialized
  "Evaluates scheduler instance. If exists (logical true returned), evaluates body."
  {:added "0.0.1"}
  [& body]
  `(when (initialized?)
     (do ~@body)))

(defn start
  "Starts the scheduler."
  {:added "0.0.1"}
  []
  (when-initialized
    (.start ^Scheduler @*scheduler*)))

(defn stop
  "Stops the scheduler."
  {:added "0.0.1"}
  []
  (when-initialized
    (.stop ^Scheduler @*scheduler*)))

(defn valid-pattern? ;; TODO Change contract and return true or false ?
  "Returns nil if the given pattern is valid, otherwise an AssertionError."
  {:added "0.0.1"}
  [pattern]
  (assert (SchedulingPattern/validate pattern)
    (str "Pattern [" pattern "] not valid.")))

(defn schedule
  "Schedules a job given a scheduling pattern."
  {:added "0.0.1"}
  [pattern job]
  (when-initialized
    (when (nil? (valid-pattern? pattern))
      (.schedule ^Scheduler @*scheduler* pattern job))))
