(ns snapper.core
  "Badass webpage grabber and streamer"
  ;(:require ['snapper.gifsockets.core :as 'gifsockets] ['snapper.server :as 'server])
  (:require [snapper.server :as server] 
            [snapper.gifsockets :as gifsockets])
  (:gen-class))



(defn gif-handler
  "Respond with gifs"
  [connection]
  (println "Running")

  (def encoder (gifsockets/create-gif (.getOutputStream connection)))
  ;;
  ;;Now we are ready to send messages to that browser client
  (gifsockets/add-message encoder "Hello gif-sockets")
  ;; now you should see a GIF image with the new message on it.
  (gifsockets/add-message encoder "Zup zup zup")
  (gifsockets/add-message encoder "And so forth")

  (.finish encoder)
  (.close connection)
)

(defn -main
  "Go baby go"
  [& args]
  (def mainserver (server/tcp-server :port 8081 :handler gif-handler))
  (server/start2 mainserver)
  (println "Snapper server running on 8081.")
)
