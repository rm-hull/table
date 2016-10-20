(ns table.ansi
  (:require
   [clojure.string :as s]))

(def ^:private pattern (re-pattern #"\u001b\[\d+m"))

(defn strip-ansi [text]
  (s/replace (str text) pattern ""))

(def reset-ansi
  "\u001b[0m")

(defn suffix-reset-ansi [text]
  (if (re-find pattern text)
    (str text reset-ansi)
    text))
