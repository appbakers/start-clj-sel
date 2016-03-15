(ns start-clj-sel.base-test
  (:require [clj-webdriver.taxi :as t]
            [clj-webdriver.core :as tc]
            [clj-webdriver.remote.server :as tr-svr]
            [io.aviso.taxi-toolkit.index :as tt]
            [io.aviso.taxi-toolkit.fixtures :as fixtures]
            [io.aviso.taxi-toolkit.url :as url]
            [clojure.data.json :as json]
            [clojure.test :refer :all]
            [start-clj-sel.base :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; HOW TO USE SELENIUM REMOTE SERVER AND REMOTE DRIVER
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(comment "START selenium server and driver and assign to `svr-and-drv` VAR")
(def svr-and-drv (renew-local-svrdrv))
(reachable? "127.0.0.1" 4444 5000)


(comment "assign `grid-server` instance to `a-grid-server` VAR")
(def a-grid-server (:grid-server svr-and-drv))
(prn a-grid-server)

(comment "assign `driver` instance to `a-driver` VAR")
(def a-driver (:remote-driver svr-and-drv))
(alive? a-driver)

(comment "set `a-driver` to taxi driver")
(t/set-driver! (:remote-driver (renew-local-svrdrv)))

(comment "위와 같은 기능.
          local-grid-server가 없으면 생성하고,
          local-remote-driver(chrome)가 없으면 생성하고,
          local-remote-driver 가 있으면 생성하지 않고,
          (t/set-driver! 기능을 사용하여 taxi에 driver를 태움.")
(renew-taxi-driver)

(comment "navigate to selenium local directory
          프로젝트의 루트 드렉토리임.")
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

(comment "`html` func는 첫번째 엘레멘트를 찾아서 html로 표현하는 기능.")
(t/html "input[name=q]")

(comment "`find-elements` func는 엘레멘트들을 찾음.")
(t/find-elements {:css "input[name=q]"})

(comment "페이지 소스 보기")
(t/page-source)


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





