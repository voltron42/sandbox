package server

import (
	"../framework"
	"fmt"
	"github.com/gorilla/mux"
	"net/http"
)

func NewServer() *server {
	return &server{}
}

type server struct {
	port        int
	controllers []framework.Controller
}

func (s *server) Listen() error {
	r := mux.NewRouter()
	for _, controller := range s.controllers {
		err := controller.Apply(r)
		if err != nil {
			return err
		}
	}
	return http.ListenAndServe(fmt.Sprintf(":%v", s.port), r)
}
