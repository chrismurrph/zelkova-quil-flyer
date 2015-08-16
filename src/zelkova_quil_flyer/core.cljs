(ns ^:figwheel-always zelkova-quil-flyer.core
    (:require [quil.core :as q :include-macros true]
              [quil.middleware]
              [jamesmacaulay.zelkova.signal :as z]
              [zelkova-quil-flyer.signal :as signal]
              [zelkova-quil-flyer.random :as random]
              ))

;(enable-console-print!)

(def width 800)
(def height 600)
;; These are guesses
(def obj-width 50)
(def obj-height 50)

(defn centre-object
  [container-len object-len]
  (let [container-midway (/ container-len 2)
        obj-midway (/ object-len 2)]
    (- container-midway obj-midway)))

(defn start-direction-1
  []
  (let [min 0
        max (* 2 q/PI)
        res (random/random-float min max)
        res-res (first res)]
    (println res-res)
    res-res))

(defn start-direction-2
  []
  (let [res -1.0]
    (println res)
    res))

(defn setup
  []
  ;; zero is heading east for the angle
  ;; 2nd thing in velocity is direction in radians
  ;; -1.55 will head straight up and 1.55 straight down
  ;; 0 is like 90 degrees - off to the right
  ;; angle is initial position so head is laid on the side
  (let [init-state {:flyer {:position [(centre-object width obj-width) (centre-object height obj-height)]
                            :angle    0                      ;-1.55
                            :velocity [0.1 (start-direction-1)]
                            :image (q/request-image "flyer.png")
                            }}]
    (-> init-state
        (signal/app-signal)
        (z/pipe-to-atom (q/state-atom)))
    (q/frame-rate 30)
    (q/no-smooth)
    (q/rect-mode :center)
    (q/shape-mode :center)
    (q/image-mode :center)
    init-state))

(defn draw-flyer
  [{:keys [image position angle]}]
  (q/reset-matrix)
  (q/fill 255)
  (q/stroke 180)
  (apply q/translate position)
  (q/rotate (+ angle q/HALF-PI))
  (q/scale 0.2)
  (q/image image 0 0))

(defn draw
  [{:keys [flyer]}]
  (q/background 20 20 40)
  (draw-flyer flyer))

(q/defsketch flyer
  :size [width height]
  :setup setup
  :draw draw
  :middleware [quil.middleware/fun-mode])
