(defproject gered/config "0.1"
  :description  "Management of application configuration stored in an external EDN file."
  :url          "https://github.com/gered/config"
  :license      {:name "MIT License"
                 :url  "http://opensource.org/licenses/MIT"}

  :dependencies [[org.clojure/tools.logging "0.3.1"]]

  :profiles     {:provided
                 {:dependencies [[org.clojure/clojure "1.8.0"]]}})
