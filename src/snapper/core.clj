(ns snapper.core
  "Badass webpage grabber and streamer"
  (:require [ring.adapter.jetty  :as jetty]
            [snapper.gifsockets  :as gifsockets]
            [snapper.photos      :as photos]
            [clojure.java.io     :as io]
            [ring.util.io        :as ringio]
            [clojure.tools.trace :as trace]
            ring.middleware.reload-modified
            ring.middleware.params
            )
  (:gen-class))

(defn stream-gif
  [page output-stream]
  (println "Snapping" page)
  (let [encoder (gifsockets/create-gif output-stream)] (
    (doseq [png (photos/snaps page)]
      (println png)
      (gifsockets/add-image encoder (photos/png-to-gif png)))
    (.finish encoder))))

(defn gif-handler
  "Respond with gifs"
  [request]
  (println request)
  (if (= (:uri request) "/stream.gif")
    (let [pipe-in (ringio/piped-input-stream #(stream-gif (get (:params request) "page") %))]
      {:status 200 :headers {"Content-Type" "image/gif"} :body pipe-in})
    {:status 404 :headers {} :body ""}))

(defn -main
  "Go baby go"
  [& [port]]
  (let [port (Integer. port)]
    (jetty/run-jetty
      (ring.middleware.params/wrap-params (ring.middleware.reload-modified/wrap-reload-modified #'gif-handler ["src"]))
      {:port port})))

(defn app
  "Go baby go"
  [& args]
  (ring.middleware.params/wrap-params #'gif-handler))
