(ns snapper.core
  "Badass webpage grabber and streamer"
  ;(:require ['snapper.gifsockets.core :as 'gifsockets] ['snapper.server :as 'server])
  (:require [ring.adapter.jetty :as jetty]
            [snapper.gifsockets :as gifsockets]
            [snapper.photos     :as photos]
            [clojure.java.io    :as io]
            [ring.util.io       :as ringio]
            ring.middleware.reload-modified
            ring.middleware.params
            )
  (:gen-class))

(defn- printit [x] (println x) x)

(defn stream-gif
  [page output-stream]
  (try
    (println "Snapping" page)
    (let [encoder (gifsockets/create-gif output-stream)] (
      (doseq [i (range 5)]
        (println "Snap #" i)
        (gifsockets/add-image encoder (photos/png-to-gif (photos/snap page)))
        (Thread/sleep 300))
      (.finish encoder)
      (println "Closing encoder")))
  (catch Exception e
    (println "Caught" (.getMessage e) "failure," (class e))
    (println (.printStackTrace e)))))

(defn gif-handler
  "Respond with gifs"
  [request]
  (println (:uri request))
  (if (= (:uri request) "/stream")
    (let [pipe-in (ringio/piped-input-stream
        (fn [output-stream] (
          stream-gif((:page (:params request)), output-stream)
        ))
      )]
      {:status 200 :headers {"Content-Type" "image/gif"} :body pipe-in})
    {:status 404 :headers {} :body ""}))

(defn -main
  "Go baby go"
  [& args]
  (jetty/run-jetty
    (ring.middleware.params/wrap-params (ring.middleware.reload-modified/wrap-reload-modified #'gif-handler ["src"]))
    {:port 8081}))

(defn app
  [& args]
  (-> gif-handler ring.middleware.params/wrap-params))
