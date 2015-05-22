package main

import (
	"encoding/xml"
	"errors"
	"fmt"
	"io/ioutil"
	"reflect"
)

func main() {
	bytes, err := ioutil.ReadFile("./data.xml")
	if err != nil {
		panic(err)
	}
	a := A{}
	err = xml.Unmarshal(bytes, &a)
	if err != nil {
		panic(err)
	}
	for _, item := range a.List {
		fmt.Printf("item: %v\n", item)
	}
}

type A struct {
	List []Inner
}

var childMapOfA = map[string]reflect.Type{
	"b": reflect.TypeOf(B{}),
	"c": reflect.TypeOf(C{}),
	"d": reflect.TypeOf(D{}),
}

func (a *A) UnmarshalXML(d *xml.Decoder, start xml.StartElement) error {
	//var end *xml.EndElement
	fmt.Printf("start: %v\n", start.Name.Local)
	fmt.Printf("end: %v\n", start.End())
	for true {
		t, err := d.Token()
		if err != nil {
			return err
		}
		if reflect.DeepEqual(t, start.End()) {
			fmt.Println("here")
			break
		}
		fmt.Printf("token: %v\n", t)
		fmt.Printf("type: %v\n", typeName(t))
		if reflect.DeepEqual(reflect.TypeOf(t), reflect.TypeOf(start)) {
			bookend, ok := t.(xml.StartElement)
			if ok {
				item, err := getChild(d, bookend, childMapOfA)
				if err != nil {
					return err
				}
				fmt.Printf("item: %v\n", item)
				innerItem, ok := item.(Inner)
				if ok {
					a.List = append(a.List, innerItem)
				}
			}
		}
	}
	return nil
}

func getChild(d *xml.Decoder, start xml.StartElement, childMap map[string]reflect.Type) (interface{}, error) {
	itemType, ok := childMap[start.Name.Local]
	if !ok {
		return nil, errors.New(fmt.Sprintf("Token name not recognized: %v", start.Name.Local))
	}
	item := reflect.New(itemType).Interface()
	err := d.DecodeElement(&item, &start)
	return item, err
}

type Inner interface {
	String() string
	MyData() string
}

type B struct {
	Data string `xml:",chardata"`
}

func (b *B) String() string {
	return "b:" + b.Data
}

func (b *B) MyData() string {
	return b.Data
}

type C struct {
	Data string `xml:",chardata"`
}

func (c *C) String() string {
	return "c:" + c.Data
}

func (c *C) MyData() string {
	return c.Data
}

type D struct {
	Data string `xml:",chardata"`
}

func (d *D) String() string {
	return "d:" + d.Data
}

func (d *D) MyData() string {
	return d.Data
}

func typeName(i interface{}) string {
	return reflect.TypeOf(i).String()
}
