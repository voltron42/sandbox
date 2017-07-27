(ns domain.spec-test
    (:require [clojure.test :refer :all]
              [domain.spec :refer :all]
              [clojure.spec.alpha :as s]))

(deftest test-domain-spec
    (is (s/valid? :domain.spec/domain '{Artist {name [text {:max-size 32}]
                                                albums */Album}
                                        Album {title [text {:max-size 128}]
                                               year ?/int
                                               tracks */Track}
                                        Track {title [text {:max-size 128}]
                                               duration duration
                                               }}))
    (is (s/valid? :domain.spec/domain '   {Student [{id int
                                                     first-name [text {:max-size 32}]
                                                     last-name [text {:max-size 32}]}
                                                    {:id id}]
                                           Enrollment [{student $/Student
                                                        section $/Section}
                                                       {:unique [student section]}]
                                           Course [{code int
                                                    name [text {:max-size 32}]
                                                    description [text {:max-size 8196}]
                                                    credits [int {:min 1}]
                                                    department $/Department}
                                                   {:id code}]
                                           Prerequisite [{course $/Course
                                                          prerequisite $/Course}
                                                         {:unique [course prerequisite]}]
                                           Department [{code int
                                                        name [text {:max-size 64}]}
                                                       {:id code}]
                                           Instructor [{id int
                                                        first-name [text {:max-size 32}]
                                                        last-name [text {:max-size 32}]
                                                        department $/Department}
                                                       {:id id}]
                                           Available-To-Instruct {course $/Course
                                                                  instructor $/Instructor}
                                           Section [{id int
                                                     term [Term {:format ":semester :year"}]
                                                     building $/Building
                                                     time time
                                                     course $/Course
                                                     instructor $/Instructor
                                                     room-number int}
                                                    {:id id}]
                                           Term {year [int {:min 1900 :max 2100}]
                                                 semester [text "FALL" "SPRING" "SUMMER"]}
                                           Building {name [text {:max-size 128}]
                                                     address Address}
                                           Address {street1 [text {:max-size 128}]
                                                    street2 [?/text {:max-size 32}]
                                                    city [text {:max-size 128}]
                                                    state [text "WI" "MN" "AK"]
                                                    zip int}})))