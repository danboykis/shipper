(ns shipper.core
  (:require [mount.core :refer [defstate]]
            [shipper.tools :refer [listener stop-listener]]
            [shipper.postal :refer [go-postal]]
            [shipper.warehouse :refer [ready-to-ship]]
            [shipper.conf :refer [config]]))

;; "db" and "emailer" in reality will be defined somewhere else
;; at the edges of the application

(defstate db
  :start (atom {})      ;; i.e. (create-conn config)
  :stop (reset! db {})) ;; i.e. (disconnect db)

;; emulating emailer.. e.g. mailgun, postal, etc..
(defstate emailer
  :start (fn [{:keys [to subject body] :as email}]
           {:sent-email email}))

;; ---------------------------------

(defn notify-customer [to order-num]
  (let [sent (emailer {:to "customer@shipper.com" 
                       :subject (str "Order #" order-num " is ready to ship!")
                       :body "Dear Luke S., ... Respect, Darth V."})]
    (swap! db assoc order-num {:email sent
                               :status :ready-to-ship})))  ;; in reality it will be a :status update/conj/etc.

(defstate notifier
  :start (listener (partial notify-customer go-postal) 
                   ready-to-ship)
  :stop (stop-listener notifier))
