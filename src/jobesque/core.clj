(ns jobesque.core
	(:import (it.sauronsoftware.cron4j Scheduler SchedulingPattern)))

(def ^:dynamic *scheduler* (atom nil))

(defn initialized
	"Create a new scheduler instance."
	([]
		(initialized (Scheduler.)))
	([scheduler]
		(reset! *scheduler* scheduler)))

(defn initialized?
	"Return true if a scheduler instance exist, false otherwise."
	[]
	(instance? Scheduler @*scheduler*))

(defmacro when-initialized
  "Evaluates scheduler instance. If exists (logical true returned), evaluates body."
  [& body]
  `(when (initialized?)
    (do ~@body)))

(defn start
	"Start the scheduler."
	[]
	(.start ^Scheduler @*scheduler*))

(defn stop
	"Stop the scheduler."
	[]
	(.stop ^Scheduler @*scheduler*))

(defn valid-pattern? ;; TODO Change contract and return true or false ?
	"Return nil if the given pattern is valid, otherwise an AssertionError."
	[pattern]
	(assert (SchedulingPattern/validate pattern) (str "Pattern [" pattern "] not valid.")))

(defn schedule
	"Schedule a job given a scheduling pattern."
	[pattern job]
	(when (nil? (valid-pattern? pattern))
		(.schedule ^Scheduler @*scheduler* pattern job)))
