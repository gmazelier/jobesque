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

(defmacro with-valid-pattern
  "Evaluates pattern validity. If valid (logical true returned), evaluates body."
  {:added "0.0.2"}
  [pattern & body]
  `(when (nil? (valid-pattern? ~pattern))
    (do ~@body)))

(defn schedule
  "Schedules a job given a scheduling pattern. Returns job ID when successful."
  {:added "0.0.1"}
  [pattern job]
  (when-initialized
    (with-valid-pattern pattern
      (dosync
        (let [id (.schedule ^Scheduler @*scheduler* pattern job)]
          (commute all-jobs assoc id {:pattern pattern})
          id)))))

(defn jobs
  "Returns jobs collection as a sequence."
  {:added "0.0.2"}
  []
  (map #(assoc %2 :id %1) (keys @all-jobs) (vals @all-jobs)))

(defmacro with-job
  "Evaluates if a job identified by the given id is present. If exists (logical true returned), evaluates body."
  {:added "0.0.2"}
  [id & body]
  `(when (contains? @all-jobs ~id)
    (do ~@body)))

(defn job
  "Returns the job identified by the given id, if exists."
  {:added "0.0.2"}
  [id]
  (with-job id
    (assoc (@all-jobs id) :id id)))

(defn deschedule
  "Deschedules a job and removes it from the jobs collection."
  {:added "0.0.2"}
  [id]
  (with-job id
    (dosync
      (.deschedule ^Scheduler @*scheduler* id)
      (alter all-jobs dissoc id)
      id)))
