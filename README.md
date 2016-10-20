# Table
[![Build Status](https://travis-ci.org/rm-hull/table.svg?branch=master)](http://travis-ci.org/rm-hull/table)
[![Coverage Status](https://coveralls.io/repos/rm-hull/table/badge.svg?branch=master)](https://coveralls.io/r/rm-hull/table?branch=master)
[![Dependencies Status](https://jarkeeper.com/rm-hull/table/status.svg)](https://jarkeeper.com/rm-hull/table)
[![Downloads](https://jarkeeper.com/rm-hull/table/downloads.svg)](https://jarkeeper.com/rm-hull/table)
[![Clojars Project](https://img.shields.io/clojars/v/rm-hull/table.svg)](https://clojars.org/rm-hull/table)
[![Maintenance](https://img.shields.io/maintenance/yes/2016.svg?maxAge=2592000)]()

Display ascii tables that fit in your terminal for almost any data
structure.

**NOTE:** This is a diverging fork of https://github.com/cldwalker/table.
All rights of the original author reserved.

### Pre-requisites

You will need [Leiningen](https://github.com/technomancy/leiningen) 2.6.1 or above installed.

### Building

To build and install the library locally, run:

    $ cd table
    $ lein test
    $ lein install

### Including in your project

There is a version hosted at [Clojars](https://clojars.org/rm-hull/table).
For leiningen include a dependency:

```clojure
[rm-hull/table "0.6.0"]
```

For maven-based projects, add the following to your `pom.xml`:

```xml
<dependency>
  <groupId>rm-hull</groupId>
  <artifactId>table</artifactId>
  <version>0.6.0</version>
</dependency>
```

## API Documentation

See [www.destructuring-bind.org/table](http://www.destructuring-bind.org/table/) for API details.

## Usage

To use in a library:

    (use '[table.core :only [table]])

table handles rendering combinations of maps, vecs, lists and sets nested
in one another.

    $ lein repl
    user=> (use 'table.core)
    nil

    ; These three yields the same table
    user=> (table [["1" "2"] ["3" "4"]])
    user=> (table '((1 2) (3 4)))
    user=> (table #{[1 2] [3 4]})
    +---+---+
    | 1 | 2 |
    +---+---+
    | 3 | 4 |
    +---+---+

    user=> (table [{:a 11} {:a 3 :b 22}])
    +----+----+
    | a  | b  |
    +----+----+
    | 11 |    |
    | 3  | 22 |
    +----+----+

table can render different styles of tables:

    user=> (table [ [1 2] [3 4]] :style :unicode)
    ┌───┬───┐
    │ 1 │ 2 │
    ├───┼───┤
    │ 3 ╎ 4 │
    └───┴───┘

    user=> (table [ [1 2] [3 4]] :style :org)
    |---+---|
    | 1 | 2 |
    |---+---|
    | 3 | 4 |
    |---+---|

    # Yes, these will generate tables for github's markdown
    user=> (table [ [10 20] [3 4]] :style :github-markdown)

    | 10 | 20 |
    |--- | ---|
    | 3  | 4  |

    user=> (table [ [10 20] [3 4]] :style :borderless)

    10  20
    ------
    3   4

table can also render custom styles:

    user> (table [[10 20] [3 4]] :style {:top ["◤ " " ▼ " " ◥"]
                                 :top-dash "✈︎"
                                 :middle ["▶︎ " "   " " ◀︎"]
                                 :dash "✂︎"
                                 :bottom ["◣ " " ▲ " " ◢"]
                                 :bottom-dash "☺︎"
                                 :header-walls ["  " "   " "  "]
                                 :body-walls ["  " "   " "  "] })
    ◤ ✈︎✈︎ ▼ ✈︎✈︎ ◥
      10   20
    ▶︎ ✂︎✂︎   ✂︎✂︎ ◀︎
      3    4
    ◣ ☺︎☺︎ ▲ ☺︎☺︎ ◢

table can handle plain maps and vectors of course:

    user=> (table (meta #'doc))
    +-----------+---------------------------------------------------------------+
    | key       | value                                                         |
    +-----------+---------------------------------------------------------------+
    | :macro    | true                                                          |
    | :ns       | clojure.repl                                                  |
    | :name     | doc                                                           |
    | :arglists | ([name])                                                      |
    | :added    | 1.0                                                           |
    | :doc      | Prints documentation for a var or special form given its name |
    | :line     | 120                                                           |
    | :file     | clojure/repl.clj                                              |
    +-----------+---------------------------------------------------------------+

    user=> (table (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader))))
    +--------------------------------------------------+
    | value                                            |
    +--------------------------------------------------|
    | file:/Users/me/code/gems/table/test/             |
    | file:/Users/me/code/gems/table/src/              |
    | file:/Users/me/code/gems/table/dev-resources     |
    | file:/Users/me/code/gems/table/resources         |
    | file:/Users/me/code/gems/table/target/classes/   |
    ...

## Configuration

If your terminal width isn't being auto-detected, you can execute this in
your shell before using the repl: `export COLUMNS`. Alternatively you can
bind/alter table.width/\*width\* to your desired width.

## License

### The MIT LICENSE

Copyright (c) 2012 Gabriel Horner

Copyright (c) 2016 Richard Hull

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
