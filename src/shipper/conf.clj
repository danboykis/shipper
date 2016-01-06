(ns shipper.conf
  (:require [clojure.edn :as edn]
            [mount.core :refer [defstate]]))

(defn load-config [path]
  (-> path slurp edn/read-string))

(defstate config :start (load-config "resources/conf.edn"))
