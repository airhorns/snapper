(ns snapper.core
  "Badass webpage grabber and streamer"
  (:require ['snapper.gifsockets.core :as 'gifsockets] ['snapper.server :as 'server]))

;;Then we declare the tcp server
(def mainserver (server/tcp-server :port 8081 :handler gifsockets/gif-handler))
(server/start2 server)
;; wait for a browser connection on port 8081
;; go and open http://localhost:8081/ in Safari or IE6
;; In Chrome it works a bit laggy and in Firefox it doesn't work at all
;;
;; Now let's create the gif encoder that we use to write messages to the browser.
(def encoder (gifsockets/create-gif (.getOutputStream client)))
;;
;;Now we are ready to send messages to that browser client
(gifsockets/add-message encoder "Hello gif-sockets")
;; now you should see a GIF image with the new message on it.
(gifsockets/add-message encoder "Zup zup zup")
(gifsockets/add-message encoder "And so forth")
;;
;; Now let's clean up and close the connection
(.finish encoder)
(.close client)
