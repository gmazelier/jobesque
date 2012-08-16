(ns jobesque.test.core
  (:use [clojure.test])
  (:require [jobesque.core :as job]))

(deftest valid-pattern?
  (testing "Returns nil when pattern is valid."
    (testing "Some valid patterns."
      (is (nil? (job/valid-pattern? "* * * * *")))
      (is (nil? (job/valid-pattern? "*/5 * * * *")))
      (is (nil? (job/valid-pattern? "0-15/5 * * * *")))
      (is (nil? (job/valid-pattern? "* 12 * * mon")))
      (is (nil? (job/valid-pattern? "0 12 * * 1,2,3,4,5")))
      (is (nil? (job/valid-pattern? "0 12 * * 1-5"))))
    (testing "Patterns composition."
      (is (nil? (job/valid-pattern? "0 0 * * *|1 1 * * *|2 2 * * *")))))
  (testing "Throws AssertionError when pattern is invalid."
    (testing "Illegal arguments."
      (is (thrown? AssertionError (job/valid-pattern? "")))
      (is (thrown? AssertionError (job/valid-pattern? "this is not a valide pattern")))
      (is (thrown? NullPointerException (job/valid-pattern? nil))))
    (testing "Unknown aliases."
      (is (thrown? AssertionError (job/valid-pattern? "mm * * * *")))
      (is (thrown? AssertionError (job/valid-pattern? "* hh * * *")))
      (is (thrown? AssertionError (job/valid-pattern? "* * dd * *")))
      (is (thrown? AssertionError (job/valid-pattern? "* * * mm *")))
      (is (thrown? AssertionError (job/valid-pattern? "* * * * ww"))))
    (testing "Values too small."
      (is (thrown? AssertionError (job/valid-pattern? "* * 0 * *")))
      (is (thrown? AssertionError (job/valid-pattern? "* * * 0 *"))))
    (testing "Values too big."
      (is (thrown? AssertionError (job/valid-pattern? "60 * * * *")))
      (is (thrown? AssertionError (job/valid-pattern? "* 24 * * *")))
      (is (thrown? AssertionError (job/valid-pattern? "* * 32 * *")))
      (is (thrown? AssertionError (job/valid-pattern? "* * * 13 *")))
      (is (thrown? AssertionError (job/valid-pattern? "* * * * 8"))))
    (testing "Illegal characters."
      (is (thrown? AssertionError (job/valid-pattern? "* * * * 0,2;4"))))))
