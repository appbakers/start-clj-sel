(ns start-clj-sel.base
  (:require [clj-webdriver.taxi :as t]
            [clj-webdriver.core :as tc]
            [clj-webdriver.remote.server :as tr-svr]
            [io.aviso.taxi-toolkit.index :as tt]
            [io.aviso.taxi-toolkit.fixtures :as fixtures]
            [io.aviso.taxi-toolkit.url :as url]
            [clojure.data.json :as json])
  (:import (org.openqa.selenium.phantomjs PhantomJSDriver)
           (org.openqa.selenium.remote DesiredCapabilities)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; BASIC FUNCTION DEFINITIONS
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn reachable?
  "Return boolean.
   "
  [hostname port timeout-ms]
  (try
    (let [inet-soc (java.net.InetSocketAddress. hostname port)
          soc (java.net.Socket.)]
      (.connect soc inet-soc timeout-ms))
    true
    (catch Exception e false)))
(reachable? "127.0.0.1" 4444 5000)
(prn (reachable? "127.0.0.1" 4444 5000))

(defn local-grid-server?
  [grid-server]
  (let [has-grid-server (let [port (get-in grid-server [:connection-params :port])
                              host (get-in grid-server [:connection-params :host])
                              timeout-ms 5000]
                             (if (or
                              (nil? grid-server)
                              (not (reachable? host port timeout-ms)))
                          false
                          true))
        has-webdriver-server (if (nil? (:webdriver-server grid-server))
                                false
                                true)]
  (if (and has-grid-server has-webdriver-server)
    true
    false)))

(defn remote-driver?
  "check if remote driver exists"
  [the-driver]
  (and (not-empty (bean the-driver))
       (= "class org.openqa.selenium.remote.RemoteWebDriver"
          (type (get the-driver :webdriver)))))

(defn driver-alive?
  ""
  [the-driver]
  (try
    (println the-driver)
    true
    (catch org.openqa.selenium.WebDriverException e false)))

(comment "define grid-server")
(def grid-server)
(comment "check grid-server existing?")
(local-grid-server? grid-server)
(prn grid-server)
(prn (local-grid-server? grid-server))

(comment "define remote-server")
(def remote-driver)
(comment "remote-driver initialized with no value.")
(remote-driver? remote-driver)
(prn remote-driver)
(prn (remote-driver? remote-driver))

(defn renew-local-svrdrv
  "Alpha :TODO if default server exists then reuse it.
  Returns map {:grid-server local-remote-server :driver local-remote-driver}
  :grid-server is instance of RemoteServer
  :driver is instance of RemoteWebDriver.
  chromdriver executable should be in path( okay if the executable is in the project root )
  RemoteWebDriver instance support upload function if grid-server is in local"
  ([]
    (let [port 4444
          host "127.0.0.1"
          get-remote-driver (fn [the-server] (if (driver-alive? remote-driver)
                                                 remote-driver
                                                  (tr-svr/start-remote-driver
                                                   the-server
                                                   {:browser :chrome}
                                                   "about:blank")))]
      (if (local-grid-server? grid-server)
        (do
          (if-not (alive? remote-driver)
            (def remote-driver (get-remote-driver grid-server)))
          {:grid-server grid-server :remote-driver remote-driver})
        (let [[a-server a-driver]
              (tr-svr/new-remote-session {:port port :host host}
                                         {:browser :chrome})]
          (def grid-server a-server)
          (def remote-driver a-driver)
          {:grid-server grid-server :remote-driver remote-driver})))))

(defn renew-taxi-driver
  []
  (t/set-driver! (:remote-driver (renew-local-svrdrv))))
