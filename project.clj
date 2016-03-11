(defproject start-clj-sel "0.1.0-SNAPSHOT"
  :iijjdescription "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.logic "0.8.10"]
                 [org.clojure/data.json "0.2.6"]

                 ;; choose between two library...
                 [hickory "0.6.0"]
                 [clj-tagsoup/clj-tagsoup "0.3.0" :exclusions [org.clojure/clojure]]

                 [com.codeborne/phantomjsdriver "1.2.1"
                  :exclusion [org.seleniumhq.selenium/selenium-java
                              org.seleniumhq.selenium/selenium-server
                              org.seleniumhq.selenium/selenium-remote-driver]]
                 [org.seleniumhq.selenium/selenium-remote-driver "2.52.0"]
                 [org.seleniumhq.selenium/selenium-java "2.52.0"]
                 [org.seleniumhq.selenium/selenium-server "2.52.0"]
                 [clj-webdriver "0.7.1"]

                 [io.aviso/taxi-toolkit "0.2.3"]

                 [clj-http "2.0.1"]]
  :profiles {:dev
             {:dependencies
              [[fact/lein-light-nrepl "0.1.3"]]}}
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]}
  )
