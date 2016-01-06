(ns shipper.warehouse
  (:require [shipper.tools :refer [qconn qdisc listener stop-listener]]
            [shipper.conf :refer [config]]
            [mount.core :refer [defstate]]
            [clojure.core.async :refer [chan put! close!]]))

(defstate warehouse-queue 
  :start (qconn config)
  :stop (qdisc warehouse-queue))

(defstate ready-to-ship
  :start (chan)
  :stop (close! ready-to-ship))

;; connects ready-to-ship to warehouse-queue, 
;; since in reality warehouse-queue is "some", potentially blocking, queue
(defstate warehouse-listener
  :start (listener #(put! ready-to-ship %)
                   warehouse-queue)
  :stop (stop-listener warehouse-listener))
