(ns config.core
  (:refer-clojure :exclude [load get])
  (:require
    [clojure.java.io :as io]
    [clojure.edn :as edn]
    [clojure.tools.logging :as log]))

(def default-options
  {
   ; whether to log a warning whenever config.core/get is called to access config values
   ; which are undefined in the configuration
   :log-undefined?   true

   ; whether to throw an exception whenever config.core/get is called to access config values
   ; which are undefined in the configuration
   :throw-undefined? false
   })

(defn load-edn-config
  [f]
  (edn/read-string (slurp f)))

(defn load
  "Loads and returns an EDN configuration stored in a file. If the file is not
   specified tries to load from 'config.edn' in the current directory.

   For supported options, see config.core/default-options."
  {:arglists '([]
               [options]
               [f]
               [f options])}
  [& args]
  (let [[f options] (if (map? (first args))
                      [nil (first args)]
                      args)]
    {:options (merge default-options options)
     :config  (load-edn-config (or f "config.edn"))}))

(defn get
  "Returns a value from a loaded configuration under the path ks. May log a warning
   or throw an exception if the value is undefined in the configuration depending on
   the options specified when the configuration was loaded."
  [config & ks]
  (let [options (:options config)
        value   (get-in (:config config) ks ::undefined)]
    (if (= ::undefined value)
      (do
        (if (:log-undefined? options) (log/warn "Read of undefined configuration value" ks))
        (if (:throw-undefined? options) (throw (ex-info (str "Read of undefined configuration value " ks) {:ks ks :config (:config config)}))))
      value)))
