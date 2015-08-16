(ns zelkova-quil-flyer.signal
  (:require [zelkova-quil-flyer.vector-math :as v]
            [jamesmacaulay.zelkova.signal :as z]
            [jamesmacaulay.zelkova.mouse :as mouse]
            [jamesmacaulay.zelkova.keyboard :as keyboard]
            [jamesmacaulay.zelkova.time :as time]))

(defn inertia
  [state time-delta]
  (let [velocity (-> state :flyer :velocity)
        translation (v/multiply velocity time-delta 0.2)]
    (update-in state
               [:flyer :position]
               (partial v/add translation))))

(defn acceleration
  [state toggle]
  (if toggle
    (let [angle (-> state :flyer :angle)]
      (update-in state
                 [:flyer :velocity]
                 (fn [velocity]
                   (->> angle
                        (v/radians->vector)
                        (v/with-magnitude 0.2)
                        (v/add velocity)
                        (v/limit-magnitude 1)))))
    state))

(defn steering
  [state mouse-pos]
  (let [flyer-pos (-> state :flyer :position)
        diff      (v/subtract mouse-pos flyer-pos)
        angle     (v/vector->radians diff)]
    (assoc-in state [:flyer :angle] angle)))

(defn my-stream
      []
      (z/constant [10,10]))

(defn app-signal
  [init-state]
  (let [time-deltas (time/fps 30)
        inputs      (->> (z/map vector
                                time-deltas
                                keyboard/space
                                ;mouse/position
                                )
                         (z/sample-on time-deltas))]
    (z/reductions (fn [state [time-delta spacebar?          ;mouse-pos
                              ]]
                    (-> state
                        (inertia time-delta)
                        (acceleration spacebar?)
                        ;(steering mouse-pos)
                        )
                    )
                  init-state
                  inputs)))
