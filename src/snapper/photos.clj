(ns snapper.photos
  (:require ring.util.codec
            clojure.java.io
            conch.sh
            [clojure.tools.trace :as trace])
  (:import java.awt.image.BufferedImage
           java.awt.Graphics2D
           java.awt.Color
           [java.io File IOException ByteArrayInputStream]
           javax.imageio.ImageIO ))

(def snap-location (.getFile (clojure.java.io/resource "coffee/snap.coffee")))
(def snaps-location (.getFile (clojure.java.io/resource "coffee/snaps.coffee")))
(conch.sh/programs phantomjs)

(defn- base64-to-png
  "Get a PNG BufferedImage from a base64 encoded string"
  [string]
  (->
    string
    ring.util.codec/base64-decode
    ByteArrayInputStream.
    ImageIO/read
    ))


(defn- debug
  [in]
  (println "here")
  (base64-to-png in))

(defn snap
  "Get a PNG photo of a url"
  [url]
  (let [take-out #(:out %)] (
    ->
    (phantomjs snap-location url)
    take-out
    base64-to-png)))

(defn snaps
  "Get a lazy seq of PNG photos of a url"
  [url]
  (map base64-to-png (phantomjs snaps-location url {:seq true})))

(defn png-to-gif
  "Convert an ABGR PNG to a RGB gif image"
  [png]
  (let [newImage (BufferedImage. (.getWidth png) (.getHeight png) BufferedImage/TYPE_INT_RGB)]
    (.drawImage (.createGraphics newImage) png 0 0 Color/WHITE nil)
    newImage))
