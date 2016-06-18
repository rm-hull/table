(defproject rm-hull/table "0.6.1-SNAPSHOT"
  :description "Display ascii tables for almost any data structure with ease"
  :url "http://github.com/rm-hull/table"
  :license {
    :name "The MIT License"
    :url "http://opensource.org/licenses/MIT"}
  :repl-options  {
    :init-ns table.core}
  :dependencies [
    [org.clojure/clojure "1.8.0"]]
  :scm {:url "git@github.com:rm-hull/infix.git"}
  :source-paths ["src"]
  :jar-exclusions [#"(?:^|/).git"]
  :codox {
    :source-paths ["src"]
    :output-path "doc/api"
    :source-uri "http://github.com/rm-hull/infix/blob/master/{filepath}#L{line}"  }
  :min-lein-version "2.6.1"
  :profiles {
    :dev {
      :global-vars {*warn-on-reflection* true}
      :plugins [
        [lein-codox "0.9.5"]
        [lein-cloverage "1.0.6"]]}})
