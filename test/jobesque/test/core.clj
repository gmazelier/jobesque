(ns jobesque.test.core
	(:use [clojure.test])
	(:require [jobesque.core :as job]))

(deftest is-valid-every-minute
	(is (nil? (job/valid-pattern? "* * * * *"))))

(deftest is-valid-every-five-minutes
	(is (nil? (job/valid-pattern? "*/5 * * * *"))))

(deftest is-valid-every-minute-of-noon-on-monday
	(is (nil? (job/valid-pattern? "* 12 * * Mon"))))

(deftest is-valid-every-working-day-at-noon
	(is (nil? (job/valid-pattern? "0 12 * * 1,2,3,4,5"))))
(deftest is-not-valid-empty-string
	(is (thrown? AssertionError (job/valid-pattern? ""))))

(deftest is-not-valid-another-string
	(is (thrown? AssertionError (job/valid-pattern? "this is not a valide pattern"))))

(deftest is-not-valid-nil
	(is (thrown? NullPointerException (job/valid-pattern? nil))))
