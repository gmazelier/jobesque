(ns jobesque.core
	(:import (it.sauronsoftware.cron4j Scheduler SchedulingPattern)))

(def ^:dynamic *scheduler* (atom nil))

(defn init
	"Create a new scheduler instance."
	([]
		(init (Scheduler.)))
	([scheduler]
		(reset! *scheduler* scheduler)))

(defn initialized?
	"Return true if a scheduler instance exist, false otherwise."
	[]
	(instance? Scheduler @*scheduler*))

(defn start
	"Start the scheduler."
	[]
	(.start ^Scheduler @*scheduler*))

(defn stop
	"Stop the scheduler."
	[]
	(.stop ^Scheduler @*scheduler*))

(defn is-valid-pattern
	"Return nil if the given pattern is valid, otherrise an AssertionError."
	[pattern]
	(assert (SchedulingPattern/validate pattern) (str "Pattern [" pattern "] not valid.")))

(defn schedule
	"Schedule a job given a scheduling pattern."
	[pattern job]
	(when (nil? (is-valid-pattern pattern))
		(.schedule ^Scheduler @*scheduler* pattern job)))
