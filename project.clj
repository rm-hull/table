(defproject rm-hull/table "0.7.1"
  :description "Display ascii tables for almost any data structure with ease"
  :url "http://github.com/rm-hull/table"
  :license {
    :name "The MIT License"
    :url "http://opensource.org/licenses/MIT"}
  :repl-options  {
    :init-ns table.core}
  :dependencies [ ]
  :scm {:url "git@github.com:rm-hull/table.git"}
  :source-paths ["src"]
  :jar-exclusions [#"(?:^|/).git"]
  :codox {
    :source-paths ["src"]
    :output-path "doc/api"
    :source-uri "http://github.com/rm-hull/table/blob/master/{filepath}#L{line}"  }
  :min-lein-version "2.7.1"
  :profiles {
    :dev {
      :global-vars {*warn-on-reflection* true}
      :plugins [
        [lein-cljfmt "0.5.7"]
        [lein-codox "0.10.3"]
        [lein-cloverage "1.0.10"]]
      :dependencies [
        [org.clojure/clojure "1.10.0"]]}})
