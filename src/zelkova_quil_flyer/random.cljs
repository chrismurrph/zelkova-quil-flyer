(ns zelkova-quil-flyer.random)

;
; When n is 1 true is certain to be returned.
; When n is 0 false is certain to be returned.
; When n is 100 there is a 1% chance of true being returned
;

(enable-console-print!)

(defn chance-one-in
  "If you gave 100 as the argument then there would be a 1% chance of getting true, and 99% chance of getting false"
  [n]
  (if ( = n 0)
    false
    (= 0 (rand-int n))))

(defn coin-flip
  "Randomly returns either true or false"
  []
  (chance-one-in 2))

;;
;; Note that there will be a tendency to not quite go to the sample-size due to the de-duping effect of creating
;; a set. In those cases we recurse until the difference is made up.
;;
(defn random-ints
  [sample-size pop-size]
  (let [res (set (take sample-size (repeatedly #(rand-int pop-size))))
        diff (- sample-size (count res))]
    (if (> diff 0)
      (set (concat res (random-ints diff pop-size)))
      res)))

(defn random-floats
  ([sample-size lowest highest]
    (let [randoms (random-floats sample-size)
          diff (- highest lowest)]
      (map #(+ (* diff %) lowest) randoms)))
  ([sample-size]
   (let [res (set (take sample-size (repeatedly #(rand))))
         missing (- sample-size (count res))]
     (if (> missing 0)
       (set (concat res (random-floats missing)))
       res))))

(defn random-float
  [lowest highest]
  first (random-floats 1 lowest highest))
