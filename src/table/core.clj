(ns table.core
  (:require
   [clojure.string :as s]
   [table.width :refer :all]
   [table.ansi :refer :all]))

(declare style-for format-cell render-rows-with-fields escape-newline render-rows table-str)

(def ^:dynamic *style* :plain)
(def ^:private walls ["| " " | " " |"])
(def ^:private styles
  {:plain {:top ["+-" "-+-" "-+"], :middle ["+-" "-+-" "-+"] :bottom ["+-" "-+-" "-+"]
           :dash "-" :header-walls walls :body-walls walls}
   :rst {:top ["+-" "-+-" "-+"], :middle ["+=" "=+=" "=+"] :bottom ["+-" "-+-" "-+"] :body ["+-" "-+-" "-+"]
         :dash "-" :middle-dash "=" :header-walls walls :body-walls walls}

   :org {:top ["|-" "-+-" "-|"], :middle ["|-" "-+-" "-|"] :bottom ["|-" "-+-" "-|"]
         :dash "-" :header-walls walls :body-walls walls}
   :unicode {:top ["┌─" "─┬─" "─┐"] :middle ["├─" "─┼─" "─┤"] :bottom ["└─" "─┴─" "─┘"]
             :dash "─" :header-walls ["│ " " │ " " │"] :body-walls ["│ " " ╎ " " │"]}
   :unicode-3d {:top ["┌─" "─┬─" "─╖"] :middle ["├─" "─┼─" "─╢"] :bottom ["╘═" "═╧═" "═╝"]
                :top-dash "─" :dash "─" :bottom-dash "═"
                :header-walls ["│ " " │ " " ║"] :body-walls ["│ " " │ " " ║"]}
   :github-markdown {:top ["" "" ""] :middle ["|-" " | " "-|"] :bottom ["" "" ""]
                     :top-dash "" :dash "-" :bottom-dash "" :header-walls walls :body-walls walls}
   :borderless {:top ["" "" ""] :middle ["" "--" ""] :bottom ["" "" ""]
                :top-dash "" :dash "-" :bottom-dash "" :header-walls ["" "  " ""] :body-walls ["" "  " ""]}})

(defn table
  "Generates an ascii table for almost any input that fits in your terminal.
   Multiple table styles are supported.

   Options:

   * :sort   When set with field name, sorts by field name. When set to true
             sorts by first column. Default is false.
   * :fields An optional vector of fields used to control ordering of fields.
             Only works with rows that are maps.
   * :desc   When set to true, displays row count after table. Default is nil.
   * :style  Sets table style. Available styles are :plain, :org, :unicode and
             :github-markdown. Default is :plain."
  [& args]
  (println (apply table-str args)))

(defn table-str
  "Same options as table but returns table as a string"
  [args & {:keys [style] :or {style :plain} :as options}]
  (binding [*style* (if (map? style) style (style styles))]
    (apply str (s/join "\n" (render-rows args (if (map? options) options {}))))))

(defn- inflate
  "Calculates the length of the longest row of columns, and returns the rows
   each inflated to that number of columns."
  [rows]
  (let [max-cols (reduce max 0 (map count rows))]
    (map #(take max-cols (lazy-cat % (repeat nil))) rows)))

(defn- generate-rows-and-fields
  "Returns rows and fields. Rows are a vector of vectors containing string cell values."
  [table options]
  (let [top-level-vec (not (coll? (first table)))
        fields (vec (cond
                 top-level-vec [:value]
                 (map? (first table)) (or (:fields options) (distinct (vec (flatten (map keys table)))))
                 (map? table) [:key :value]
                 :else (first (inflate table))))
        rows (cond
               top-level-vec (map #(vector %) table)
               (map? (first table)) (map #(map (fn [k] (get % k)) fields) table)
               (map? table) table
               :else (rest (inflate table)))
        rows (map (fn [row] (map str row)) rows)
        sort-opt (options :sort)
        rows (if (and sort-opt (some #{sort-opt} (conj fields true)))
               (sort-by #(nth % (if (true? sort-opt) 0 (.indexOf #^java.util.List fields sort-opt))) rows)
               rows)
        rows (->> rows (map vec) (map (fn [row] (map escape-newline row))))]
    [rows fields]))

(defn- render-rows
  "Generates a list of formatted string rows given almost any input"
  [table options]
  (let [[rows fields] (generate-rows-and-fields table options)
        rendered-rows (render-rows-with-fields rows fields options)]
    (if (:desc options)
      (concat rendered-rows [(format "%s rows in set" (count rows))])
      rendered-rows)))

(defn- wrap-row
  [row [beg mid end]]
  (str beg (s/join mid row) end))

(defn- render-rows-with-fields [rows fields options]
  (let [headers (map #(if (keyword? %) (name %) (str %)) fields)
        widths (get-widths (cons headers rows))
        fmt-row (fn [row] (map format-cell row widths))
        headers (fmt-row headers)
        border-for (fn [section dash-key]
                     (let [dash (or (style-for dash-key) (style-for :dash))]
                       (wrap-row (map #(s/join (repeat % dash))
                                      widths)
                                 (style-for section))))
        header (wrap-row headers (style-for :header-walls))
        body (map #(wrap-row (fmt-row %) (style-for :body-walls)) rows)
        body (if (style-for :body)
               (rest (interleave (repeat (border-for :body :body-dash)) body))
               body)]

    (concat [(border-for :top :top-dash) header (border-for :middle :middle-dash)]
            body [(border-for :bottom :bottom-dash)])))

(defn- escape-newline [string]
  (s/replace string "\n" (char-escape-string \newline)))

(defn- style-for [k] (k *style*))

(def spaces (s/join (repeat 256 " ")))

(defn- pad-right [string width]
  (let [len (count (strip-ansi string))]
    (str (subs spaces 0 (- width len)) string)))

(defn- pad-left [string width]
  (let [len (count (strip-ansi string))]
    (str string (subs spaces 0 (- width len)))))

(defn- truncate [string width pad-fn]
  (if (> (count (strip-ansi string)) width)
    (suffix-reset-ansi (str (subs string 0 (- width 3)) "..."))
    (pad-fn string width)))

(defn format-cell [string width]
  (if (zero? width)
    ""
    (truncate string width pad-left)))
