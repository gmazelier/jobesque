(ns jobesque.core
  (:import (it.sauronsoftware.cron4j Scheduler SchedulingPattern)))

;; ## Scheduler

;; Jobs collection.
(def all-jobs (ref {}))

(defn clear-jobs
  "Clears jobs collection."
  {:added "0.0.2"}
  []
  (dosync
    (ref-set all-jobs {})))

;; Scheduler instance.
(def ^:dynamic *scheduler* (atom nil))

(defn on-reset
  "Calls `clear-jobs` on scheduler reset."
  {:added "0.0.2"}
  [the-key the-ref old-value new-value]
  (clear-jobs))

;; Scheduler monitoring.
(add-watch *scheduler* :reset-watcher on-reset)

(defn initialized?
  "Returns `true` if a scheduler instance exists, `false` otherwise."
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

(defn started?
  "Returns `true` if a scheduler instance is started, `false` otherwise."
  {:added "0.1.0"}
  []
  (when-initialized
    (.isStarted ^Scheduler @*scheduler*)))

(defmacro when-started
  "Evaluates scheduler status. If started (logical true returned), evaluates body."
  {:added "0.1.0"}
  [& body]
  `(when (started?)
     (do ~@body)))

(defn stop
  "Stops the scheduler."
  {:added "0.0.1"}
  []
  (when-started
    (.stop ^Scheduler @*scheduler*)))

;; ## Patterns

(defn valid-pattern?
  "Returns nil if the given pattern is valid, otherwise an `AssertionError`."
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

;; ## Jobs

(defn jobs
  "Returns jobs collection as a sequence."
  {:added "0.0.2"}
  []
  (map #(assoc %2 :id %1) (keys @all-jobs) (vals @all-jobs)))

(defmacro with-job
  "Evaluates if a job identified by the given id is present.
  If exists (logical true returned), evaluates body."
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

(defn launch
  "Executes immediately an existing job, without scheduling it."
  {:added "0.1.0"}
  [id]
  (when-started
    (with-job id
      (do
        (.launch ^Scheduler @*scheduler*
          (.getTask ^Scheduler @*scheduler* id))
        id))))

;; ## Scheduling tasks

(defn schedule
  "Schedules a job given a scheduling pattern.
  Returns job ID when successful, `nil` otherwise."
  {:added "0.0.1"}
  [pattern job]
  (when-initialized
    (with-valid-pattern pattern
      (dosync
        (let [id (.schedule ^Scheduler @*scheduler* pattern job)]
          (commute all-jobs assoc id {:pattern pattern})
          id)))))

(defn deschedule
  "Deschedules a job and removes it from the jobs collection.
  Returns job ID when successful, `nil` otherwise."
  {:added "0.0.2"}
  [id]
  (when-initialized
    (with-job id
      (dosync
        (.deschedule ^Scheduler @*scheduler* id)
        (alter all-jobs dissoc id)
        id))))

(defn reschedule
  "Reschedules a job and updates the jobs collection.
  Returns job ID when successful, `nil` otherwise."
  {:added "0.0.2"}
  [id pattern]
  (when-initialized
    (with-job id
      (with-valid-pattern pattern
        (dosync
          (.reschedule ^Scheduler @*scheduler* id pattern)
          (alter all-jobs assoc id (assoc (@all-jobs id) :pattern pattern))
          id)))))
