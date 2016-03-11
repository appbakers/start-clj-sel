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
(defn local-grid-server-existing?
  [grid-server]
  (let [has-grid-server (if (nil? grid-server) false true)
        has-webdriver-server (if (nil? (:webdriver-server grid-server))
                                false
                                true)]
  (if (and has-grid-server has-webdriver-server)
    true
    false)))

(comment "define grid-server")
(def grid-server)
(comment "define remote-server")
(def remote-driver)
(defn is-remote-driver?
  [the-driver]
  (not (empty? (bean the-driver))))

(defn alive?
  [a-driver]
  (try
    (println a-driver)
    true
    (catch org.openqa.selenium.WebDriverException e false)))

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
          get-remote-driver (fn [the-server] (if (alive? remote-driver)
                                                 remote-driver
                                                  (tr-svr/start-remote-driver
                                                   the-server
                                                   {:browser :chrome}
                                                   "about:blank")))]
      (if (local-grid-server-existing? grid-server)
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
