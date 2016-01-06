(ns shipper.tools
  (:require [clojure.core.async :refer [chan put! alt! go-loop close!]]))

;; since not using a real queue emulate one with a chan
(defn qconn [conf] (chan))
(defn qdisc [q] (close! q))

(defn listener [f ch]
  (let [stop-ch (chan)]
    (go-loop []
             (alt!
               stop-ch ([_] :no-op)
               ch      ([msg] (f msg) 
                              (recur))))
    {:listener ch :stop stop-ch}))

(defn stop-listener [{:keys [stop]}]
  (put! stop :stop))
