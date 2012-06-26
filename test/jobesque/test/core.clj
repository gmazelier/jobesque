(ns jobesque.test.core
  (:use [clojure.test])
  (:require [jobesque.core :as job]))

(deftest is-valid-every-minute
  (is (nil? (job/valid-pattern? "* * * * *"))))

(deftest is-valid-every-five-minutes
  (is (nil? (job/valid-pattern? "*/5 * * * *"))))

(deftest is-valid-every-five-minutes-the-first-quarter
  (is (nil? (job/valid-pattern? "0-15/5 * * * *"))))

(deftest is-valid-every-minute-of-noon-on-monday
  (is (nil? (job/valid-pattern? "* 12 * * mon"))))

(deftest is-valid-every-working-day-at-noon
  (is (nil? (job/valid-pattern? "0 12 * * 1,2,3,4,5")))
  (is (nil? (job/valid-pattern? "0 12 * * 1-5"))))

(deftest is-valid-patterns-composition
  (is (nil? (job/valid-pattern? "0 0 * * *|1 1 * * *|2 2 * * *"))))

(deftest is-not-valid-empty-string
  (is (thrown? AssertionError (job/valid-pattern? ""))))

(deftest is-not-valid-another-string
  (is (thrown? AssertionError (job/valid-pattern? "this is not a valide pattern"))))

(deftest is-not-valid-nil
  (is (thrown? NullPointerException (job/valid-pattern? nil))))

(deftest is-not-valid-minutes-unknwon-alias
  (is (thrown? AssertionError (job/valid-pattern? "mm * * * *"))))

(deftest is-not-valid-minute-too-big
  (is (thrown? AssertionError (job/valid-pattern? "60 * * * *"))))

(deftest is-not-valid-hour-unknown-alias
  (is (thrown? AssertionError (job/valid-pattern? "* hh * * *"))))

(deftest is-not-valid-hour-too-big
  (is (thrown? AssertionError (job/valid-pattern? "* 24 * * *"))))

(deftest is-not-valid-dom-unknown-alias
  (is (thrown? AssertionError (job/valid-pattern? "* * dd * *"))))

(deftest is-not-valid-dom-too-small
  (is (thrown? AssertionError (job/valid-pattern? "* * 0 * *"))))

(deftest is-not-valid-dom-too-big
  (is (thrown? AssertionError (job/valid-pattern? "* * 32 * *"))))

(deftest is-not-valid-month-unknown-alias
  (is (thrown? AssertionError (job/valid-pattern? "* * * mm *"))))

(deftest is-not-valid-month-too-small
  (is (thrown? AssertionError (job/valid-pattern? "* * * 0 *"))))

(deftest is-not-valid-month-too-big
  (is (thrown? AssertionError (job/valid-pattern? "* * * 13 *"))))

(deftest is-not-valid-dow-unknown-alias
  (is (thrown? AssertionError (job/valid-pattern? "* * * * ww"))))

(deftest is-not-valid-dow-too-big
  (is (thrown? AssertionError (job/valid-pattern? "* * * * 8"))))

(deftest is-not-valid-bad-special-character
  (is (thrown? AssertionError (job/valid-pattern? "* * * * 0,2;4"))))
