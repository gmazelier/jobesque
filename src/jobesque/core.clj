(ns jobesque.core
	(:import (it.sauronsoftware.cron4j Scheduler SchedulingPattern)))

(def ^:dynamic *scheduler* (atom nil))

;; TODO Add "added" meta.

(defn initialized?
	"Return true if a scheduler instance exists, false otherwise."
	[]
	(instance? Scheduler @*scheduler*))

(defn initialize
  "Create a new scheduler instance if not exists."
  ([]
   (initialize (Scheduler.)))
  ([scheduler]
   (when (not (initialized?))
     (reset! *scheduler* scheduler))))

(defmacro when-initialized
  "Evaluates scheduler instance. If exists (logical true returned), evaluates body."
  [& body]
  `(when (initialized?)
    (do ~@body)))

(defn start
	"Starts the scheduler."
	[]
	(when-initialized
    (.start ^Scheduler @*scheduler*)))

(defn stop
	"Stops the scheduler."
	[]
	(when-initialized
    (.stop ^Scheduler @*scheduler*)))

(defn valid-pattern? ;; TODO Change contract and return true or false ?
	"Returns nil if the given pattern is valid, otherwise an AssertionError."
	[pattern]
	(assert (SchedulingPattern/validate pattern)
    (str "Pattern [" pattern "] not valid.")))

(defn schedule
	"Schedules a job given a scheduling pattern."
	[pattern job]
  (when-initialized
    (when (nil? (valid-pattern? pattern))
		  (.schedule ^Scheduler @*scheduler* pattern job))))
