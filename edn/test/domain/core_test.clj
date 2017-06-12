(ns domain.core-test
  (:require [clojure.test :refer :all]
            [domain.core :refer :all]))

(def big-domain '{ Student [{
                             id int
                             first-name [text {:max-size 32}]
                             last-name [text {:max-size 32}]
                             }
                            {:id id}]
                   Enrollment [{
                                student $/Student
                                section $/Section
                                }
                               {:unique [student section]}]
                   Course [{
                            code int
                            name [text {:max-size 32}]
                            description [text {:max-size 8196}]
                            credits [int {:min 1}]
                            department $/Department
                            }
                           {:id code}]
                   Prerequisite [{
                                  course $/Course
                                  prerequisite $/Course
                                  }
                                 {:unique [course prerequisite]}]
                   Department [{
                                code int
                                name [text {:max-size 64}]
                                }
                               {:id code}]
                   Instructor [{
                                id int
                                first-name [text {:max-size 32}]
                                last-name [text {:max-size 32}]
                                department $/Department
                                }
                               {:id id}]
                   Available-To-Instruct {
                                          course $/Course
                                          instructor $/Instructor
                                          }
                   Section [{
                             id int
                             term [Term {:format ":semester :year"}]
                             building $/Building
                             time time
                             course $/Course
                             instructor $/Instructor
                             room-number int
                             }
                            {:id id}]
                   Term {
                         year [int {:min 1900 :max 2100}]
                         semester [text "FALL" "SPRING" "SUMMER"]
                         }
                   Building {
                             name [text {:max-size 128}]
                             address Address
                             }
                   Address {
                            street1 [text {:max-size 128}]
                            street2 [?/text {:max-size 32}]
                            city [text {:max-size 128}]
                            state [text "WI" "MN" "AK"]
                            zip int
                            }
                   })

(def small-domain '{
                    Artist {
                            name [text {:max-size 32}]
                            albums */Album
                            }
                    Album {
                           title [text {:max-size 128}]
                           year ?/int
                           tracks */Track
                           }
                    Track {
                           title [text {:max-size 128}]
                           duration duration
                           }
                    })

(deftest test-objectify
  (is (= (objectify small-domain)
         [{:class-name :Artist
           :fields [{:field-name :name
                     :type :text
                     :cardinality :required
                     :max-size 32}
                    {:field-name :albums
                     :type :Album
                     :cardinality :one-to-many}]}
          {:class-name :Album
           :fields [{:field-name :title
                     :type :text
                     :cardinality :required
                     :max-size 128}
                    {:field-name :year
                     :type :int
                     :ordinality :optional}
                    {:field-name :tracks
                     :type :Track
                     :cardinality :one-to-many}]}
          {:class-name :Track
           :fields [{:field-name :title
                     :type :text
                     :cardinality :required
                     :max-size 128}
                    {:field-name :duration
                     :type :duration
                     :cardinality :required}]}])))

(deftest test-sqlize-small
  (is (= (sqlize small-domain)
         "CREATE TABLE Artist (
           artist_id LONG GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
           name VARCHAR(32) NOT NULL
         );
         CREATE TABLE Album (
           album_id LONG GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
           title VARCHAR(128) NOT NULL,
           year NUMBER,
           artist_id LONG NOT NULL,
           CONSTRAINT artist_fkey REFERENCES Artist (artist_id)
         );
         CREATE TABLE Track (
           track_id LONG GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
           title VARCHAR(128) NOT NULL,
           duration LONG NOT NULL,
           album_id LONG NOT NULL,
           constraint album_fkey REFERENCES Album (album_id)
         );")))

(deftest test-schemafy-small
  (is (= (schemafy small-domain "Discography")
         "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
         <xs:schema
         xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"
         targetNamespace=\"https://www.w3schools.com\"
         xmlns=\"https://www.w3schools.com\"
         elementFormDefault=\"qualified\">
         <xs:element name=\"Discography\" type=\"Discography\"/>
         <xs:complexType name=\"Discography\">
         <xs:sequence maxOccurs=\"unbounded\">
         <xs:element name=\"Artist\" type=\"Artist\"/>
         </xs:sequence>
         </xs:complexType>
         <xs:complexType name=\"Artist\">
         <xs:sequence maxOccurs=\"unbounded\">
         <xs:element name=\"Album\" type=\"Album\"/>
         </xs:sequence>
         <xs:attribute name=\"name\" use=\"required\">
         <xs:simpleType>
         <xs:restriction base=\"xs:string\">
         <xs:maxLength value=\"32\"/>
         </xs:restriction>
         </xs:simpleType>
         </xs:attribute>
         </xs:complexType>
         <xs:complexType name=\"Album\">
         <xs:sequence maxOccurs=\"unbounded\">
         <xs:element name=\"Track\" type=\"Track\"/>
         </xs:sequence>
         <xs:attribute name=\"title\" use=\"required\">
         <xs:simpleType>
         <xs:restriction base=\"xs:string\">
         <xs:maxLength value=\"128\"/>
         </xs:restriction>
         </xs:simpleType>
         </xs:attribute>
         <xs:attribute name=\"year\" type=\"xs:int\"/>
         </xs:complexType>
         <xs:complexType name=\"Track\">
         <xs:attribute name=\"title\" use=\"required\">
         <xs:simpleType>
         <xs:restriction base=\"xs:string\">
         <xs:maxLength value=\"128\"/>
         </xs:restriction>
         </xs:simpleType>
         </xs:attribute>
         <xs:attribute name=\"duration\" type=\"xs:positiveInteger\" use=\"required\"/>
         </xs:complexType>
         </xs:schema>")))