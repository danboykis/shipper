(def +version+ "0.1.0-SNAPSHOT")

(set-env!
  :source-paths #{"src"}
  :dependencies '[[org.clojure/clojure    "1.7.0"]
                  [org.clojure/core.async "0.2.374"]
                  [mount "0.1.8"]

                  [boot/core              "2.5.1"           :scope "provided"]
                  [adzerk/bootlaces       "0.1.13"          :scope "test"]])


(require '[adzerk.bootlaces :refer :all])

(bootlaces! +version+)

(task-options!
  pom {:project     'shipper
       :version     +version+
       :description "mount sample app"
       :url         "https://github.com/danboykis/shipper"
       :scm         {:url "https://github.com/danboykis/shipper"}
       :license     {"Eclipse Public License"
                     "http://www.eclipse.org/legal/epl-v10.html"}})
