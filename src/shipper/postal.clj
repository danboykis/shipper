(ns shipper.postal
  (:require [shipper.tools :refer [qconn qdisc listener stop-listener]]
            [shipper.conf :refer [config]]
            [mount.core :refer [defstate]]
            [clojure.core.async :refer [chan put! close!]]))

(defstate postal-queue 
  :start (qconn config)
  :stop (qdisc postal-queue))

(defstate go-postal
  :start (chan)
  :stop (close! go-postal))

;; connects go-postal to postal-queue, 
;; since in reality postal-queue is "some", potentially blocking, queue
(defstate postal-listener
  :start (listener #(put! postal-queue %)
                   go-postal)
  :stop (stop-listener postal-listener))
