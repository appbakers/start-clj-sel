(ns start-clj-sel.base
  (:require [clj-webdriver.taxi :as t]
            [clj-webdriver.core :as tc]
            [clj-webdriver.remote.server :as tr-svr]
            [io.aviso.taxi-toolkit.index :as tt]
            [io.aviso.taxi-toolkit.fixtures :as fixtures]
            [io.aviso.taxi-toolkit.url :as url]
            [clojure.data.json :as json]
            )
  (:import (org.openqa.selenium.phantomjs PhantomJSDriver)
           (org.openqa.selenium.remote DesiredCapabilities)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; BASIC FUNCTION DEFINITIONS
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn init-local-svrdrv
  "Alpha :TODO if default server exists then reuse it.
  Returns map {:grid-server local-remote-server :driver local-remote-driver}
  :grid-server is instance of RemoteServer
  :driver is instance of RemoteWebDriver.
  chromdriver executable should be in path( okay if the executable is in the project root )
  RemoteWebDriver instance support upload function if grid-server is in local"
  ([]
    (let [[a-server a-driver]
      (tr-svr/new-remote-session {:port 4444 :host "127.0.0.1"}
                                              {:browser :chrome})]
      (prn (bean (:class (bean a-driver))))
      (prn (bean (:class (bean a-driver))))
        {:grid-server a-server :driver a-driver})))

(defn renew-taxi-driver
  "renew a remote driver instance and set to taxi"
  ([grid-server]
    (let [a-remote-driver (tr-svr/start-remote-driver
                                          grid-server
                                          {:browser :chrome}
                                          "about:blank")]
      (t/set-driver! a-remote-driver)
      (def a-driver a-remote-driver))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; HOW TO USE SELENIUM REMOTE SERVER AND REMOTE DRIVER
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(comment "START selenium server and driver and assign to `svr-and-drv` VAR")
(def svr-and-drv (init-local-svrdrv))

(comment "assign `grid-server` instance to `a-grid-server` VAR")
(def a-grid-server (:grid-server svr-and-drv))

(comment "assign `driver` instance to `a-driver` VAR")
(def a-driver (:driver svr-and-drv))

(comment "set `a-driver` to taxi driver")
(t/set-driver! a-driver)

(comment "renew taxi driver (택시 드라이버가 졸고 있거나/ 사고가 생긴 것 같을 때 실행..)")
(renew-taxi-driver a-grid-server)

(comment "navigate to selenium local directory")
(t/to "http://127.0.0.1:4444")

(comment "navigate to selenium remote driver instance")
(t/to "http://127.0.0.1:4444/wd")

(comment "navimap: key value map for navigation")
(def navimap {
            :domain-url "http://clojure.org"
            :local-picture-1 "C:/Users/Public/Pictures/Sample Pictures/Chrysanthemum.jpg"
            })

(comment "check navimap")
(:domain-url navimap)

(comment "go to domain-url")
(t/to (:domain-url navimap))

(comment "엘레멘트를 찾을 수 있는지 확인.
          빨간 에러 창은 더블클릭으로 없앨 수 있음.")
(t/html "input[name=userId]")

(comment "페이지 소스 보기")
(t/page-source)

(comment "프레임 변경하기")
(t/switch-to-frame "[src*=Login]")

(comment "프레임 변경되었는지 확인")
(t/page-source)

(comment "엘레멘트를 찾을 수 있는지 확인.")
(t/html "input[name=userId]")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; "DO DRIVE TAXI AS YOU WISH"
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;









;;;;;;;
;;
;; Exit
;;
;;;;;;;
(comment "Stop Selenium Grid Server")
(tr-svr/stop a-grid-server)
