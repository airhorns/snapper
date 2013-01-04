(ns snapper.photos
  (:require clojure.java.shell
            ring.util.codec
            clojure.java.io )
  (:import java.awt.image.BufferedImage
           java.awt.Graphics2D
           java.awt.Color
           [java.io File IOException ByteArrayInputStream]
           javax.imageio.ImageIO ))

(def snap-location (.getFile (clojure.java.io/resource "coffee/snap.coffee")))

(defn- stupid
  [x]
  (get x :out))

(defn- printit [x] (println x) x)

(defn snap
  "Get a PNG photo of a url"
  [url]
  (->
    (clojure.java.shell/sh "phantomjs" snap-location url)
    stupid
    ring.util.codec/base64-decode
    ByteArrayInputStream.
    ImageIO/read ))

(defn png-to-gif
  "Convert an ABGR PNG to a RGB gif image"
  [png]
  (let [newImage (BufferedImage. (.getWidth png) (.getHeight png) BufferedImage/TYPE_INT_RGB)]
    (.drawImage (.createGraphics newImage) png 0 0 Color/WHITE nil)
    newImage))
